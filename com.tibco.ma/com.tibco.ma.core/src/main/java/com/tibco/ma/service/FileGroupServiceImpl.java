package com.tibco.ma.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.ValidateUtils;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.FileGroupDao;
import com.tibco.ma.model.Category;
import com.tibco.ma.model.File;
import com.tibco.ma.model.FileGroup;
import com.tibco.ma.model.Image;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.Specification;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@Service
public class FileGroupServiceImpl extends BaseServiceImpl<FileGroup> implements
		FileGroupService {
	
	private static final Logger log = LoggerFactory
			.getLogger(FileGroupServiceImpl.class);

	@Autowired
	private FileGroupDao fileGroupDao;

	@Autowired
	private FileService fileService;

	@Autowired
	private SpecificationService specificationService;

	@Autowired
	private CategoryService categoryService;

	@Override
	public BaseDao<FileGroup> getDao() {
		return fileGroupDao;
	}

	@Override
	public void deleteByGroupId(String groupId, boolean col) throws Exception {
		if (StringUtil.notEmpty(groupId)) {
			fileService.deleteByGroupId(groupId);
			Query query = new Query();
			query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(groupId)));
			FileGroup lists = fileGroupDao.findOne(query, FileGroup.class);
			if (lists != null) {
				fileGroupDao.remove(query, FileGroup.class);
				if (col) {
					deleteCategoryByGroupId(lists.getId());
				}
			}
		}

	}

	@Override
	public FileGroup saveFileGroup(HttpServletRequest request,
			BaseGridFSService baseGridFSService) throws Exception {
		JSONObject jsonObject = new JSONObject();
		String categoryId = request.getParameter("category");
		String groupId = request.getParameter("groupId");
		FileGroup fileGroup = new FileGroup();
		String fileGroupName = null;
		FileGroup fileGroupResult = null;

		// get fileGroupName
		if (StringUtil.notEmpty(groupId)) {
			Query query = new Query();
			query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(groupId)));
			fileGroup = fileGroupDao.findOne(query, FileGroup.class);
			if (fileGroup != null) {
				fileGroupName = fileGroup.getGroupName();
			}
		}

		List<String> deletefileNames = new ArrayList<String>();
		List<File> deleteListFiles = new ArrayList<File>();

		if (StringUtil.isEmpty(groupId)) {
			if (StringUtil.notEmpty(categoryId)) {
				fileGroup.setCategory(new Category(categoryId));
			}
			if (request.getParameterMap().containsKey("fileGroupDescription")) {
				fileGroup.setDescription(request
						.getParameter("fileGroupDescription"));
			}
			fileGroupDao.save(fileGroup);
			groupId = fileGroup.getId();

		}
		String filenames = "";
		String fileGroupDescrition = request
				.getParameter("fileGroupDescription");

		if (StringUtil.notEmpty(groupId)) {
			Map<String, String[]> requestMap = request.getParameterMap();
			if (requestMap.containsKey("deleteIds") && requestMap != null) {
				if (requestMap.get("deleteIds").length > 0) {
					String idStr = requestMap.get("deleteIds")[0];
					String type = request.getParameter("uploadType");
					deleteListFiles = fileService.QueryByIds(idStr, type);
					if (StringUtil.notEmpty(idStr)) {
						fileService.deleteOldFile(idStr, type);
					}
					if (StringUtil.isEmpty(type)) {
						groupId = null;
					}
				}
			}
		}
		log.debug("delete file success");
		// get delete group Name
		if (ValidateUtils.isValidate(deleteListFiles)) {
			for (File file : deleteListFiles) {
				deletefileNames.add(file.getName());
			}
		}
		
		// delete old name
		// update fileGroupName
		fileGroupName = deleteOldFileName(fileGroupName, deletefileNames);
		log.debug("result fileGroupName is {}"+fileGroupName);
		List<File> listFiles = fileService.saveFiles(request, groupId,
				baseGridFSService);
		// build fileNams
		if (ValidateUtils.isValidate(listFiles)) {
			for (File file : listFiles) {
				if (StringUtil.notEmpty(filenames)
						&& StringUtil.notEmpty(file.getName())) {
					filenames += " , " + file.getName();
				} else {
					if (StringUtil.notEmpty(file.getName())) {
						filenames = file.getName();
					}
				}
			}
		}

		// update fileGroupName
		if (StringUtil.notEmpty(fileGroupName)) {
			if (StringUtil.notEmpty(filenames)) {
				fileGroupName += " , " + filenames;
			}
		} else {
			fileGroupName = filenames;
		}

		// update filegoupDescription
		if (StringUtil.notEmpty(groupId)) {
			Update update = new Update();
			update.set("groupName", fileGroupName);
			update.set("description", fileGroupDescrition);
			Query query = new Query();
			query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(groupId));
			fileGroupDao.update(query, update, FileGroup.class);

			fileGroupResult = getFileGroupAndFile(groupId);
			log.debug("upload file success");
		}
		return fileGroupResult;
	}

	private String deleteOldFileName(String groupName, List<String> fileNames) {
		List<String> exist = new ArrayList<String>();
		if (StringUtil.notEmpty(groupName)) {
			String groupNames[] = groupName.split(",");
			for (int j = 0; j < groupNames.length; j++) {
				exist.add(groupNames[j].trim());
			}
			for (int i = 0; i < fileNames.size(); i++) {
				if (exist.contains(fileNames.get(i)) && exist.size() > 0) {
					exist.remove(fileNames.get(i));
				}
			}
		}
		String fileNameStr = null;
		if (exist.size() > 0) {
			for (String str : exist) {
				if (StringUtil.notEmpty(fileNameStr)
						&& StringUtil.notEmpty(str)) {
					fileNameStr += " , " + str;
				} else {
					if (StringUtil.notEmpty(str)) {
						fileNameStr = str;
					}
				}
			}
		}
		return fileNameStr;
	}

	@Override
	public FileGroup queryByGroupId(String groupId) throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(groupId));
		FileGroup fileGroups = fileGroupDao.findOne(query, FileGroup.class);
		return fileGroups;
	}

	@Override
	public Image getImage(String groupId, String spec_name) throws Exception {
		List<File> files = fileService.queryByGroupId(groupId);
		Query query = new Query();
		Image image = new Image();
		for (File file : files) {
			if (file.getSpecification() != null) {
				query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
						file.getSpecification().getId()));
				Specification specification = specificationService.findOne(
						query, Specification.class);
				if (specification != null && StringUtil.notEmpty(spec_name)) {
					if (spec_name.equals(specification.getName())) {
						image.setName(file.getName());
						image.setUrl(file.getFilePath());
					}
				}
			}
		}
		return image;
	}

	@Override
	public void deleteCategoryByGroupId(String groupId) throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(groupId)));
		FileGroup fileGroup = fileGroupDao.findOne(query, FileGroup.class);
		if (fileGroup != null && fileGroup.getCategory() != null) {
			Category category = fileGroup.getCategory();
			Query deleteCategory = new Query();
			if (StringUtil.notEmpty(category.getId())) {
				deleteCategory.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
						new ObjectId(category.getId())));
				categoryService.delete(query, Category.class);
				specificationService.deleteByCategory(category.getId());
			}
		}
	}

	@Override
	public FileGroup getFileGroupAndFile(String groupId) throws Exception {
		FileGroup fileGroup = null;
		fileGroup = queryByGroupId(groupId);
		List<File> file = fileService.queryByGroupId(groupId);
		fileGroup.setLists(file);
		return fileGroup;
	}
}
