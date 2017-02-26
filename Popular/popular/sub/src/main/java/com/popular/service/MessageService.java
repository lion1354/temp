package com.popular.service;

import java.util.Date;
import java.util.List;

import com.popular.exception.DBException;
import com.popular.model.Message;

public interface MessageService {

	List<Message> getMessageBySendIdAndReceiveId(Integer sendId,
			Integer receiveId) throws DBException;

	Message getMessageById(Integer id) throws DBException;

	int add(Message message) throws DBException;

	int deleteBySendIdAndReceiveId(Integer sendId, Integer receiveId)
			throws DBException;

	int deleteById(Integer id) throws DBException;
	
	int deleteByDate(Date date) throws DBException;

}
