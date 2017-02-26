package com.popular.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.popular.dao.ClientUserDao;
import com.popular.dao.FlowerDao;
import com.popular.exception.DBException;
import com.popular.model.ClientUser;
import com.popular.model.Flower;
import com.popular.service.FlowerService;

/**
 * 
 * @author Aidan
 *
 * @Date 2016年7月27日
 */
@Service
public class FlowerServiceImpl implements FlowerService {
	private final static Logger log = LoggerFactory.getLogger(FlowerServiceImpl.class);
	@Autowired
	private FlowerDao flowerDao;
	
	@Autowired
	private ClientUserDao userDao;

	@Override
	public Flower getFlowerBySenderAndRecipient(Flower flower) throws DBException {
		return flowerDao.getFlowerBySenderAndRecipient(flower);
	}

	@Override
	public Flower getFlowerById(int id) throws DBException {
		return flowerDao.getFlowerById(id);
	}

	@Override
	public int add(Flower flower) throws DBException {
		return flowerDao.add(flower);
	}

	@Override
	public int update(Flower flower) throws DBException {
		return flowerDao.update(flower);
	}

	@Override
	public void send(Flower info) throws DBException {
		add(info);
		ClientUser user = userDao.getClientUserById(info.getRecipient());
		user.setFlowerCount(user.getFlowerCount() + 1);
		userDao.update(user);
	}

}
