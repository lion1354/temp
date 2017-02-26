package com.popular.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.popular.common.AppConfig;
import com.popular.dao.PhotoDao;
import com.popular.exception.DBException;
import com.popular.model.FileType;
import com.popular.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	private static Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

	@Autowired
	private AppConfig config;

	@Autowired
	private PhotoDao photoDao;

	@Override
	public List<String> saveFile(HttpServletRequest request, String userId,
			FileType fileType) throws Exception {
		if (fileType == FileType.photo) {
			int totalPhoto = Integer.parseInt(config.getTotal_photo());
			int count = photoDao.countByUserId(Integer.parseInt(userId));
			if (count > totalPhoto) {
				throw new DBException("只能上传" + totalPhoto + "张照片");
			}
		}

		List<String> fileList = new ArrayList<String>();
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			String savePath = getParentPath(request) + File.separator;
			savePath += config.getFileDirectory() + File.separator;
			savePath += userId + File.separator + fileType.name()
					+ File.separator;
			log.debug("savePath : {}", savePath);
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					String myFileName = file.getOriginalFilename();
					if (myFileName != null && myFileName.trim() != "") {
						File localFile = null;
						if (fileType == FileType.photo) {
							localFile = new File(savePath,
									System.currentTimeMillis() + "_"
											+ myFileName);
						} else {
							localFile = new File(savePath, myFileName);
						}
						File fileDir = new File(savePath);
						if (!fileDir.exists()) {
							fileDir.mkdirs();
						}
						file.transferTo(localFile);
						String path = localFile.getAbsolutePath().substring(
								localFile.getAbsolutePath()
										.indexOf(config.getFileDirectory()),
								localFile.getAbsolutePath().length());
						path = config.getFileServer() + File.separator + path;
						path.replaceAll("\\\\", "/");
						fileList.add(path);
					}
				}
			}
		}
		return fileList;
	}

	@Override
	public String getFilePathFromUrl(HttpServletRequest request, String url) {
		String filePath = url.substring(url.indexOf(config.getFileDirectory()));
		filePath = getParentPath(request) + File.separator + filePath;
		return filePath;

	}

	@Override
	public String getFileUrlFromPath(String path) {
		String fileUrl = path
				.substring(path.indexOf(config.getFileDirectory()));
		fileUrl = config.getFileServer() + File.separator + fileUrl;
		fileUrl = fileUrl.replaceAll("\\\\", "/");
		return fileUrl;
	}

	private String getRootPath(HttpServletRequest request) {
		return request.getServletContext().getRealPath("/");
	}

	private String getParentPath(HttpServletRequest request) {
		String rootPath = getRootPath(request);
		rootPath = rootPath.substring(0, rootPath.length() - 1);
		rootPath = rootPath.substring(0,
				rootPath.lastIndexOf(File.separatorChar) + 1);
		return rootPath;
	}

	@Override
	public void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}
}
