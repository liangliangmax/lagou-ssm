package com.liang.spring.webmvc.servlet;

import com.liang.mybatis.core.io.Resources;
import com.liang.spring.core.context.AnnotationApplicationContext;
import com.liang.spring.core.context.ApplicationContext;
import com.liang.spring.webmvc.annotation.Controller;
import com.liang.spring.webmvc.annotation.RequestMapping;
import com.liang.spring.webmvc.pojo.Handler;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DispatcherServlet extends HttpServlet {

    private List<Handler> handlerMapping = new ArrayList<>();

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
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getRequestURI()+"-->"+req.getMethod());
        if(req.getRequestURI().equals("/favicon.ico")) return;

        //根据uri获取handler
        Handler handler = getHandler(req);

        if(handler == null){
            resp.getWriter().write("404 not found");
        }

        //参数绑定
        //获取所有的参数类型数组，这个数组的长度就是我们最后要传入的args的长度
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();

        //根据长度新建数组
        Object[] paraValues = new Object[parameterTypes.length];

        //向参数中注入值，而且保证顺序
        Map<String, String[]> parameterMap = req.getParameterMap();

        //遍历所有参数
        for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
            String value = StringUtils.join(param.getValue(), ",");

            //如果参数和方法中的参数匹配，就填充数据
            if(!handler.getParamIndexMapping().containsKey(param.getKey())) continue;

            //找到索引位
            Integer index = handler.getParamIndexMapping().get(param.getKey());

            paraValues[index] = value; //把前台传递的参数放到对应未知

        }

        int requestIndex = handler.getParamIndexMapping().get(HttpServletRequest.class.getSimpleName());
        paraValues[requestIndex] = req;

        int responseIndex = handler.getParamIndexMapping().get(HttpServletResponse.class.getSimpleName());
        paraValues[responseIndex] = resp;

        try {
            handler.getMethod().invoke(handler.getController(),paraValues);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }


///////////////////////////////////////////////////////////////////////////////////////



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
                if(!declaredMethod.isAnnotationPresent(RequestMapping.class)){
                    continue;
                }

                RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
                String subUrl = annotation.value();
                String fullUrl = baseUrl + subUrl;

                //把method所有信息封装为handler
                Handler handler = new Handler(stringObjectEntry.getValue(),declaredMethod, Pattern.compile(fullUrl));

                //计算方法参数位置
                Parameter[] parameters = declaredMethod.getParameters();
                for (int i = 0; i < parameters.length; i++) {

                    Parameter parameter = parameters[i];

                    if(parameter.getType() == HttpServletRequest.class || parameter.getType() == HttpServletResponse.class){
                        //如果是request和response对象，那么参数名称写httpServletRequest和httpServletResponse
                        handler.getParamIndexMapping().put(parameter.getType().getSimpleName(),i);
                    }else {
                        handler.getParamIndexMapping().put(parameter.getName(),i);
                    }
                }

                handlerMapping.add(handler);
            }

        }

    }

    private Handler getHandler(HttpServletRequest request) {
        if(handlerMapping.isEmpty()) return null;

        String requestURI = request.getRequestURI();
        for (Handler handler : handlerMapping) {
            Matcher matcher = handler.getPattern().matcher(requestURI);

            if(!matcher.matches()){
                continue;
            }
            return handler;
        }
        return null;
    }

    ////////////////////////////////////////////////////////////

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

//            IAccountService accountService = (IAccountService)annotationApplicationContext.getBean(IAccountService.class);
//
//
//            accountService.queryAll();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
