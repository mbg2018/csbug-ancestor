package com.csbug.ancestor.vo.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author ： Martin
 * Date : 18/1/19
 * Description : 分页请求参数
 * Version : 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQuery {
    private Integer start;
    private Integer pageSize;
}
