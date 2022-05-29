package com.wnsrich.reggie.common;

/**
 * 业务异常类 RuntimeException
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
