package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

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
public class Main80 {
    public static void main(String[] args) {
        SpringApplication.run(Main80.class, args);
    }

}