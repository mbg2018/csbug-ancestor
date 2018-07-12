package com.csbug.ancestor.util;

import com.csbug.ancestor.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author ： Martin
 * Date : 17/10/28
 * Description : 参数校验器工具类
 * Version : 1.0
 */
public class ValidatorUtil {

    private static final int LONG_ID_MIN_LEN = 1;
    private static final int LONG_ID_MAX_LEN = 18;

    private ValidatorUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取验证结果中的错误提示信息
     *
     * @param result : 验证结果
     * @return String : 提示信息字符串
     */
    public static String buildErrorMessage(BindingResult result) {
        StringBuilder message = new StringBuilder();
        List<ObjectError> list = result.getAllErrors();
        if (!CollectionUtils.isEmpty(list)) {
            for (ObjectError elem : list) {
                String defaultMessage = elem.getDefaultMessage();
                if (StringUtils.isNotEmpty(defaultMessage)) {
                    message.append(defaultMessage).append(StringUtils.SPACE);
                }
            }
        }
        return message.toString().trim();
    }

    /**
     * 判断字符串是否符合uuid格式
     *
     * @param str
     * @return true : 符合格式 / false : 不符合
     */
    public static boolean isUUIDWithoutLine(String str) {
        Pattern pattern = Pattern.compile("^[0-9A-F]{8}[0-9A-F]{4}[0-9A-F]{4}[0-9A-F]{4}[0-9A-F]{12}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isNotUUIDWithoutLine(String str) {
        return !isUUIDWithoutLine(str);
    }

    /**
     * 校验字符串长度
     *
     * @param str : 需要校验的字符串
     * @param min : 最小长度
     * @param max : 最大长度
     * @return true : 有效 / false : 无效
     */
    public static boolean validStrLen(String str, int min, int max) {
        return str != null && str.length() >= min && str.length() <= max;
    }

    /**
     * 判断字符串是否为一个long型id
     * @param str : 需要判断的字符串
     * @return true : 是long id / false : 不是
     */
    public static boolean isLongId(String str) {
        return validStrLen(str, LONG_ID_MIN_LEN, LONG_ID_MAX_LEN) && NumberUtils.isDigits(str);
    }

    public static boolean isNotLongId(String str) {
        return !isLongId(str);
    }

    /**
     * 校验Integer数值大小
     *
     * @param number : 需要校验的数值
     * @param min    : 最小值
     * @param max    : 最大值
     * @return true : 有效 / false : 无效
     */
    public static boolean validIntegerSize(Integer number, int min, int max) {
        return number != null && number >= min && number <= max;
    }

    /**
     * 只将第一个错误抛出异常
     *
     * @param errResult : 校验结果
     * @return ValidationException : 校验异常
     */
    public static ValidationException build(BindingResult errResult) {
        FieldError fieldError = errResult.getFieldError();
        String code = fieldError.getCode();
        String field = fieldError.getField();
        String message = fieldError.getDefaultMessage();
        message = "`" + field + "` " + message;
        return new ValidationException(message);
    }
}
