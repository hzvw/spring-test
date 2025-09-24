package com.example.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    // private String email; // 注册时可能需要
    // getters and setters
}