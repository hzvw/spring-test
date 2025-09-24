package com.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    @GetMapping("/hello")
    public String sayHello(Authentication authentication) {
        // Authentication对象包含了当前认证用户的信息[8](@ref)
        String username = authentication.getName();
        return "Hello, " + username + "! 这是一个受保护的接口。";
    }

    @GetMapping("/admin")
    // 可以使用注解进行方法级别的权限控制，需要在SecurityConfig中启用@EnableMethodSecurity
    // @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "这是管理员界面。";
    }
}