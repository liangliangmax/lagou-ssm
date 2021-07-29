package com.liang.spring.webmvc.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Security {


    String[] username() default {};

}
