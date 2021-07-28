package com.liang.spring.webmvc.servlet;

import com.liang.mybatis.core.io.Resources;
import com.liang.spring.core.context.AnnotationApplicationContext;
import com.liang.spring.core.context.ApplicationContext;
import com.liang.spring.webmvc.annotation.Controller;
import com.liang.spring.webmvc.annotation.RequestMapping;
import com.liang.ssm_demo.service.IAccountService;
import com.liang.ssm_demo.util.IocUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {

        //加载文件
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");

        //初始化容器
        initContext(contextConfigLocation);

        //构造一个HandlerMapping处理器映射器，将配置好的url和Method建立映射关系
        initHandlerMapping();

    }




    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }


    private void initHandlerMapping() {

        AnnotationApplicationContext applicationContext = (AnnotationApplicationContext) IocUtil.getApplicationContext();


        for (Map.Entry<String, Object> stringObjectEntry : applicationContext.getBeans().entrySet()) {

            Class<?> aClass = stringObjectEntry.getValue().getClass();

            if(!aClass.isAnnotationPresent(Controller.class)){
                continue;
            }


            String baseUrl = "";

            if(aClass.isAnnotationPresent(RequestMapping.class)){

                RequestMapping annotation = aClass.getAnnotation(RequestMapping.class);

                baseUrl = annotation.value();

            }

            Method[] declaredMethods = aClass.getDeclaredMethods();

            for (Method declaredMethod : declaredMethods) {


                if(declaredMethod.isAnnotationPresent(RequestMapping.class)){

                    RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);

                    String subUrl = annotation.value();

                    String fullUrl = baseUrl + subUrl;

                }

            }


        }




    }


    //初始化容器
    private void initContext(String contextConfigLocation) {

        try {
            if(StringUtils.isNotBlank(contextConfigLocation)){

                if(contextConfigLocation.startsWith("classpath:")){
                    contextConfigLocation = contextConfigLocation.replaceAll("classpath:","");
                }

            }

            InputStream resourceAsStream = Resources.getResourceAsStream(contextConfigLocation);


            Properties properties = new Properties();

            properties.load(resourceAsStream);

            AnnotationApplicationContext annotationApplicationContext = new AnnotationApplicationContext(properties.getProperty("scanPackage"));

            IAccountService accountService = (IAccountService)annotationApplicationContext.getBean(IAccountService.class);


            accountService.queryAll();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
