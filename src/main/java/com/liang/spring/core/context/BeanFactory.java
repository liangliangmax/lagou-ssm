package com.liang.spring.core.context;

import java.util.Map;

public interface BeanFactory {

    Object getBean(String beanName);

    Object getBean(Class<?> beanType);

    Object getBean(String beanName, Class<?> beanType);

    Map<String,Object> getBeans();

    boolean containsBean(String beanName);
}
