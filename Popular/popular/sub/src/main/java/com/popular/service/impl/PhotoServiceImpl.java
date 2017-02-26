package com.popular.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.popular.common.AppConfig;
import com.popular.dao.PhotoDao;
import com.popular.model.Photo;
import com.popular.service.FileService;
import com.popular.service.PhotoService;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class PhotoServiceImpl implements PhotoService {
	private final static Logger log = LoggerFactory
			.getLogger(PhotoServiceImpl.class);

	@Autowired
	private PhotoDao photoDao;

	@Autowired
	private FileService fileService;

	@Autowired
	private AppConfig appConfig;

	@Override
	public List<Photo> savePhoto(HttpServletRequest request,
			List<Photo> photoList) throws Exception {
		if (photoList != null && photoList.size() > 0) {
			for (int i = 0; i < photoList.size(); i++) {
				Photo photo = photoList.get(i);
				String originalPath = fileService.getFilePathFromUrl(request,
						photo.getOriginalUrl());
				String thumbnailPath = createThumbnail(new File(originalPath));
				photo.setThumbnailUrl(
						fileService.getFileUrlFromPath(thumbnailPath));
				photo.setCreateTime(new Date());
				photoDao.add(photo);
			}
		}
		return photoList;
	}

	@Override
	public void deletePhotoByUserId(HttpServletRequest request, Integer userId)
			throws Exception {
		List<Photo> photoList = photoDao.getPhotoByUserId(userId);
		if (photoList != null && photoList.size() > 0) {
			for (int i = 0; i < photoList.size(); i++) {
				Photo photo = photoList.get(i);
				deletePhoto(request, photo);
			}
			photoDao.deleteByUserId(userId);
		}
	}

	@Override
	public void deletePhotoByIds(HttpServletRequest request, List<Integer> ids)
			throws Exception {
		if (ids != null && ids.size() > 0) {
			for (int i = 0; i < ids.size(); i++) {
				Integer id = ids.get(i);
				Photo photo = photoDao.getPhotoById(id);
				deletePhoto(request, photo);
			}
			photoDao.deleteByIds(ids);
		}
	}

	@Override
	public List<Photo> getPhotoByUserId(HttpServletRequest request,
			Integer userId) throws Exception {
		List<Photo> list = photoDao.getPhotoByUserId(userId);
		return list;
	}

	@Override
	public Photo getPhotoById(HttpServletRequest request, Integer id)
			throws Exception {
		Photo photo = photoDao.getPhotoById(id);
		return photo;
	}

	private String createThumbnail(File file) throws IOException {
		File thumbnail = new File(file.getParentFile(),
				"thumbnail" + "_" + file.getName());
		Thumbnails.of(file)
				.size(Integer.parseInt(appConfig.getThumbnail_width()),
						Integer.parseInt(appConfig.getThumbnail_height()))
				.toFile(thumbnail);
		return thumbnail.getPath();
	}

	private void deletePhoto(HttpServletRequest request, Photo photo) {
		String originalPath = fileService.getFilePathFromUrl(request,
				photo.getOriginalUrl());
		String thumbnailPath = fileService.getFilePathFromUrl(request,
				photo.getThumbnailUrl());
		File original = new File(originalPath);
		if (original.exists()) {
			original.delete();
		}

		File thumbnail = new File(thumbnailPath);
		if (thumbnail.exists()) {
			thumbnail.delete();
		}
	}
}
