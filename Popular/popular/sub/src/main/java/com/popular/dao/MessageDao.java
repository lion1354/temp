package com.popular.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.popular.exception.DBException;
import com.popular.model.Message;

@Repository
public interface MessageDao {
	List<Message> getMessageBySendIdAndReceiveId(Map<String, Object> map)
			throws DBException;

	Message getMessageById(Integer id) throws DBException;

	int add(Message message) throws DBException;

	int deleteBySendIdAndReceiveId(Map<String, Object> map) throws DBException;

	int deleteById(Integer id) throws DBException;
	
	int deleteByDate(Date date) throws DBException;
}
