package com.csbug.ancestor.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Author : chenbin
 * Date : 2018/3/15
 * Description :
 * version : 1.0
 */
@Component
@Aspect
public class SimpleValidate {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final ExecutableValidator validator = factory.getValidator().forExecutables();

//    @Around("execution(* com.csbug.ancestor.controller.*.*(..))")
    public Object validate(ProceedingJoinPoint jpj) throws Throwable {
        Object target = jpj.getThis();
        Object[] args = jpj.getArgs();
        Method method = ((MethodSignature) jpj.getSignature()).getMethod();
        // 执行校验，获得校验结果
        Set<ConstraintViolation<Object>> validResult = validMethodParams(target, method, args);
        StringBuffer errorMessage = new StringBuffer();
        validResult.forEach(result -> errorMessage.append(result.getMessage()).append(" "));
        if(!StringUtils.isEmpty(errorMessage.toString())){
            throw new ValidationException(errorMessage.toString());
        }
        return jpj.proceed();
    }

    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object[] params) {
        return validator.validateParameters(obj, method, params);
    }
}
