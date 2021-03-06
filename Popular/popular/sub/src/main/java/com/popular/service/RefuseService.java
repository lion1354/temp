package com.popular.service;

import java.util.List;

import com.popular.exception.DBException;
import com.popular.model.Refuse;

/**
 * 
 * @author Aidan
 *
 * @Date 2016年7月28日
 */
public interface RefuseService {

	Refuse getRefuseById(int id) throws DBException;

	Refuse getRefuseByOwnerAndTarget(Refuse refuse) throws DBException;

	int add(Refuse refuse) throws DBException;

	int updateByOwnerAndTarget(Refuse refuse) throws DBException;

	int deleteById(int id) throws DBException;

	int deleteByOwnerAndTarget(Refuse refuse) throws DBException;

	List<Refuse> getRefuseByOwner(int ownerId) throws DBException;
}
