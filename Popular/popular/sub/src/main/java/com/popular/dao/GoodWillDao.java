package com.popular.dao;

import org.springframework.stereotype.Repository;

import com.popular.exception.DBException;
import com.popular.model.GoodWill;

/**
 * 
 * @author Aidan
 *
 * @Date 2016年7月28日
 */
@Repository
public interface GoodWillDao {
	GoodWill getGoodWillById(int id) throws DBException;

	GoodWill getGoodWillByOwnerAndTarget(GoodWill goodWill) throws DBException;

	int add(GoodWill goodWill) throws DBException;

	int deleteById(int id) throws DBException;

	int deleteByOwnerAndTarget(GoodWill goodWill) throws DBException;
}
