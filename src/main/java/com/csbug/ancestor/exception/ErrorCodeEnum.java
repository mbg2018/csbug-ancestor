package com.csbug.ancestor.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * Author ： Martin
 * Date : 17/9/22
 * Description : 状态码
 * Version : 1.0
 *
 * 200成功
 * 401失败
 * 3XXXX    受检与未受检异常
 * 40000    controller层异常
 * 50000    service层异常
 * 60000    dao层异常
 */

@AllArgsConstructor
@Getter
public enum ErrorCodeEnum{

    SUCCESS(200, "Success"),  //成功
    ERROR(401,"Error"), //失败
    REQUEST_PARAMS_NOT_VALID(40000, "Request Params Illegal"),//请求参数非法
    INTERNAL_SERVER_ERROR(50000, "Server Internal Error"), //服务器内部错误
    DATA_ACCESS_ERROR(60000, "Database Operate Fail"); //数据访问异常

    private int reasonCode;
    private String reasonPhrase;
}
