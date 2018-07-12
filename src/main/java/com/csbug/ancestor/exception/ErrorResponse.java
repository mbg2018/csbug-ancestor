package com.csbug.ancestor.exception;

import lombok.Getter;
import lombok.Setter;


/**
 * Author ： Martin
 * Date : 2017/9/22
 * Description : 返回错误的定义
 * Version : 1.0
 */
@Setter
@Getter
public final class ErrorResponse {

    public final int code;
    public final String message;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
