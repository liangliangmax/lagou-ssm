package com.liang.test;

import com.liang.mybatis.core.io.Resources;
import com.liang.spring.core.context.AnnotationApplicationContext;
import com.liang.ssm_demo.service.IAccountService;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InitTest {


    @Test
    public void  test() throws IOException {

        InputStream resourceAsStream = Resources.getResourceAsStream("springmvc.properties");

        Properties properties = new Properties();

        properties.load(resourceAsStream);

        AnnotationApplicationContext annotationApplicationContext = new AnnotationApplicationContext(properties.getProperty("com.liang.ssm_demo"));

        IAccountService accountService = (IAccountService)annotationApplicationContext.getBean(IAccountService.class);

        accountService.run();

    }
}
