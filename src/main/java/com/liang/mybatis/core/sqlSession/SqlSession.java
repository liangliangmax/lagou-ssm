package com.liang.mybatis.core.sqlSession;

import java.util.List;

public interface SqlSession {

    //查询所有
    public <E> List<E> selectList(String statementId, Object... params) throws Exception;

    //查询单个
    public <E> E selectOne(String statementId, Object... params) throws Exception;

    //新增
    public int insert(String statementId, Object... params) throws Exception;

    //修改
    public int update(String statementId, Object... params) throws Exception;

    //删除
    public int delete(String statementId, Object... params) throws Exception;



    public <T> T getMapper(Class<?> mapperClass) ;

}
