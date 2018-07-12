package com.csbug.ancestor.util;


import com.csbug.ancestor.exception.BaseException;
import com.csbug.ancestor.exception.ErrorCodeEnum;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.BindingResult;

import java.util.Map;

/**
 * Author ： Martin
 * Date : 17/9/22
 * Description : 返回结果工具类
 * Version : 0.1
 */
public class ResultBuilder {

    private static final String CODE = "statusCode";
    private static final String DATA = "data";
    private static final String MESSAGE = "message";
    private static final String JSON_OBJECT_EMPTY = "{}";

    private int code;
    private String message;
    private Map<String, Object> dataMap =  Maps.newHashMap();

    public static ResultBuilder create() {
        return new ResultBuilder();
    }

    public ResultBuilder code(int code) {
        this.code = code;
        return this;
    }

    public ResultBuilder ok() {
        this.code = ErrorCodeEnum.SUCCESS.getReasonCode();
        this.message = ErrorCodeEnum.SUCCESS.getReasonPhrase();
        dataMap.put(DATA, JSON_OBJECT_EMPTY);
        return this;
    }

    public static Map ok(Object data) {
        return ImmutableMap.of(
                CODE, ErrorCodeEnum.SUCCESS.getReasonCode(),
                MESSAGE, ErrorCodeEnum.SUCCESS.getReasonPhrase(),
                DATA, ObjectUtils.defaultIfNull(data, JSON_OBJECT_EMPTY));
    }

    public ResultBuilder error(ErrorCodeEnum error) {
        this.code = error.getReasonCode();
        this.message = error.getReasonPhrase();
        return this;
    }

    public ResultBuilder error(ErrorCodeEnum error, Object content) {
        this.code = error.getReasonCode();
        this.message = error.getReasonPhrase();
        dataMap.put(DATA, content);
        return this;
    }

    public ResultBuilder message(String message) {
        this.message = message;
        return this;
    }

    public ResultBuilder data(String field, Object content) {
        dataMap.put(field, content);
        return this;
    }

    public ResultBuilder data(Object content) {
        this.code = ErrorCodeEnum.SUCCESS.getReasonCode();
        this.message = ErrorCodeEnum.SUCCESS.getReasonPhrase();
        dataMap.put(DATA, content);
        return this;
    }

    public Map build() {
        Map<String, Object> map = Maps.newHashMap();
        map.put(CODE, code);
        map.putAll(dataMap);
        if (message != null) {
            map.put(MESSAGE, message);
        }
        return map;
    }

    public static Map requestNotValid(BindingResult result) {
        return create().error(ErrorCodeEnum.REQUEST_PARAMS_NOT_VALID)
                .message(ValidatorUtil.buildErrorMessage(result))
                .build();
    }

    public static Map requestThrowException(BaseException ex) {
        return create().code(ex.getReasonCode()).message(ex.getMessage()).build();
    }
}

