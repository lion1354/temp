package com.popular.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.popular.dao.ProvinceDao;
import com.popular.exception.DBException;
import com.popular.model.Province;
import com.popular.service.ProvinceService;

@Service
public class ProvinceServiceImpl implements ProvinceService {
	private final static Logger LOG = LoggerFactory
			.getLogger(ProvinceServiceImpl.class);
	@Autowired
	private ProvinceDao provinceDao;

	@Override
	public List<Province> getProvince() throws DBException {
		return provinceDao.getProvince();
	}

	@Override
	public List<Province> getProvinceByCountry() throws DBException {
		return provinceDao.getProvinceByCountry();
	}

}
