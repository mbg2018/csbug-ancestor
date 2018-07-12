package com.csbug.ancestor.service.impl;

import com.csbug.ancestor.dao.CityDao;
import com.csbug.ancestor.entity.City;
import com.csbug.ancestor.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 城市业务逻辑实现类
 *
 * Created by bysocket on 07/02/2017.
 */
@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDao cityDao;

    @Override
    public City findCityByName(String cityName) {
        return cityDao.findByName(cityName);
    }

    @Override
    public City findCityId(Integer id) {
        return cityDao.findId(id);
    }

}
