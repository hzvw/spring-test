package com.example.dto;

import lombok.Data;

// 认证响应DTO
@Data
public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }
    // getter
}