package com.csbug.ancestor.exception;

import com.csbug.ancestor.constant.BindValidator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Author : chenbin
 * Date : 2018/4/26
 * Description :
 * version : 1.0
 */
@Component
public class MyWebBindingInitializer implements WebBindingInitializer, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initBinder(WebDataBinder binder, WebRequest request) {
        System.out.println("============应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器");
        Collection<Object> beans = applicationContext.getBeansWithAnnotation(BindValidator.class).values();
        Collection<Validator> validators = beans.stream().map(o -> (Validator) o).collect(Collectors.toList());
        binder.addValidators(validators.toArray(new Validator[validators.size()]));
    }
}
