package com.liang.spring.webmvc.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target({ElementType.TYPE})
public @interface Controller {
    String value() default "";
}
