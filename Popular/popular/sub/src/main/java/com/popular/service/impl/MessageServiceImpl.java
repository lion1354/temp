package com.popular.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.popular.dao.MessageDao;
import com.popular.exception.DBException;
import com.popular.model.Message;
import com.popular.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {
	private final static Logger log = LoggerFactory
			.getLogger(MessageServiceImpl.class);

	@Autowired
	private MessageDao messageDao;

	@Override
	public List<Message> getMessageBySendIdAndReceiveId(Integer sendId,
			Integer receiveId) throws DBException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sendId", sendId);
		map.put("receiveId", receiveId);
		return messageDao.getMessageBySendIdAndReceiveId(map);
	}

	@Override
	public Message getMessageById(Integer id) throws DBException {
		return messageDao.getMessageById(id);
	}

	@Override
	public int add(Message message) throws DBException {
		return messageDao.add(message);
	}

	@Override
	public int deleteBySendIdAndReceiveId(Integer sendId, Integer receiveId)
			throws DBException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sendId", sendId);
		map.put("receiveId", receiveId);
		return messageDao.deleteBySendIdAndReceiveId(map);
	}

	@Override
	public int deleteById(Integer id) throws DBException {
		return messageDao.deleteById(id);
	}

	@Override
	public int deleteByDate(Date date) throws DBException {
		return messageDao.deleteByDate(date);
	}

}
