package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// OrderModificationRequest.java
@Data
public class OrderModificationRequest {
    @NotBlank
    private String fieldName;
    
    @NotBlank
    private String newValue;
    
    private String remark;
    
    // 如果是修改商品数量，需要传递商品信息
    private Long productId;
    private Integer quantity;
}
