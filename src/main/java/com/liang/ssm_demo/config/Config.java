package com.liang.ssm_demo.config;

import com.liang.mybatis.core.io.Resources;
import com.liang.mybatis.core.sqlSession.SqlSessionFactory;
import com.liang.mybatis.core.sqlSession.SqlSessionFactoryBuilder;
import com.liang.spring.core.annotation.Bean;
import com.liang.spring.core.annotation.Configuration;
import com.liang.spring.core.annotation.Value;

import java.io.InputStream;

@Configuration
public class Config {

    @Value("${mybatis.mapper.mapperScanPackage}")
    private String mapperScanPackage;


    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {

        InputStream resourceAsStream = Resources.getResourceAsStream(mapperScanPackage);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        return sqlSessionFactory;

    }



}
