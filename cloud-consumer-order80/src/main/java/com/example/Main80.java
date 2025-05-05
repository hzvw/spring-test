package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * ClassName: ${NAME}
 * Package: com.example
 * Description:
 *
 * @Author Harizon
 * @Create ${DATE} ${TIME}
 * @Version 1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients// 开启feign功能
@MapperScan("com.example.mapper")
public class Main80 {
    public static void main(String[] args) {
        SpringApplication.run(Main80.class, args);
    }

}