package com.popular.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.popular.dao.CountryDao;
import com.popular.exception.DBException;
import com.popular.model.Country;
import com.popular.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService {
	private final static Logger LOG = LoggerFactory
			.getLogger(CountryServiceImpl.class);
	@Autowired
	private CountryDao countryDao;

	@Override
	public List<Country> getCountry() throws DBException {
		return countryDao.getCountry();
	}
}
