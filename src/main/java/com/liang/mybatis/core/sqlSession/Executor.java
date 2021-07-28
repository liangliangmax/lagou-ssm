package com.liang.mybatis.core.sqlSession;

import com.liang.mybatis.core.pojo.MybatisConfiguration;
import com.liang.mybatis.core.pojo.MappedStatement;

import java.util.List;

public interface Executor {

    public <E> List<E> query(MybatisConfiguration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    public int insert(MybatisConfiguration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    public int update(MybatisConfiguration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    public int delete(MybatisConfiguration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

}
