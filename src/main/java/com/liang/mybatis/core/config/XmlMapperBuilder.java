package com.liang.mybatis.core.config;

import com.liang.mybatis.core.constant.OpType;
import com.liang.mybatis.core.pojo.MybatisConfiguration;
import com.liang.mybatis.core.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XmlMapperBuilder {

    private MybatisConfiguration configuration;

    public XmlMapperBuilder(MybatisConfiguration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {

        Document document = new SAXReader().read(inputStream);

        Element rootElement = document.getRootElement();

        String namespace = rootElement.attributeValue("namespace");


        //解析select标签
        List<Element> selectNodes = rootElement.selectNodes("//select");

        doParseElement(namespace, selectNodes);

        //解析insert标签
        List<Element> insertNodes = rootElement.selectNodes("//insert");

        doParseElement(namespace, insertNodes);

        //解析update标签

        List<Element> updateNodes = rootElement.selectNodes("//update");

        doParseElement(namespace, updateNodes);


        //解析delete标签

        List<Element> deleteNodes = rootElement.selectNodes("//delete");

        doParseElement(namespace, deleteNodes);

    }

    /**
     * 开始解析xml
     * @param namespace
     * @param list
     */
    private void doParseElement(String namespace, List<Element> list) {
        for (Element element : list) {

            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");

            String parameterType = element.attributeValue("parameterType");

            String sql = element.getTextTrim();

            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setResultType(resultType);
            mappedStatement.setSql(sql);

            switch (element.getName()){
                case "select":
                    mappedStatement.setOpType(OpType.SELECT);
                    break;
                case "update":
                    mappedStatement.setOpType(OpType.UPDATE);
                    break;
                case "insert":
                    mappedStatement.setOpType(OpType.INSERT);
                    break;
                case "delete":
                    mappedStatement.setOpType(OpType.DELETE);
                    break;

                default:

                    throw new RuntimeException("未知数据库操作类型，请检查");
            }

            configuration.getMappedStatementMap().put(namespace+"."+id,mappedStatement);
        }
    }


}
