package com.popular.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.popular.exception.DBException;
import com.popular.model.Photo;

@Repository
public interface PhotoDao {
	List<Photo> getPhotoByUserId(Integer userId) throws DBException;

	Photo getPhotoById(Integer id) throws DBException;

	int countByUserId(Integer userId) throws DBException;

	int add(Photo photo) throws DBException;

	int deleteByUserId(Integer userId) throws DBException;

	int deleteByIds(List<Integer> ids) throws DBException;
}
