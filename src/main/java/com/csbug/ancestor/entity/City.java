package com.csbug.ancestor.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 城市实体类
 *
 * Created by bysocket on 07/02/2017.
 */

public class City {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 省份编号
     */
    private String provinceId;

    /**
     * 城市编号
     */
    private String cityId;

    /**
     * 城市名称
     */
    private String cityName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
