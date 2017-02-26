package com.popular.service;

import java.util.List;
import java.util.Map;

import com.popular.exception.DBException;
import com.popular.model.City;

public interface CityService {

	List<City> getCity() throws DBException;

	List<String> getCityInitial() throws DBException;

	List<City> getCityByProvince() throws DBException;

	List<Map<String, String[]>> getCityGroup() throws DBException;
}
