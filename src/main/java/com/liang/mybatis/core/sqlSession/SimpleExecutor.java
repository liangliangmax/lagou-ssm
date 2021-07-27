package com.liang.mybatis.core.sqlSession;

import com.liang.mybatis.core.config.BoundSql;
import com.liang.mybatis.core.constant.OpType;
import com.liang.mybatis.core.pojo.Configuration;
import com.liang.mybatis.core.pojo.MappedStatement;
import com.liang.mybatis.core.utils.GenericTokenParser;
import com.liang.mybatis.core.utils.ParameterMapping;
import com.liang.mybatis.core.utils.ParameterMappingTokenHandler;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {


    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        //返回集合
        ArrayList<E> objects = new ArrayList<>();

        //执行sql
        ResultSet resultSet = (ResultSet) doExecute(configuration, mappedStatement, params, OpType.SELECT);

        //开始处理结果集，如果指定了返回类型，则说明需要结果集，如果没指定返回类型，说明不想要返回值，直接返回个空集合即可
        String resultType = mappedStatement.getResultType();
        if(StringUtils.isNotBlank(resultType)){
            //封装结果
            while (resultSet.next()){
                Class<E> resultTypeClass = getClassType(resultType);
                E resultEntity = resultTypeClass.newInstance();

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount ; i++) {
                    String columnName = metaData.getColumnName(i);

                    Object value = resultSet.getObject(columnName);

                    //使用反射，进行属性赋值
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);

                    Method writeMethod = propertyDescriptor.getWriteMethod();

                    writeMethod.invoke(resultEntity,value);

                }

                objects.add(resultEntity);

            }

        }

        return objects;
    }

    @Override
    public int insert(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {

        return (int) doExecute(configuration, mappedStatement, params, OpType.INSERT);
    }

    @Override
    public int update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        return (int) doExecute(configuration, mappedStatement, params, OpType.UPDATE);
    }

    @Override
    public int delete(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        return (int) doExecute(configuration, mappedStatement, params, OpType.DELETE);
    }

    /**
     * 真正开始执行sql的地方
     * 如果有参数，则开始处理参数占位，如果没有参数，直接执行即可
     *
     * 增删改都没有返回值，查询是有返回值的
     *
     * @param configuration
     * @param mappedStatement
     * @param params
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private Object doExecute(Configuration configuration, MappedStatement mappedStatement, Object[] params, OpType opType) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Connection connection = configuration.getDataSource().getConnection();

        String sql = mappedStatement.getSql();

        BoundSql boundSql = getBoundSql(sql);

        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        //如果指定了参数，则认为有参数，才开始设置参数，如果没有参数，则直接运行sql语句
        String parameterType = mappedStatement.getParameterType();
        if(StringUtils.isNotBlank(parameterType)){
            Class<?> parameterTypeClass = getClassType(parameterType);
            List<ParameterMapping> parameterMappingsList = boundSql.getParameterMappingsList();
            for (int i = 0; i < parameterMappingsList.size(); i++) {
                ParameterMapping parameterMapping = parameterMappingsList.get(i);
                String content = parameterMapping.getContent();

                //使用反射，对想赋值
                Field declaredField = parameterTypeClass.getDeclaredField(content);
                declaredField.setAccessible(true);
                Object o = declaredField.get(params[0]);

                preparedStatement.setObject(i+1,o);
            }

        }

        //执行sql
        if(opType == OpType.SELECT){
            return preparedStatement.executeQuery();
        }

        return preparedStatement.executeUpdate();
    }



    private <E> Class<E> getClassType(String parameterType) throws ClassNotFoundException {

        if(parameterType!=null){

            Class clazz = Class.forName(parameterType);

            return clazz;
        }
        return null;
    }

    /**
     * 完成对占位符的解析工作
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();

        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);

        //解析出来的sql
        String parseSql = genericTokenParser.parse(sql);

        //#{}里面解析出来的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();


        BoundSql boundSql = new BoundSql(parseSql,parameterMappings);

        return boundSql;


    }
}
