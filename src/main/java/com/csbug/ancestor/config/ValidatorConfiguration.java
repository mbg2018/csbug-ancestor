package com.csbug.ancestor.config;

import com.csbug.ancestor.constant.BindValidator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author : chenbin
 * Date : 2018/4/27
 * Description : 获取系统所有标柱的校验器-ValidatorCollection
 * version : 1.0
 */
@Configuration
@Slf4j
public class ValidatorConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ValidatorMap validatorCollection() {
        log.info("init validator collection");
        HashMap<String, Validator> validatorHashMap = new HashMap<>();
        Collection<Object> beans = applicationContext.getBeansWithAnnotation(BindValidator.class).values();
        List<Validator> validators = beans.stream()
                .filter(o -> o instanceof Validator)
                .map(o -> (Validator) o)
                .collect(Collectors.toList());
        validators.forEach(validator -> {
            BindValidator bindValidator = validator.getClass().getAnnotation(BindValidator.class);
            if (bindValidator == null) {
                return;
            }
            Class<?>[] targets = bindValidator.target();
            for (Class<?> target : targets) {
                validatorHashMap.put(target.getSimpleName(), validator);
            }
        });

        ValidatorMap validatorMap = new ValidatorMap();
        validatorMap.setValidatorMap(validatorHashMap);
        return validatorMap;
    }

    @Data
    public class ValidatorMap{
        private HashMap<String, Validator> validatorMap;
    }
}
