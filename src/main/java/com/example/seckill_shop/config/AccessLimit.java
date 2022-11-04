package com.example.seckill_shop.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//运行时注解
@Retention(RetentionPolicy.RUNTIME)
//该注解放在方法上
@Target(ElementType.METHOD)
public @interface AccessLimit {

    int second();

    int maxCount();

    boolean needLogin() default true;

}
