package com.liang.mybatis.core.sqlSession;

import com.liang.mybatis.core.pojo.MybatisConfiguration;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private MybatisConfiguration configuration;

    public DefaultSqlSessionFactory(MybatisConfiguration configuration) {
        this.configuration = configuration;
    }


    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
