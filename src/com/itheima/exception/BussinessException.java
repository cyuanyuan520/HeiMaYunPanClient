package com.itheima.exception;

public class BussinessException extends RuntimeException{
    //空参构造
    public BussinessException() {
    }
    //有参构造
    public BussinessException(String message) {
        super(message);
    }
}
