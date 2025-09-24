package com.example.service;

import com.example.dao.OrderModifyLogRepository;
import com.example.dto.OrderModifyLog;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async; // 支持异步执行
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor // Lombok注解，为final字段生成构造函数
public class OrderModifyLogService {

    private final OrderModifyLogRepository logRepository;

    /**
     * 记录订单修改日志（异步方法）
     * @param orderId 订单ID
     * @param operatorId 操作人ID
     * @param operatorType 操作人类别
     * @param changedField 被修改的字段
     * @param oldValue 旧值
     * @param newValue 新值
     * @param remark 备注
     */
    @Async // 标记为异步方法，提升主业务性能
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 开启新事务，确保日志即使业务失败也能记录
    public void recordModificationLog(Long orderId, Long operatorId, Integer operatorType,
                                      String changedField, String oldValue, String newValue, String remark) {
        OrderModifyLog log = new OrderModifyLog();
        log.setOrderId(orderId);
        log.setOperatorId(operatorId);
        log.setOperatorType(operatorType);
        //log.setOperationType("MODIFY"); // 可根据操作细化，如 "UPDATE_ADDRESS"
        log.setChangedField(changedField);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setRemark(remark);
        log.setChangeTime(LocalDateTime.now());

        logRepository.save(log);
    }
}