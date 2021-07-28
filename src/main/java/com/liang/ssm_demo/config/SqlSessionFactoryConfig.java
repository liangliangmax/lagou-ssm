package com.liang.ssm_demo.config;

import com.liang.mybatis.core.io.Resources;
import com.liang.mybatis.core.pojo.MybatisConfiguration;
import com.liang.mybatis.core.sqlSession.DefaultSqlSessionFactory;
import com.liang.mybatis.core.sqlSession.SqlSessionFactory;
import com.liang.mybatis.core.sqlSession.SqlSessionFactoryBuilder;
import com.liang.spring.core.annotation.Autowired;
import com.liang.spring.core.annotation.Bean;
import com.liang.spring.core.annotation.Configuration;
import com.liang.spring.core.annotation.Value;

import javax.sql.DataSource;
import java.io.InputStream;

@Configuration
public class SqlSessionFactoryConfig {

    @Value("${mybatis.mapper.configPath}")
    private String mapperScanPackage;

    @Autowired
    private DataSource dataSource;


    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {

        if(mapperScanPackage.startsWith("classpath:")){
            mapperScanPackage = mapperScanPackage.replace("classpath:","");
        }

        InputStream resourceAsStream = Resources.getResourceAsStream(mapperScanPackage);

        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

        sqlSessionFactoryBuilder.setDataSource(dataSource);

        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(resourceAsStream);

        return sqlSessionFactory;

    }



}
