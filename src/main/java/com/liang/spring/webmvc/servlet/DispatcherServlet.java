package com.liang.spring.webmvc.servlet;

import com.liang.spring.core.context.AnnotationApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");

        doLoadConfig(contextConfigLocation);

        initContext(contextConfigLocation);

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }


    void doLoadConfig(String contextConfigLocation){



    }


    //初始化容器
    private void initContext(String contextConfigLocation) {

        new AnnotationApplicationContext(contextConfigLocation);

    }
}
