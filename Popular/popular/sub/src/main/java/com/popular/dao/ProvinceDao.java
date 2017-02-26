package com.popular.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.popular.exception.DBException;
import com.popular.model.Province;

@Repository
public interface ProvinceDao {
	List<Province> getProvince() throws DBException;

	List<Province> getProvinceByCountry() throws DBException;
}
