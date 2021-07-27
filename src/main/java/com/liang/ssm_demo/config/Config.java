package com.liang.ssm_demo.config;

import com.liang.mybatis.spring.SpringSqlSessionFactoryBean;
import com.liang.spring.core.annotation.Bean;
import com.liang.spring.core.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public SpringSqlSessionFactoryBean springSqlSessionFactoryBean(){

        SpringSqlSessionFactoryBean springSqlSessionFactoryBean = new SpringSqlSessionFactoryBean();

        return springSqlSessionFactoryBean;
    }

}
