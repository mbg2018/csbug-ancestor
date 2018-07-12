package com.csbug.ancestor.config.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.validation.Validation;
import com.alibaba.dubbo.validation.Validator;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;


/**
 * Author ： Martin
 * Date : 2017/9/22
 * Description : dubbo服务参数校验拦截器
 * Version : 1.0
 */
@Slf4j
@Activate(group = {Constants.CONSUMER, Constants.PROVIDER}, value = Constants.VALIDATION_KEY, before = {"validation"})
public class MisDubboValidationFilter implements Filter {

    private Validation validation;

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String methodName = invocation.getMethodName();
        String parameters = invoker.getUrl().getMethodParameter(methodName, Constants.VALIDATION_KEY);
        if (validation != null && !methodName.startsWith("$") && ConfigUtils.isNotEmpty(parameters)) {
            Result message = validateMethodParameter(invoker, invocation);
            if (message != null) return message;
        }
        return invoker.invoke(invocation);
    }
    
    private Result validateMethodParameter(Invoker<?> invoker, Invocation invocation) {
        try {
            Validator validator = validation.getValidator(invoker.getUrl());
            if (validator != null) {
                validator.validate(invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments());
            }
        } catch (ConstraintViolationException ex) {
            return handleValidateException(ex);
        } catch (Exception ex) {
           return handleUnknownException(ex);
        }
        return null;
    }
    
    private Result handleValidateException(ConstraintViolationException ex) {
        if (log.isWarnEnabled()) {
            log.warn("dubbo参数校验失败:{}", ex.toString());
        }
        if (ex.getConstraintViolations() != null && !ex.getConstraintViolations().isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("dubbo参数校验失败-->");
            ex.getConstraintViolations()
                    .forEach(v -> message.append(v.getPropertyPath())
                            .append(":")
                            .append(v.getMessage()).append(","));
            return new RpcResult(new RuntimeException(message.toString()));
        }
        return null;
    }
    
    private Result handleUnknownException(Exception ex) {
        if (log.isWarnEnabled()) {
            log.warn("dubbo参数校验失异常:{}", ex.getMessage());
        }
        return new RpcResult(ex);
    }
}
