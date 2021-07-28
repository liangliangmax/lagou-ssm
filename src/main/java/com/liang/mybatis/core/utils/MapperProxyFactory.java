package com.liang.mybatis.core.utils;

import com.liang.mybatis.core.sqlSession.SqlSessionFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MapperProxyFactory {

    private SqlSessionFactory sqlSessionFactory;

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * 使用cglib动态代理生成代理对象
     * @param obj 委托对象
     * @return
     */
    public Object getCglibProxy(Class obj) {
        return  Enhancer.create(obj, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                Object result = null;

                Object mapper = sqlSessionFactory.openSession().getMapper(obj);

                result = method.invoke(mapper,objects);

                return result;
            }
        });
    }
}
