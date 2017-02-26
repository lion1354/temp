package com.popular.service;

import java.util.List;

import com.popular.exception.DBException;
import com.popular.model.Province;

public interface ProvinceService {

	List<Province> getProvince() throws DBException;

	List<Province> getProvinceByCountry() throws DBException;
}
