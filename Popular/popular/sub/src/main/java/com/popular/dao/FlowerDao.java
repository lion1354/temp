package com.popular.dao;

import org.springframework.stereotype.Repository;

import com.popular.exception.DBException;
import com.popular.model.Flower;

/**
 * 
 * @author Aidan
 *
 * 2016.7.27
 */
@Repository
public interface FlowerDao {
	
	Flower getFlowerBySenderAndRecipient(Flower flower) throws DBException;
	
	Flower getFlowerById(int id) throws DBException;
	
	int add(Flower flower) throws DBException;

	int update(Flower flower) throws DBException;
}
