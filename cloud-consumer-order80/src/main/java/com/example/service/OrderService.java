package com.example.service;

import com.example.RedissonDistributedLock;
import com.example.dao.OrderModifyLogRepository;
import com.example.dao.OrderRepository;
import com.example.dto.Order;
import com.example.dto.OrderModificationRequest;
import com.example.dto.OrderModifyLog;
import com.example.exp.BusinessException;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

// OrderService.java
@Service
@Transactional
@Slf4j
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderModifyLogRepository modifyLogRepository;
    
    @Autowired
    private OrderModificationRule modificationRule;

    @Autowired
    private RedissonDistributedLock redissonDistributedLock;

    @Autowired
    private OrderModifyLogService logService;
    
    /**
     * 修改订单信息
     */
    public Order modifyOrder(Long orderId, OrderModificationRequest modificationRequest, 
                           Long operatorId, Integer operatorType) throws BusinessException {
        // 使用分布式锁防止并发修改（可选但推荐）
        String lockKey = "order:modify:" + orderId;
        boolean locked = false;
        try {
            // 尝试获取分布式锁，等待5秒，锁有效期30秒
            locked = tryLock(lockKey, 5, 30);
            if (!locked) {
                throw new BusinessException("系统正忙，请稍后重试");
            }
            
            // 获取订单（带乐观锁）
            Order order = orderRepository.findWithLockingById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
            
            // 验证订单状态是否允许修改
            modificationRule.validateModification(order.getStatus(), 
                modificationRequest.getFieldName());
            
            // 记录修改前的值
            String oldValue = getFieldValue(order, modificationRequest.getFieldName());
            
            // 应用修改
            applyModification(order, modificationRequest);
            
            // 验证业务规则
            validateBusinessRules(order);
            
            // 保存订单（乐观锁会自动检查版本）
            Order savedOrder = orderRepository.save(order);
            
            // 记录修改日志
            recordModificationLog(orderId, modificationRequest, operatorId, 
                operatorType, oldValue);
            
            // 触发相关联动操作
            triggerRelatedOperations(savedOrder, modificationRequest);
            
            return savedOrder;
        } catch (ObjectOptimisticLockingFailureException | BusinessException e) {
            throw new BusinessException("订单已被其他用户修改，请刷新后重试");
        } finally {
            if (locked) {
                releaseLock(lockKey);
            }
        }
    }

    /**
     * 修改订单信息 (使用 JPA 的乐观锁)
     */
    @Transactional(rollbackFor = Exception.class)
    public Order modifyOrder02(Long orderId, OrderModificationRequest request, Long operatorId, Integer operatorType) throws BusinessException {
        // 1. 查询订单
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        // 2. 根据订单状态和请求字段，验证是否允许修改 (你的业务规则)
        modificationRule.validateModification(order.getStatus(), request.getFieldName());

        // 3. 记录修改前的旧值（用于记录日志）
        String oldValue = getFieldValue(order, request.getFieldName()); // 你需要实现这个方法

        // 4. 应用修改到订单对象
        applyModification(order, request); // 你需要实现这个方法，例如 order.setShippingAddress(request.getNewValue());

        Order savedOrder;
        try {
            // 5. 保存订单。JPA的save方法会在执行UPDATE时自动加上"WHERE id=:id AND version=:currentVersion"
            // 如果版本不匹配（数据已被其他事务修改），此处会抛出ObjectOptimisticLockingFailureException
            savedOrder = orderRepository.save(order);

        } catch (ObjectOptimisticLockingFailureException e) {
            // 6. 捕获乐观锁冲突异常，进行友好处理
            log.warn("订单[{}]发生并发修改冲突，请重试。", orderId);
            throw new BusinessException("订单信息已被他人修改，请刷新页面后重试");
        }

        // 7. 记录修改日志（可选，但推荐）
        logService.recordModificationLog(orderId, operatorId, operatorType, "字段", "旧值", "新值", "标记");

        // 8. 触发相关联动操作（如更新库存、通知物流等）
        triggerRelatedOperations(savedOrder, request);

        return savedOrder;
    }


    private String getFieldValue(Order order, @NotBlank String fieldName) {
        return null;
    }

    private void validateBusinessRules(Order order) {
    }

    /**
     * 尝试获取分布式锁
     */
    private boolean tryLock(String key, long waitTime, long leaseTime) {
        // 使用Redis实现分布式锁[9,11](@ref)
        // 实际项目中可使用Redisson等框架提供的分布式锁
        // 这里简化实现
        return redissonDistributedLock.tryLock(key, waitTime, leaseTime);
    }
    
    /**
     * 释放分布式锁
     */
    private void releaseLock(String key) {
        // 实现锁释放逻辑
        redissonDistributedLock.unlock(key);
    }
    
    /**
     * 应用修改到订单对象
     */
    private void applyModification(Order order, OrderModificationRequest request) throws BusinessException {
        switch (request.getFieldName()) {
            case "shippingAddress":
                order.setShippingAddress(request.getNewValue());
                break;
            case "contactPhone":
                order.setContactPhone(request.getNewValue());
                break;
            case "quantity":
                // 修改商品数量需要重新计算金额等复杂逻辑
                updateOrderQuantity(order, request.getNewValue());
                break;
            // 其他字段处理...
            default:
                throw new BusinessException("不支持的修改字段: " + request.getFieldName());
        }
    }

    private void updateOrderQuantity(Order order, @NotBlank String newValue) {
    }

    /**
     * 记录修改日志
     */
    private void recordModificationLog(Long orderId, OrderModificationRequest request, 
                                      Long operatorId, Integer operatorType, String oldValue) {
        OrderModifyLog log = new OrderModifyLog();
        log.setOrderId(orderId);
        log.setOperatorId(operatorId);
        log.setOperatorType(operatorType);
        log.setChangedField(request.getFieldName());
        log.setOldValue(oldValue);
        log.setNewValue(request.getNewValue());
        log.setChangeTime(LocalDateTime.now());
        log.setRemark(request.getRemark());
        
        modifyLogRepository.save(log);
    }
    
    /**
     * 触发相关联动操作
     */
    private void triggerRelatedOperations(Order order, OrderModificationRequest request) {
        // 根据修改类型触发不同的联动操作
        if ("shippingAddress".equals(request.getFieldName())) {
            // 通知物流系统地址变更
            notifyLogisticsSystem(order);
        } else if ("quantity".equals(request.getFieldName())) {
            // 重新计算金额并可能触发退款/补款
            recalculatePayment(order);
            // 调整库存
            adjustInventory(order);
        }
        
        // 发送通知给用户
        sendNotificationToUser(order, request);
    }

    private void sendNotificationToUser(Order order, OrderModificationRequest request) {
        log.info("发送通知给用户");

    }

    private void adjustInventory(Order order) {
        log.info("调整库存");
    }

    private void recalculatePayment(Order order) {
        log.info("重新计算金额并可能触发退款/补款");
    }

    private void notifyLogisticsSystem(Order order) {
        log.info("通知物流系统地址变更");
    }

    public List<OrderModifyLog> getModificationHistory(Long orderId) {
        log.info("获取订单修改历史");
        return null;
    }

    public Set<String> getModifiableFields(Long orderId) {
        log.info("获取订单可修改字段信息");
        return null;
    }
}