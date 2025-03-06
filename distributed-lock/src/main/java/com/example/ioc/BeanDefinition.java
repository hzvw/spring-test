package com.example.ioc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeanDefinition {

    private String beanName;

    private Class beanClass;
     //省略getter、setter
 }