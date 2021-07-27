package com.liang.mybatis.core.sqlSession;

import com.liang.mybatis.core.config.XMLConfigBuilder;
import com.liang.mybatis.core.pojo.Configuration;

import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream inputStream) throws Exception {
        //1.使用dom4j解析配置文件，将解析出来的内容封装到configuration中

        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();

        Configuration configuration = xmlConfigBuilder.parseConfig(inputStream);

        //2,创建sqlSessionFactory
        SqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);

        return defaultSqlSessionFactory;
    }

}
