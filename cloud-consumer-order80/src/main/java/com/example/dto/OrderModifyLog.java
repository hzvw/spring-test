package com.example.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// OrderModifyLog.java
@Entity
@Table(name = "order_modify_log")
@Data
public class OrderModifyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "operator_id", nullable = false)
    private Long operatorId;
    
    @Column(name = "operator_type", nullable = false)
    private Integer operatorType; // 1-用户, 2-客服, 3-系统
    
    @Column(name = "changed_field", nullable = false)
    private String changedField;
    
    @Column(name = "old_value")
    private String oldValue;
    
    @Column(name = "new_value")
    private String newValue;
    
    @Column(name = "change_time", nullable = false)
    private LocalDateTime changeTime;
    
    @Column(name = "remark")
    private String remark;
}