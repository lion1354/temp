package com.popular.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.popular.exception.DBException;
import com.popular.model.Friend;

/**
 * 
 * @author Aidan
 *
 * @Date 2016年7月28日
 */
@Repository
public interface FriendDao {
	Friend getFriendById(int id) throws DBException;

	Friend getFriendByOwnerAndFriend(Friend friend) throws DBException;
	
	List<Friend> getFriendByOwnerId(int OwnerId) throws DBException;

	int add(Friend friend) throws DBException;

	int deleteById(int id) throws DBException;

	int deleteByOwnerAndFriend(Friend friend) throws DBException;
}
