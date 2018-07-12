package com.csbug.ancestor.controller;

import com.csbug.ancestor.entity.City;
import com.csbug.ancestor.service.CityService;
import com.csbug.ancestor.util.ResultBuilder;
import com.csbug.ancestor.vo.group.CityGroup;
import com.csbug.ancestor.vo.query.CityQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * Created by bysocket on 07/02/2017.
 */
@RestController
@RequestMapping("/api")
public class CityRestController {

    private CityService cityService;

    @GetMapping(value = "/api/city")
    public Object findOneCity(@Validated (value = {CityGroup.CityName.class}) CityQuery cityQuery) {
        City city = cityService.findCityByName(cityQuery.getCityName());
        return ResultBuilder.ok(city);
    }

    @GetMapping(value = "/api/cityId")
    public Object findCitId(@NotNull @RequestParam(value = "id") Integer id) {
        City city = cityService.findCityId(id);
        return ResultBuilder.ok(city);
    }

    @Autowired
    public CityRestController(CityService cityService) {
        this.cityService = cityService;
    }
}
