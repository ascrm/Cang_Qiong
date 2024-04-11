package com.sky.aspect;

/*
 * @Author:ascrm
 * @Date:2024/3/22
 */

import com.sky.annotation.AutoFill;
import com.sky.enumeration.OperationType;
import com.sky.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static com.sky.constant.AutoFillConstant.*;

/**
 * 自定义切面
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoPointCut(){
    }

    /**
     * 前置通知
     */
    @Before("autoPointCut()")
    public void autoFill(JoinPoint joinPoint){

        //获取数据库操作类型
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();//获取方法签名对象
        log.info("打印joinPoint：{}",joinPoint);
        log.info("打印signature：{}",signature);
        log.info("打印Method类：{}",signature.getMethod());

        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解对象
        log.info("打印autoFill：{}",autoFill);

        OperationType value = autoFill.value();//获取数据库操作类型
        log.info("打印OperationType：{}",value);

        //获取实体类对象
        Object[] args = joinPoint.getArgs(); //参数列表
        log.info("打印参数列表：{}",args);

        if(args==null||args.length==0){
            return;
        }
        Object entity=args[0];

        //获取要增强数据
        LocalDateTime now = LocalDateTime.now();
        Long userId = Long.parseLong(UserHolder.getUser());

        //判断数据库操作是哪一种
        if(value==OperationType.INSERT){
            try {

                Method setCreateTime = entity.getClass().getDeclaredMethod(SET_CREATE_TIME, String.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(SET_UPDATE_TIME, String.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(SET_UPDATE_USER, Long.class);

                log.info("entity的类型是：{}",entity.getClass());
                log.info("打印setCreateTime：{}",setCreateTime);

                //通过反射给对象赋值
                setCreateTime.invoke(entity,now);
                setUpdateTime.invoke(entity,now);
                setCreateUser.invoke(entity,userId);
                setUpdateUser.invoke(entity,userId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if(value==OperationType.UPDATE){
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(SET_UPDATE_TIME, String.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,userId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        log.info("切面表达式执行成功....");
    }
}
