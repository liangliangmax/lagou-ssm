package com.liang.mybatis.core.sqlSession;

import com.liang.mybatis.core.constant.OpType;
import com.liang.mybatis.core.pojo.MybatisConfiguration;
import com.liang.mybatis.core.pojo.MappedStatement;

import java.lang.reflect.*;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private MybatisConfiguration configuration;

    public DefaultSqlSession(MybatisConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        //完成simpleExecutor对jdbc的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);

        List<E> query = simpleExecutor.query(configuration, mappedStatement, params);

        return query;
    }

    @Override
    public <E> E selectOne(String statementId, Object... params) throws Exception {

        List<Object> objects = selectList(statementId, params);
        if(objects.size() == 1){

            return (E) objects.get(0);
        }

        throw new RuntimeException("查询结果为空或结果过多");
    }

    @Override
    public int insert(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);

        return simpleExecutor.insert(configuration,mappedStatement,params);
    }

    @Override
    public int update(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);

        return simpleExecutor.update(configuration,mappedStatement,params);
    }

    @Override
    public int delete(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);

        return simpleExecutor.delete(configuration,mappedStatement,params);
    }


    //获取代理对象，通过操作类型，判断调用的方法
    @Override
    public <T> T getMapper(Class<?> mapperClass) {

        Object proxyInstance =  Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String className = mapperClass.getName();
                String statementId = className + "."+method.getName();

                MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);

                Enum<OpType> opType = mappedStatement.getOpType();

                //判断操作的类型，其实只判断是否是select即可，其他类型的都一样，但是这里分了四种，方便以后扩展
                if (opType == OpType.SELECT){
                    Type genericReturnType = method.getGenericReturnType();

                    if(genericReturnType instanceof ParameterizedType){
                        List<Object> objects = selectList(statementId, args);

                        return objects;
                    }
                    return selectOne(statementId,args);
                }else if(opType == OpType.INSERT){
                    return insert(statementId,args);
                }else if(opType == OpType.UPDATE){
                    return update(statementId,args);
                }else if(opType == OpType.DELETE){
                    return delete(statementId,args);
                }else {
                    throw new RuntimeException("未知操作类型");
                }

            }
        });

        return (T) proxyInstance;
    }
}
