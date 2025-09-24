package com.example.service;

import com.example.exp.BusinessException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// OrderModificationRule.java
@Component
public class OrderModificationRule {
    
    // 定义不同状态下允许修改的字段
    private static final Map<Integer, Set<String>> ALLOWED_MODIFICATIONS = new HashMap<>();
    
    static {
        // 待支付状态允许修改的字段
        ALLOWED_MODIFICATIONS.put(OrderStatus.PENDING_PAYMENT.getCode(), 
            Set.of("shippingAddress", "contactPhone", "quantity", "coupon"));
        
        // 已支付/待发货状态允许修改的字段
        ALLOWED_MODIFICATIONS.put(OrderStatus.PAID.getCode(), 
            Set.of("shippingAddress", "contactPhone"));
        ALLOWED_MODIFICATIONS.put(OrderStatus.PENDING_SHIPMENT.getCode(), 
            Set.of("shippingAddress", "contactPhone"));
        
        // 已发货及之后状态不允许修改核心信息
        ALLOWED_MODIFICATIONS.put(OrderStatus.SHIPPED.getCode(), Collections.emptySet());
        ALLOWED_MODIFICATIONS.put(OrderStatus.COMPLETED.getCode(), Collections.emptySet());
        ALLOWED_MODIFICATIONS.put(OrderStatus.CANCELLED.getCode(), Collections.emptySet());
    }
    
    public boolean isFieldModificationAllowed(Integer status, String field) {
        Set<String> allowedFields = ALLOWED_MODIFICATIONS.getOrDefault(status, Collections.emptySet());
        return allowedFields.contains(field);
    }
    
    public void validateModification(Integer currentStatus, String field) throws BusinessException {
        if (!isFieldModificationAllowed(currentStatus, field)) {
            throw new BusinessException("当前订单状态下不允许修改" + field + "字段");
        }
    }
}