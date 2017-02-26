package com.popular.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.popular.exception.DBException;
import com.popular.model.City;

@Repository
public interface CityDao {
	List<City> getCity() throws DBException;

	List<String> getCityInitial() throws DBException;

	List<City> getCityByProvince() throws DBException;
}
