package com.example;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}") // 从配置文件中读取密钥，生产环境务必设置一个复杂且保密的密钥
    private String secret;

    @Value("${jwt.expiration.ms:3600000}") // 默认过期时间1小时 (毫秒)
    private long jwtExpirationMs;

    // 生成密钥[10](@ref)
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 从Token中提取用户名[9](@ref)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 从Token中提取过期时间[9](@ref)
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 从Token中提取特定声明[9](@ref)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 解析Token，获取所有声明[9](@ref)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 检查Token是否过期[9](@ref)
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 为用户生成Token[9,10](@ref)
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // 可以在这里添加自定义声明，如角色、权限等
        return createToken(claims, userDetails.getUsername());
    }

    // 创建Token[9,10](@ref)
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // 主题，通常放用户名
                .setIssuedAt(new Date(System.currentTimeMillis())) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // 过期时间
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 签名算法和密钥
                .compact();
    }

    // 验证Token是否有效（用户名匹配且未过期）[9](@ref)
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}