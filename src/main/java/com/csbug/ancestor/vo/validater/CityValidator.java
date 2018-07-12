package com.csbug.ancestor.vo.validater;

import com.csbug.ancestor.constant.BindValidator;
import com.csbug.ancestor.vo.query.CityQuery;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Author : chenbin
 * Date : 2018/3/19
 * Description :
 * version : 1.0
 */
@BindValidator(target = {CityQuery.class})
public class CityValidator extends CommonValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return CityQuery.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
    }
}
