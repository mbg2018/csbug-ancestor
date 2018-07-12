package com.csbug.ancestor.exception;


import com.csbug.ancestor.config.ValidatorConfiguration;
import com.csbug.ancestor.util.ResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ValidationException;
import java.sql.SQLException;
import java.util.Map;


/**
 * Author ： Martin
 * Date : 17/2/17
 * Description : 全局异常处理
 * Version : 2.0
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private ValidatorConfiguration.ValidatorMap validatorMap;

    @Autowired
    public GlobalExceptionHandler(ValidatorConfiguration.ValidatorMap validatorMap) {
        this.validatorMap = validatorMap;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        if (binder == null || binder.getTarget() == null) {
            return;
        }
        String target = binder.getTarget().getClass().getSimpleName();
        if (validatorMap.getValidatorMap().containsKey(target)) {
            binder.addValidators(validatorMap.getValidatorMap().get(target));
        }
    }

    private void logException(Exception ex, WebRequest request) {
        log.error("exception class ==> " + ex.getClass().getName());
        log.error("exception message ==> " + ex.getLocalizedMessage());
        if (request != null) {
            log.error("handling exception ==> " + request.getDescription(true));
        }
        log.error(ex.toString());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    public Map handleArgumentNtoValidException(MethodArgumentNotValidException ex, WebRequest request) {
        logException(ex, request);
        return ResultBuilder.requestNotValid(ex.getBindingResult());
    }

    @ExceptionHandler(value = {BindException.class})
    @ResponseBody
    public Map handleBindException(BindException ex, WebRequest request) {
        logException(ex, request);
        return ResultBuilder.requestNotValid(ex.getBindingResult());
    }

    @ExceptionHandler(value = {ValidationException.class})
    @ResponseBody
    public Map handleValidateException(ValidationException ex, WebRequest request) {
        logException(ex, request);
        return ResultBuilder.create().error(ErrorCodeEnum.REQUEST_PARAMS_NOT_VALID).message(ex.getMessage()).build();
    }

    /**
     * 处理请求非法的异常
     * controller层
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class})
    public Map handleRequestArgumentException(Exception ex, WebRequest request) {
        logException(ex, request);
        return ResultBuilder.create().error(ErrorCodeEnum.REQUEST_PARAMS_NOT_VALID).message(ex.getMessage()).build();
    }

    /**
     * 处理服务层的异常与未受检异常
     * service层
     */
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public Map errorHandler(Exception ex, WebRequest request) {
        logException(ex, request);
        return ResultBuilder.create().error(ErrorCodeEnum.INTERNAL_SERVER_ERROR).message(ex.getMessage()).build();
    }

    /**
     * 处理底层数据访问的异常
     * dao层
     */
    @ExceptionHandler(value = {SQLException.class, MyBatisSystemException.class, DataAccessException.class})
    public Map handleDataAccessException(Exception ex, WebRequest request) {
        logException(ex, request);
        return ResultBuilder.create().error(ErrorCodeEnum.DATA_ACCESS_ERROR).message(ex.getMessage()).build();
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BaseException.class)
    public Map handleRestException(BaseException ex) {
        logException(ex, null);
        return ResultBuilder.requestThrowException(ex);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
