package com.popular.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.popular.exception.DBException;
import com.popular.model.Country;

@Repository
public interface CountryDao {
	List<Country> getCountry() throws DBException;
}
