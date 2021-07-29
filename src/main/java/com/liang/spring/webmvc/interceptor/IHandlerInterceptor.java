package com.liang.spring.webmvc.interceptor;

import com.liang.spring.webmvc.pojo.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * controller的拦截器
 */
public interface IHandlerInterceptor {

    boolean needFilter(Handler handler);

    boolean preHandle(Handler handler,HttpServletRequest request, HttpServletResponse response);

}
