package com.csbug.ancestor.exception;

/**
 * Author ： Martin
 * Date : 17/12/2
 * Description : 校验异常
 * Version : 1.0
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
