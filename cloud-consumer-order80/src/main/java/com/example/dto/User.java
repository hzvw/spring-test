package com.example.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 64)
    private String username;

    @Column(name = "phone", unique = true, length = 20)
    private String phone;

    @Column(name = "email", unique = true, length = 128)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;  // 密码字段映射到password_hash列

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(nullable = false)
    private Integer status = 1;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    // 实现UserDetails接口方法
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 根据实际权限修改
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 根据需求实现
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 根据需求实现
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 根据需求实现
    }

    @Override
    public boolean isEnabled() {
        return status == 1; // 根据status字段判断账户状态
    }
}