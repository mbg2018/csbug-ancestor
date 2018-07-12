package com.csbug.ancestor.vo.query;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


/**
 * Author ： Martin
 * Date : 17/10/30
 * Description : 分页数据显示类
 * Version : 1.0
 */
@Data
public class PageVo<E extends Serializable> implements Serializable {
    private int pageSize;
    private int page;
    private long totalCount;
    private List<E> result;

    public PageVo() {
        this(0, 0, 0, Collections.emptyList());
    }

    public PageVo(int page, int pageSize, long totalCount, List<E> result) {
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.result = result;
        this.page = page;
    }

}
