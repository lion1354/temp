package com.popular.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.popular.dao.ClientUserDao;
import com.popular.dao.FlowerDao;
import com.popular.dao.FriendDao;
import com.popular.exception.DBException;
import com.popular.model.ClientUser;
import com.popular.model.Flower;
import com.popular.model.Friend;
import com.popular.service.FriendService;

import net.sf.json.JSONObject;

/**
 * 
 * @author Aidan
 *
 * @Date 2016年7月28日
 */
@Service
public class FriendServiceImpl implements FriendService {

	@Autowired
	private FriendDao dao;
	
	@Autowired
	private FlowerDao flowerDao;
	
	@Autowired
	private ClientUserDao userDao;

	@Override
	public Friend getFriendById(int id) throws DBException {
		return dao.getFriendById(id);
	}

	@Override
	public Friend getFriendByOwnerAndFriend(Friend friend) throws DBException {
		return dao.getFriendByOwnerAndFriend(friend);
	}

	@Override
	public List<Friend> getFriendByOwnerId(int OwnerId) throws DBException {
		return dao.getFriendByOwnerId(OwnerId);
	}

	@Override
	public int add(Friend friend) throws DBException {
		return dao.add(friend);
	}

	@Override
	public int deleteById(int id) throws DBException {
		return dao.deleteById(id);
	}

	@Override
	public int deleteByOwnerAndFriend(Friend friend) throws DBException {
		return dao.deleteByOwnerAndFriend(friend);
	}

	@Override
	public void deleteFriend(JSONObject info) throws DBException {
		int ownerId = info.getInt("ownerId");
		int friendId = info.getInt("friendId");
		Flower fo = flowerDao.getFlowerBySenderAndRecipient(new Flower(ownerId,friendId));
		if(fo != null){
			ClientUser user = userDao.getClientUserById(friendId);
			user.setFlowerCount(user.getFlowerCount() - 1);
			userDao.update(user);
		}
		Flower ff = flowerDao.getFlowerBySenderAndRecipient(new Flower(friendId,ownerId));
		if(ff != null){
			ClientUser user = userDao.getClientUserById(ownerId);
			user.setFlowerCount(user.getFlowerCount() - 1);
			userDao.update(user);
		}
		dao.deleteByOwnerAndFriend(new Friend(new ClientUser(ownerId),new ClientUser(friendId)));
		dao.deleteByOwnerAndFriend(new Friend(new ClientUser(friendId),new ClientUser(ownerId)));	
	}
}
