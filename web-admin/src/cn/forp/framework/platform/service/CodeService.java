/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.service;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.BusinessException;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.core.vo.PageSort;
import cn.forp.framework.platform.vo.SystemCode;
import cn.forp.framework.platform.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * 通用编码Service
 *
 * @author	Bruce
 * @version	2016-08-25 18:12:36
 */
@Service
public class CodeService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(CodeService.class);

	/**
	 * 分页查询
	 *
	 * @param user        操作人
	 * @param categoryId  编码类别
	 * @param ps          分页排序信息
	 */
	public Page<SystemCode> search(User user, Long categoryId, PageSort ps) throws Exception
	{
		lg.debug("编码类型：{}", categoryId);
		return findByPage("select * from Forp_Code where FK_DomainID=? and FK_CategoryID=?",
				SystemCode.class, ps, user.getDomainId(), categoryId);
	}

	/**
	 * 查询指定类别的编码列表
	 */
	public List<SystemCode> getAll(User user, Long categoryId) throws Exception
	{
		String sql = "select * from Forp_Code where FK_DomainID=? and FK_CategoryID=? and Status=1 order by ID";
		return findByList(sql, SystemCode.class, user.getDomainId(), categoryId);
	}

	/**
	 * 添加
	 *
	 * @param user  操作人
	 * @param code	编码
	 */
	public void create(User user, SystemCode code) throws Exception
	{
		// 重名检查
		if (isFieldDuplicate("Forp_Code", "Name", code.getName(), "FK_DomainID=" + user.getDomainId() + " and FK_CategoryID=" + code.getCategoryId()))
			throw new BusinessException("“" + code.getName() + "”已存在，请检查您的输入！");

		// 保存
		code.setDomainId(user.getDomainId());
		insertIntoTable(code, null, new String[]{"createDate", "lastModifyDate"});
	}

	/**
	 * 修改
	 *
	 * @param user  操作人
	 * @param code	编码
	 */
	public void update(User user, SystemCode code) throws Exception
	{
		// 重名检查
		String where = "ID<>" + code.getId() + " and FK_DomainID=" + user.getDomainId() + " and FK_CategoryID=" + code.getCategoryId();
		if (isFieldDuplicate("Forp_Code", "Name", code.getName(), where))
			throw new BusinessException("“" + code.getName() + "”已存在，请检查您的输入！");

		// 保存
		code.setLastModifyDate(new Timestamp(System.currentTimeMillis()));
		updateTable(code, null, new String[]{"domainId", "categoryId", "createDate"});
	}

	/**
	 * 删除（引用检测靠业务模块的主外键约束来完成）
	 *
	 * @param id  主键
	 */
	public void delete(Long id) throws Exception
	{
		jdbc.update("delete from Forp_Code where ID=?", id);
	}
}