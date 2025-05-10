package com.example.springframework.beans;

import com.example.springframework.beans.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: DefaultSingletonBeanFactory
 * Package: com.example.springframework
 * Description:
 *
 * @Author Harizon
 * @Create 2025/5/9 16:43
 * @Version 1.0
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    private Map<String, Object> objectMap = new ConcurrentHashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return objectMap.get(beanName);
    }

    protected void addSingleton(String beanName, Object obj){
        objectMap.put(beanName, obj);
    }
}
