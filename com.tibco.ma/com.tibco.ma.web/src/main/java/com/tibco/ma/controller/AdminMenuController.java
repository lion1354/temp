package com.tibco.ma.controller;

import java.util.List;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.model.AdminMenu;
import com.tibco.ma.service.AdminMenuService;
/**
 * 
 * @author qiankun
 *
 */
@Controller
@RequestMapping("ma/1/menu")
public class AdminMenuController {
	private static final Logger log = LoggerFactory
			.getLogger(AdminMenuController.class);

	@Resource
	private AdminMenuService menuService;
	/**
	 * 
	 * @return all menus
	 */
	@RequestMapping(value = "loadTree", method = RequestMethod.POST)
	public ResponseEntity<?> loadTree() {
		try {
			log.debug("load Tree");
			Query query = new Query();
			query.addCriteria(Criteria.where("p_code").is(""));
			AdminMenu rootMenu = menuService.findOne(query, AdminMenu.class);
			if (rootMenu == null) {
				AdminMenu adminMenu = new AdminMenu();
				adminMenu.setCode("Root Menu");
				adminMenu.setDescribe("Menu Root node");
				adminMenu.setMenuLevel(0);
				adminMenu.setName("Root Menu");
				adminMenu.setOrder(0);
				adminMenu.setP_code("");
				adminMenu.setUrl("");
				menuService.save(adminMenu);
			}
//			List<AdminMenu> lists = menuService.find(new Query(),
//					AdminMenu.class);
			Query queryFilter= new Query();
			queryFilter.with(new Sort(new Order("menuLevel")));
			queryFilter.with(new Sort(new Order("order")));
			List<AdminMenu> lists=menuService.find(queryFilter, AdminMenu.class);
//			TreeSet<AdminMenu> treeResult = new TreeSet<AdminMenu>();
//			for (AdminMenu adminMenu : lists) {
//				treeResult.add(adminMenu);	
//			}
			
			return ResponseUtils.successWithValue(lists);
		} catch (Exception e) {
			log.error("{}", e.getMessage());
			return ResponseUtils.fail(e.getMessage());
		}
	}
	
	/**
	 * saveOrUpdate 
	 * @param jsonObject
	 * {
	 * 	   "id":"${id}"
	 * 	   "name":"${name}"
	 * 		"code":"${code}"
	 * 		"order":"${order}"
	 * 		"menuLevel":"${menuLevel}"
	 * 		"p_code":"${pCode}"
	 * 		"url":"${url}"
	 * }
	 * @return
	 */
	@RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
	public ResponseEntity<?> saveOrUpdate(@RequestBody JSONObject jsonObject) {
		try {
			menuService.save(jsonObject);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e.getMessage());
			return ResponseUtils.fail(e.getMessage());
		}
	}
	
	/**
	 * delete menu by id
	 * @param jsonObject
	 * {
	 * 		"id":${id}
	 * }
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(@RequestBody JSONObject jsonObject) {
		try {
			menuService.delete(jsonObject);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e.getMessage());
			return ResponseUtils.fail(e.getMessage());
		}
	}

/*	private TreeSet<AdminMenu> buildTree(List<AdminMenu> list) {
		TreeSet<AdminMenu> root = new TreeSet<AdminMenu>();
		for (int i = 0; i < list.size(); i++) {
			AdminMenu menu = list.get(i);
			for (int j = 0; j < list.size(); j++) {
				if (StringUtil.notEmpty(list.get(j).getP_code())
						&& menu.getCode().equals(list.get(j).getP_code())) {
					if (menu.getChildren() == null) {
						menu.setChildren(new TreeSet<AdminMenu>());
					}
					menu.getChildren().add(list.get(j));
				}
			}
			if (StringUtil.isEmpty(menu.getP_code())) {
				root.add(menu);
			}
		}
		return root;
	}*/

	public String getCollection() {
		return "admin_menu";
	}
	/**
	 * load menus by id
	 * @param jsonObject
	 * 	{
	 * 			"id":${id}
	 * }
	 * @return
	 */
	@RequestMapping(value = "loadById", method = RequestMethod.POST)
	public ResponseEntity<?> loadById(@RequestBody JSONObject jsonObject) {
		String id = (String) jsonObject.get("id");
		try {
			AdminMenu menu = menuService.loadMenuById(id);
			return ResponseUtils.successWithValue(menu);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}
	/**
	 * check code unique
	 * @param code  
	 * @return
	 */
	@RequestMapping(value = "check", method = RequestMethod.GET)
	public ResponseEntity<?> checkCode(
			@RequestParam(value = "code", required = true) String code) {
		try {
			int result = menuService.checkCode(code);
			return ResponseUtils.successWithValue(result);
		} catch (Exception e) {
			log.error("{}", e.getMessage());
			return ResponseUtils.fail(e.getMessage());
		}

	}
}
