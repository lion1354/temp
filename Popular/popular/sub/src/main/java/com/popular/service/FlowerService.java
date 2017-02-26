package com.popular.service;

import com.popular.exception.DBException;
import com.popular.model.Flower;

/**
 * 
 * @author Aidan
 *
 * @Date 2016年7月27日
 */
public interface FlowerService {
	Flower getFlowerBySenderAndRecipient(Flower flower) throws DBException;

	Flower getFlowerById(int id) throws DBException;

	int add(Flower flower) throws DBException;

	int update(Flower flower) throws DBException;

	void send(Flower info) throws DBException;
}
