package com.example.springframework.bean;

import com.example.springframework.beans.BeanDefinition;
import com.example.springframework.beans.DefaultListableBeanFactory;
import org.junit.Test;

/**
 * ClassName: ApiTest
 * Package: com.example.springframework.test.bean
 * Description:
 *
 * @Author Harizon
 * @Create 2025/5/9 16:30
 * @Version 1.0
 */
public class ApiTest {


    @Test
    public void test_BeanFactory() throws Exception{
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2.注册 bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);
        // 3.第一次获取 bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
        // 4.第二次获取 bean from Singleton
        UserService userService_singleton = (UserService) beanFactory.getBean("userService");
        userService_singleton.queryUserInfo();
    }







}
