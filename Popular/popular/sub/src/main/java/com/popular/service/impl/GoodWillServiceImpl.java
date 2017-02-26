package com.popular.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.popular.dao.FriendDao;
import com.popular.dao.GoodWillDao;
import com.popular.exception.DBException;
import com.popular.model.Friend;
import com.popular.model.GoodWill;
import com.popular.service.GoodWillService;

/**
 * 
 * @author Aidan
 *
 * @Date 2016年7月28日
 */
@Service
public class GoodWillServiceImpl implements GoodWillService {

	@Autowired
	private GoodWillDao dao;

	@Autowired
	private FriendDao friendDao;

	@Override
	public GoodWill getGoodWillById(int id) throws DBException {
		return dao.getGoodWillById(id);
	}

	@Override
	public GoodWill getGoodWillByOwnerAndTarget(GoodWill goodWill) throws DBException {
		return dao.getGoodWillByOwnerAndTarget(goodWill);
	}

	@Override
	public int add(GoodWill goodWill) throws DBException {
		return dao.add(goodWill);
	}

	@Override
	public int deleteById(int id) throws DBException {
		return dao.deleteById(id);
	}

	@Override
	public int deleteByOwnerAndTarget(GoodWill goodWill) throws DBException {
		return dao.deleteByOwnerAndTarget(goodWill);
	}

	@Override
	public void showGood(GoodWill goodWill) throws DBException {
		dao.deleteByOwnerAndTarget(goodWill);
		friendDao.add(new Friend(goodWill.getTarget(), goodWill.getOwner(), new Date()));
		friendDao.add(new Friend(goodWill.getOwner(), goodWill.getTarget(), new Date()));
	}
}
