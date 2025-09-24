package com.example.dto;

import lombok.Data;

// ApiResponse.java
@Data
public class ApiResponse<T> {
    private boolean success;
    private String code;
    private String message;
    private T data;
    
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setCode("SUCCESS");
        response.setData(data);
        return response;
    }
    
    public static ApiResponse<?> error(String code, String message) {
        ApiResponse<?> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}