package com.sky.annotation;

import com.sky.enumeration.OperationType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author:ascrm
 * @Date:2024/3/22
 */

/**
 * 自定义注解，用于公共字段自动填充
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {

    //指定数据库操作类型
    OperationType value();
}
