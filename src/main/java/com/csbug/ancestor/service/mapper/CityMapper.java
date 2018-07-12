package com.csbug.ancestor.service.mapper;

import com.csbug.ancestor.entity.City;
import com.csbug.ancestor.vo.query.CityVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author : chenbin
 * Date : 2018/6/19
 * Description :
 * version : 1.0
 */
@Mapper
public interface CityMapper {
    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    CityVo toCityVo(City city, Map<String, String> test);

    ArrayList<CityVo> toCityVos(List<City> citys, Map<String, String> test);
}
