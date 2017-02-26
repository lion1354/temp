package com.popular.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.popular.exception.DBException;
import com.popular.model.Refuse;

/**
 * 
 * @author Aidan
 *
 * @Date 2016年7月28日
 */
@Repository
public interface RefuseDao {
	Refuse getRefuseById(int id) throws DBException;

	Refuse getRefuseByOwnerAndTarget(Refuse refuse) throws DBException;

	int add(Refuse refuse) throws DBException;

	int updateByOwnerAndTarget(Refuse refuse) throws DBException;

	int deleteById(int id) throws DBException;

	int deleteByOwnerAndTarget(Refuse refuse) throws DBException;
	
	List<Refuse> getRefuseByOwner(int ownerId) throws DBException;
}
