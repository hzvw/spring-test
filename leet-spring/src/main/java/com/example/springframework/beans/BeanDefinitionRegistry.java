package com.example.springframework.beans;

/**
 * ClassName: BeanDefinitionRegistry
 * Package: com.example.springframework.beans
 * Description:
 *
 * @Author Harizon
 * @Create 2025/5/10 11:24
 * @Version 1.0
 */
public interface BeanDefinitionRegistry {

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
