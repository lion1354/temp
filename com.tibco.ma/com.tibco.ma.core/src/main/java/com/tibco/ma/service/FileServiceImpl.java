package com.tibco.ma.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.tibco.ma.common.Constants;
import com.tibco.ma.common.ImageUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.ValidateUtils;
import com.tibco.ma.controller.CustomMultipartResolver;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.FileDao;
import com.tibco.ma.model.File;
import com.tibco.ma.model.FileGroup;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.Specification;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@Service
public class FileServiceImpl extends BaseServiceImpl<File> implements
		FileService {

	@Autowired
	private FileDao fileDao;

	@Autowired
	private CoreGridFSService coreGridFSService;

	@Autowired
	private SpecificationService specificationService;

	@Autowired
	private FileGroupService fileGroupService;

	@Override
	public BaseDao<File> getDao() {
		return fileDao;
	}

	@Override
	public boolean ValidateGroupId(String groupId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("fileGroup")
				.is(new FileGroup(groupId)));
		List<File> lists = fileDao.find(query, File.class);
		return ValidateUtils.isValidate(lists);
	}

	@Override
	public List<File> queryByGroupId(String groupId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("fileGroup")
				.is(new FileGroup(groupId)));
		List<File> lists = fileDao.find(query, File.class);
		return lists;
	}

	@Override
	public void deleteByGroupId(String groupId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("fileGroup")
				.is(new FileGroup(groupId)));
		List<File> lists = fileDao.find(query, File.class);
		deleteCore(lists);
		fileDao.remove(query, File.class);
	}

	private void deleteCore(List<File> files) {
		for (File file : files) {
			if (StringUtil.notEmpty(file.getFilePath())) {
				Query queryCore = new Query();
				queryCore.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
						new ObjectId(file.getFilePath())));
				coreGridFSService.deleteSFDBFile(queryCore);
			}
		}
	}

	@Override
	public void deleteBySpecification(String specificationId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("specification").is(
				new Specification(specificationId)));
		List<File> files = fileDao.find(query, File.class);
		if (ValidateUtils.isValidate(files)) {
			deleteCore(files);
		}
		fileDao.remove(query, File.class);
	}

	@Override
	public List<File> showByGroupId(String groupId, String contentPath)
			throws Exception {
		FileGroup fileGroup = null;
		if (StringUtil.notEmpty(groupId)) {
			fileGroup = fileGroupService.findById(new ObjectId(groupId),
					FileGroup.class);
		}
		List<File> files = queryByGroupId(groupId);
		if (ValidateUtils.isValidate(files)) {
			for (File file : files) {
				if (StringUtil.notEmpty(file.getFilePath())) {
					if (ImageUtils.checkImage(file.getContentType())) {
						file.setFilePath(contentPath
								+ Constants.IMAGE_SHOW_GRIDFS_PREFIX
								+ file.getFilePath());
					}
				}
				if (file.getSpecification() != null) {
					Query querySpecification = new Query();
					querySpecification.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
							new ObjectId(file.getSpecification().getId())));
					Specification specification = specificationService.findOne(
							querySpecification, Specification.class);
					if (specification != null) {
						file.setSpecification(specification);
					}
				}
				if (file.getFileGroup() != null) {
					file.setFileGroup(fileGroup);
				}

			}
		}
		return files;
	}

	@Override
	public List<File> QueryByIds(String ids, String contentType)
			throws Exception {
		List<File> listFiles = new ArrayList<File>();
		if ("ImageGroup".equals(contentType)) {
			if (ids.contains(",") && StringUtil.notEmpty(ids)) {
				String id[] = ids.split(",");
				for (int i = 0; i < id.length; i++) {
					if (StringUtil.notEmpty(id[i])) {
						Query query = new Query();
						query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
								new ObjectId(id[i])));
						File file = findOne(query, File.class);
						if (file != null) {
							listFiles.add(file);
						}
					}
				}
			} else {
				if (StringUtil.notEmpty(ids)) {
					Query query = new Query();
					query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
							new ObjectId(ids)));
					File file = findOne(query, File.class);
					if (file != null) {
						listFiles.add(file);
					}
				}
			}
		} else {
			if (StringUtil.notEmpty(ids)) {
				Query query = new Query();
				query.addCriteria(Criteria.where("fileGroup").is(
						new FileGroup(ids)));
				File file = findOne(query, File.class);
				if (file != null) {
					listFiles.add(file);
				}
			}
		}
		return listFiles;
	}

	@Override
	public void deleteFileById(String fileId,
			BaseGridFSService coreGridFSService) {
		Query query = new Query();
		query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(fileId)));
		File file = fileDao.findOne(query, File.class);
		if (file != null) {
			if (StringUtil.notEmpty(file.getFilePath())) {
				coreGridFSService.deleteByOid(file.getFilePath());
			}
		}
		fileDao.remove(query, File.class);
	}

	@Override
	public void deleteOldFile(String deleteIds, String type) throws Exception {
		if ("ImageGroup".equals(type)) {
			if (deleteIds.contains(",")) {
				String ids[] = deleteIds.split(",");
				for (int i = 0; i < ids.length; i++) {
					if (StringUtil.notEmpty(ids[i])) {
						deleteFileById(ids[i], coreGridFSService);
					}
				}
			} else {
				if (StringUtil.notEmpty(deleteIds)) {
					deleteFileById(deleteIds, coreGridFSService);
				}
			}
		} else {
			deleteByGroupId(deleteIds);
			fileGroupService.deleteByGroupId(deleteIds, false);
		}
	}

	@Override
	public List<File> uploadFile(HttpServletRequest request,
			BaseGridFSService baseGridFSService) throws Exception {
		String deleteIds = request.getParameter("deleteIds");
		if (StringUtil.notEmpty(deleteIds)) {
			if (deleteIds.contains(",")) {
				String ids[] = deleteIds.split(",");
				for (int i = 0; i < ids.length; i++) {
					if (StringUtil.notEmpty(ids[i])) {
						deleteFileById(ids[i], baseGridFSService);
					}
				}
			} else {
				deleteFileById(deleteIds, baseGridFSService);
			}
		}
		List<File> listFiles = saveFiles(request, null, baseGridFSService);
		return listFiles;
	}

	@Override
	public List<File> saveFiles(HttpServletRequest request, String groupId,
			BaseGridFSService baseGridFSService) throws Exception {
		List<File> fileLists = new ArrayList<File>();
		CommonsMultipartResolver commonsMultipartResolver = new CustomMultipartResolver(
				request.getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					if ("ImageGroup".equals(request.getParameter("uploadType"))) {
						if (!ImageUtils.checkImage(file.getContentType())) {
							throw new RuntimeException("file type error");
						}
					}
					File files = new File();
					String myFileName = file.getOriginalFilename();
					String name = file.getName();
					String contentType = file.getContentType();
					Long size = file.getSize();

					String fileName;
					if ("ImageGroup".equals(request.getParameter("uploadType"))) {
						files.setSpecification(new Specification(name));
						fileName = ImageUtils.createNewFileName(myFileName,
								contentType);
					} else {
						fileName = ImageUtils.createFileName(myFileName);
					}
					files.setName(fileName);
					files.setContentType(contentType);
					if (StringUtil.notEmpty(groupId)) {
						files.setFileGroup(new FileGroup(groupId));
					}
					Map<String, Object> map = baseGridFSService
							.saveGetFileUrlAndMd5(file.getInputStream(),
									fileName, null);
					if (map.containsKey("fileUrl")) {
						files.setFilePath(map.get("fileUrl").toString());
					}
					if (map.containsKey("md5")) {
						files.setMd5(map.get("md5").toString());
					}
					files.setSize(size);
					fileDao.save(files);
					fileLists.add(files);
				}
			}
		}
		return fileLists;
	}

}
