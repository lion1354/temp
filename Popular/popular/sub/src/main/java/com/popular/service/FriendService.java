package com.popular.service;

import java.util.List;

import com.popular.exception.DBException;
import com.popular.model.Friend;

import net.sf.json.JSONObject;

/**
 * 
 * @author Aidan
 *
 * @Date 2016年7月28日
 */
public interface FriendService {
	Friend getFriendById(int id) throws DBException;

	Friend getFriendByOwnerAndFriend(Friend friend) throws DBException;
	
	List<Friend> getFriendByOwnerId(int OwnerId) throws DBException;

	int add(Friend friend) throws DBException;

	int deleteById(int id) throws DBException;

	int deleteByOwnerAndFriend(Friend friend) throws DBException;

	void deleteFriend(JSONObject info) throws DBException;
}
