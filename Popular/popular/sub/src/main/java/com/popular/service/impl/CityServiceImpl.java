package com.popular.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.popular.dao.CityDao;
import com.popular.exception.DBException;
import com.popular.model.City;
import com.popular.service.CityService;

@Service
public class CityServiceImpl implements CityService {
	private final static Logger log = LoggerFactory
			.getLogger(CityServiceImpl.class);
	@Autowired
	private CityDao cityDao;

	@Override
	public List<City> getCity() throws DBException {
		return cityDao.getCity();
	}

	@Override
	public List<String> getCityInitial() throws DBException {
		return cityDao.getCityInitial();
	}

	@Override
	public List<City> getCityByProvince() throws DBException {
		return cityDao.getCityByProvince();
	}

	@Override
	public List<Map<String, String[]>> getCityGroup() throws DBException {
		List<Map<String, String[]>> cityGroupList = new ArrayList<Map<String, String[]>>();
		Map<String, String[]> cityMap = new LinkedHashMap<String, String[]>();
		List<City> cityList = getCity();
		List<String> cityInitialList = getCityInitial();

		int i = 0;
		for (String cityInitial : cityInitialList) {
			List<String> list = new ArrayList<String>();
			for (; i < cityList.size(); i++) {
				City city = cityList.get(i);
				if (city.getInitial().equals(cityInitial)) {
					list.add(city.getName());
				} else {
					break;
				}
			}
			cityMap.put(cityInitial, list.toArray(new String[] {}));
			cityGroupList.add(cityMap);
		}
		return cityGroupList;
	}
}
