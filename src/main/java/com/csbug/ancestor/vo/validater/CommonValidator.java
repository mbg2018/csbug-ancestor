package com.csbug.ancestor.vo.validater;

import com.csbug.ancestor.constant.ValidateConstant;
import com.csbug.ancestor.vo.query.PageQuery;
import org.springframework.validation.Errors;

/**
 * Author : chenbin
 * Date : 2018/3/19
 * Description :
 * version : 1.0
 */
public class CommonValidator{

    /**
     *  验证分页参数
     */
    protected void validPageQuery(PageQuery query, Errors errors) {
        if (query.getStart() == null) {
            query.setStart(ValidateConstant.MIN_PAGE_INDEX);
        }

        if (query.getPageSize() == null) {
            query.setPageSize(ValidateConstant.DEFAULT_PAGE_SIZE);
        }

        if (query.getPageSize() < ValidateConstant.MIN_PAGE_SIZE || query.getPageSize() > ValidateConstant.MAX_PAGE_SIZE) {
            errors.rejectValue(null, null, "每页大小必须在0-100之间");
        }
    }
}
