package com.liang.spring.webmvc.pojo;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class Handler {

    private Object controller;

    private Method method;

    private Pattern pattern; // url正则匹配

    private Map<String,Integer> paramIndexMapping; // 参数顺序，是为了进行参数绑定


    public Handler(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
        paramIndexMapping =  new ConcurrentHashMap<>();
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Map<String, Integer> getParamIndexMapping() {
        return paramIndexMapping;
    }

    public void setParamIndexMapping(Map<String, Integer> paramIndexMapping) {
        this.paramIndexMapping = paramIndexMapping;
    }
}
