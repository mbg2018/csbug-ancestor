package com.csbug.ancestor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Author : chenbin
 * Date : 2018/3/19
 * Description :
 * version : 1.0
 */
@Getter
public class BaseException extends RuntimeException {

    private final int reasonCode;

    public BaseException(ErrorCodeEnum error) {
        super(error.getReasonPhrase());
        this.reasonCode = error.getReasonCode();
    }

    public BaseException(ErrorCodeEnum error, String reason) {
        super(error.getReasonPhrase() + ":" + reason);
        this.reasonCode = error.getReasonCode();
    }
}
