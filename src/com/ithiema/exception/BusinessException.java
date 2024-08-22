package com.ithiema.exception;
public class BusinessException extends RuntimeException{
    public BusinessException() {
    }
    public BusinessException(String message) {
        super(message);
    }
}
