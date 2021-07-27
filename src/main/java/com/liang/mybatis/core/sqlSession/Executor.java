package com.liang.mybatis.core.sqlSession;

import com.liang.mybatis.core.pojo.Configuration;
import com.liang.mybatis.core.pojo.MappedStatement;

import java.util.List;

public interface Executor {

    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    public int insert(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    public int update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    public int delete(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

}
