package com.liang.mybatis.core.config;

import com.liang.mybatis.core.io.Resources;
import com.liang.mybatis.core.pojo.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * 对配置文件进行解析
 *
 * 分为dataSource解析和mapper文件解析
 *
 */
public class XMLConfigBuilder {

    private Configuration configuration;


    public XMLConfigBuilder() {
        this.configuration = new Configuration();
    }

    /**
     * 该方法就是将配置文件进行解析，封装configuration
     * @param inputStream
     * @return
     */
    public Configuration parseConfig(InputStream inputStream) throws DocumentException, PropertyVetoException {


        Document document = new SAXReader().read(inputStream);

        Element rootElement = document.getRootElement();

        List<Element> list = rootElement.selectNodes("//property");

        Properties properties = new Properties();
        for (Element element : list) {

            String name = element.attributeValue("name");
            String value = element.attributeValue("value");

            properties.setProperty(name,value);
        }

        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("url"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));

        configuration.setDataSource(comboPooledDataSource);


        // 开始解析各种mapper.xml
        List<Element> mapperList = rootElement.selectNodes("//mapper");

        for (Element element : mapperList) {
            String mapperPath = element.attributeValue("resource");

            InputStream resourceAsStream = Resources.getResourceAsStream(mapperPath);

            //传入configuration，将解析好的mapper直接放入其中
            XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(configuration);
            xmlMapperBuilder.parse(resourceAsStream);

        }


        return configuration;
    }



}
