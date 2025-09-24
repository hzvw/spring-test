package com.example;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown") // 当Spring容器关闭时，会调用shutdown方法优雅关闭RedissonClient
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 使用单服务器模式，并设置地址。地址格式为 "redis://host:port"
        config.useSingleServer()
                .setAddress("redis://localhost:6379") // 这里可以根据需要从环境变量或配置文件中读取
                .setDatabase(0); // 可选，选择数据库编号
        return Redisson.create(config);
    }
}