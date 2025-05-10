package com.example.springframework.beans;

/**
 * ClassName: AbstractBeanFactory
 * Package: com.example.springframework
 * Description:
 *
 * @Author Harizon
 * @Create 2025/5/9 16:53
 * @Version 1.0
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    @Override
    public Object getBean(String beanName) throws BeansException{
        Object bean = getSingleton(beanName);
        if(bean != null){
            return bean;
        }

        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        return createBean(beanName, beanDefinition);
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException ;


}
