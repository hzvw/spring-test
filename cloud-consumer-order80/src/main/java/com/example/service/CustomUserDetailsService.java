package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections; // 示例中使用简单的权限，实际应从数据库查询用户的角色/权限

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // 这里需要注入你的UserRepository或UserService
    // private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 从数据库查询用户信息[4](@ref)
        // YourUserEntity user = userRepository.findByUsername(username)
        //        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 2. 假设从数据库查询到的用户信息如下（实际应根据查询结果构建）
        // String dbUsername = user.getUsername();
        // String encryptedPassword = user.getPassword(); // 数据库存储的是加密后的密码
        // Collection<? extends GrantedAuthority> authorities = getAuthorities(user.getRoles());

        // 3. 返回UserDetails对象（这里使用Spring Security提供的User builder进行简单演示）[8](@ref)
        // 实际项目中，你可能会创建自定义的UserDetails实现类。
        // 这是一个硬编码示例，仅用于演示。真实情况应从数据库获取。
        if ("admin".equals(username)) {
            return User.builder()
                    .username("admin")
                    .password("$2a$10$...（这里是使用BCrypt加密后的密码）") // 明文可能是 "admin"
                    .authorities(Collections.emptyList()) // 应根据实际赋予权限
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }

    // 辅助方法：根据用户角色获取权限（GrantedAuthority）
    // private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
    //    return roles.stream()
    //            .map(role -> new SimpleGrantedAuthority(role.getName()))
    //            .collect(Collectors.toList());
    // }
}