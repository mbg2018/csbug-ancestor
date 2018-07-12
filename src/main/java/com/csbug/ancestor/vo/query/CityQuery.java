package com.csbug.ancestor.vo.query;

import com.csbug.ancestor.vo.group.CityGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Author : chenbin
 * Date : 2018/4/26
 * Description :
 * version : 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityQuery {

    @NotNull(message = "内容id不能为空", groups = {CityGroup.CityName.class})
    @Length(min = 1, max = 12, message = "长度不能超过12", groups = {CityGroup.CityName.class})
    private String cityName;
}
