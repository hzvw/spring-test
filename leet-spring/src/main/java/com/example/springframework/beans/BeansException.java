package com.example.springframework.beans;

/**
 * ClassName: BeansException
 * Package: com.example.springframework.beans
 * Description:
 *
 * @Author Harizon
 * @Create 2025/5/10 11:17
 * @Version 1.0
 */
public class BeansException extends Exception {
    public BeansException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeansException(String message) {
        super(message);
    }

    public BeansException() {
    }
}
