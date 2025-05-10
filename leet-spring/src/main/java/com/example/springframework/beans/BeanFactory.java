package com.example.springframework.beans;

/**
 * ClassName: BeanFactory
 * Package: com.example.springframework
 * Description:
 *
 * @Author Harizon
 * @Create 2025/5/9 16:47
 * @Version 1.0
 */
public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
}
