package com.example.springframework.beans;

/**
 * ClassName: BeanDefinition
 * Package: com.example.springframework
 * Description:
 *
 * @Author Harizon
 * @Create 2025/5/9 16:22
 * @Version 1.0
 */
// object对象存放bean
public class BeanDefinition {
    private Class beanClass;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }
}
