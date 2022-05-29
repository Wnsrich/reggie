package com.wnsrich.reggie.common;

/**
 * 基于LocalThread的工具类，用于获取和保存当前用户ID
 * 作用于线程中，每一个线程有额外的储存空间
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setThreadId(Long id){
        threadLocal.set(id);
    }

    public static Object getThreadId(){
        return threadLocal.get();
    }
}
