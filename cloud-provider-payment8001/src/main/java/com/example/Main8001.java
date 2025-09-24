package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@SpringBootApplication //创建主程序
@MapperScan("com.example.mapper")
public class Main8001 {
    public static void main(String[] args) {
        SpringApplication.run(Main8001.class, args);
    }
}