package com.popular.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.popular.model.Photo;

public interface PhotoService {

	List<Photo> savePhoto(HttpServletRequest request, List<Photo> photoList)
			throws Exception;

	void deletePhotoByUserId(HttpServletRequest request, Integer userId)
			throws Exception;

	void deletePhotoByIds(HttpServletRequest request, List<Integer> ids)
			throws Exception;

	List<Photo> getPhotoByUserId(HttpServletRequest request, Integer userId)
			throws Exception;

	Photo getPhotoById(HttpServletRequest request, Integer id) throws Exception;

}
