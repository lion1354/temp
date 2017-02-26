package com.popular.service;

import java.util.List;

import com.popular.exception.DBException;
import com.popular.model.Country;

public interface CountryService {

	List<Country> getCountry() throws DBException;
}
