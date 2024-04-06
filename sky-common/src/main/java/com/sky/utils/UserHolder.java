package com.sky.utils;


import com.sky.entity.Employee;

/**
 * @Author:ascrm
 * @Date:2024/3/17
 */
public class UserHolder {

    public static ThreadLocal<String> threadLocal=new ThreadLocal<>();

    public static void saveUser(String employeeId){
        threadLocal.set(employeeId);
    }

    public static String getUser(){
        return threadLocal.get();
    }

    public static void removeUser(){
        threadLocal.remove();
    }
}
