package com.example.springframework.beans;

/**
 * ClassName: BeanFactory
 * Package: com.example.springframework
 * Description:
 *
 * @Author Harizon
 * @Create 2025/5/9 16:25
 * @Version 1.0
 */
public interface SingletonBeanRegistry {
    Object getSingleton(String beanName);

}
