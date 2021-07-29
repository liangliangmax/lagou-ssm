package com.liang.ssm_demo.interceptor;

import com.liang.spring.core.annotation.Configuration;
import com.liang.spring.webmvc.annotation.Security;
import com.liang.spring.webmvc.interceptor.IHandlerInterceptor;
import com.liang.spring.webmvc.pojo.Handler;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityInterceptor implements IHandlerInterceptor {

    @Override
    public boolean needFilter(Handler handler) {

        if(handler.getController().getClass().isAnnotationPresent(Security.class)){
            return true;
        }

        if(handler.getMethod().isAnnotationPresent(Security.class)){
            return true;
        }

        return false;
    }

    @Override
    public boolean preHandle(Handler handler, HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进行了权限拦截");

        try {

            String username = request.getParameter("username");
            if(StringUtils.isBlank(username)){

                response.getWriter().write("401 Authentication failed");

                return false;
            }

            if(handler.getController().getClass().isAnnotationPresent(Security.class)){

                Security annotation = handler.getController().getClass().getAnnotation(Security.class);

                String[] usernames = annotation.username();

                for (String name : usernames) {

                    if(name.equalsIgnoreCase(username)){

                        System.out.println("权限验证通过");
                        return true;
                    }
                }

                response.getWriter().write("401 Authentication failed");
                return false;
            }

            if(handler.getMethod().isAnnotationPresent(Security.class)){
                Security annotation = handler.getMethod().getAnnotation(Security.class);

                String[] usernames = annotation.username();

                for (String name : usernames) {

                    if(name.equalsIgnoreCase(username)){

                        System.out.println("权限验证通过");
                        return true;
                    }
                }

                response.getWriter().write("401 Authentication failed");
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;

    }





}
