package com.example.controller;

import com.example.JwtUtil;
import com.example.dto.AuthRequest;
import com.example.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JwtUtil jwtUtil;
    @Autowired
    private final UserDetailsService userDetailsService; // 这是Spring Security的接口，你需要实现它
    @Autowired
    private final PasswordEncoder passwordEncoder; // 用于注册时加密密码
    // 假设有一个UserService或UserRepository用于用户操作
    @Autowired
    private final UserDetailsService userService;

    // 用户登录[1,4](@ref)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            // 执行认证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
            // 生成JWT
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(jwt));
        } catch (Exception e) {
            throw new RuntimeException("用户名或密码错误", e); // 认证失败
        }
    }

    // 用户注册（简单示例）[4](@ref)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest authRequest) {
        // 检查用户名是否已存在
        // if (userService.existsByUsername(authRequest.getUsername())) {
        //     return ResponseEntity.badRequest().body("用户名已存在");
        // }

        // 创建新用户实体
        // User newUser = new User();
        // newUser.setUsername(authRequest.getUsername());
        // 使用PasswordEncoder加密密码[8](@ref)
        // newUser.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        // newUser.setEmail(authRequest.getEmail()); // 如果有其他字段
        // 设置角色等...
        // userService.save(newUser);

        // 通常注册成功后，可以选择直接登录返回token，或者返回成功信息让用户去登录
        // 这里简单返回成功消息
        return ResponseEntity.ok("用户注册成功");
    }

}

// 登录请求DTO


