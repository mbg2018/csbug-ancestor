package com.csbug.ancestor.constant;


/**
 * Author ： Martin
 * Date : 18/1/18
 * Description : 基础枚举类型,可用于mybatis
 * Version : 2.0
 */
public interface BaseEnum {

    /**
     * 获取枚举索引
     * @return Integer : 索引值
     */
    Integer getIndex();

    /**
     * 获取枚举值
     * @param index : 索引
     * @param className : 枚举类
     * @param T : 类型
     * @return T 返回类中的枚举类型对象
     */
    static <T extends BaseEnum> T getEnumOfBaseEnum(Integer index, Class<T> className) {
        if (className == null || index == null) {
            return null;
        }

        Object result = null;
        Object[] enumConstants = className.getEnumConstants();
        for (Object obj : enumConstants) {
            BaseEnum enumConstant = (BaseEnum) obj;
            if (enumConstant.getIndex().equals(index)) {
                result = enumConstant;
            }
        }
        return (T) result;
    }
}
