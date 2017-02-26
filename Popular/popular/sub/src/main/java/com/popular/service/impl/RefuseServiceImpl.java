package com.popular.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.popular.dao.RefuseDao;
import com.popular.exception.DBException;
import com.popular.model.Refuse;
import com.popular.service.RefuseService;

/**
 * 
 * @author Aidan
 *
 * @Date 2016年7月28日
 */
@Service
public class RefuseServiceImpl implements RefuseService {

	@Autowired
	private RefuseDao dao;

	@Override
	public Refuse getRefuseById(int id) throws DBException {
		return dao.getRefuseById(id);
	}

	@Override
	public Refuse getRefuseByOwnerAndTarget(Refuse refuse) throws DBException {
		return dao.getRefuseByOwnerAndTarget(refuse);
	}

	@Override
	public int add(Refuse refuse) throws DBException {
		return dao.add(refuse);
	}

	@Override
	public int updateByOwnerAndTarget(Refuse refuse) throws DBException {
		return dao.updateByOwnerAndTarget(refuse);
	}

	@Override
	public int deleteById(int id) throws DBException {
		return dao.deleteById(id);
	}

	@Override
	public int deleteByOwnerAndTarget(Refuse refuse) throws DBException {
		return dao.deleteByOwnerAndTarget(refuse);
	}

	@Override
	public List<Refuse> getRefuseByOwner(int ownerId) throws DBException {
		return dao.getRefuseByOwner(ownerId);
	}
}
