/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.service;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.BusinessException;
import cn.forp.framework.core.rowmapper.ADBeanPropertyRowMapper;
import cn.forp.framework.platform.vo.Remind;
import cn.forp.framework.platform.vo.RemindMember;
import cn.forp.framework.platform.vo.User;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 提醒参数管理Service
 *
 * @author		Bruce
 * @version	2014-02-27 09:41:36
 */
public class RemindService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger logger = Logger.getLogger(RemindService.class);

	/**
	 * 查询列表
	 * 
	 * @param user			操作人
	 * 
	 * @return 提醒列表
	 * @throws Exception
	 */
	public List<Remind> search(User user) throws Exception
	{
		return jdbc.query("Select * From Forp_Remind Where FK_DomainID=? Order By ID ASC", new Object[]{user.getDomainId()},
								new ADBeanPropertyRowMapper<Remind>(Remind.class));
	}

	/**
	 * 修改
	 * 
	 * @param user			操作人
	 * @param r				提醒参数
	 * @throws Exception
	 */
	public void update(User user, Remind r) throws Exception
	{
		// 重名判断
		if (isFieldDuplicate("Forp_Remind", "Name", r.getName(), "FK_DomainID=" + user.getDomainId() + " And ID<>" + r.getId()))
			throw new BusinessException("提醒参数“" + r.getName() + "”已存在，请检查您的输入！");

		// 保存
		updateTable(r, new String[]{"name", "days", "state"});
		logger.debug("修改提醒参数：" + r.getId());
	}

	/**
	 * 启用/停用提醒
	 * 
	 * @param id						提醒编号
	 * @param newStatus		新状态
	 * 
	 * @throws Exception
	 */
	public void changeStatus(Long id, int newStatus) throws Exception
	{
		jdbc.update("Update Forp_Remind Set State=? Where ID=?", new Object[]{newStatus, id});
	}

	//=================================================================
	//		Member
	//=================================================================

	/**
	 * 查询提醒发送人列表
	 * 
	 * @param id		提醒参数ID
	 * 
	 * @return 提醒人列表
	 * @throws Exception
	 */
	public List<RemindMember> searchMember(Long id) throws Exception
	{
		String sql = "Select t.* From (Select m.*, u.UserName From Forp_Remind_Member m, Forp_User u Where m.FK_UserID=u.ID) t " +
				"Where t.FK_RemindID=? Order By t.UserName";
		return jdbc.query(sql, new Object[]{id}, new ADBeanPropertyRowMapper<RemindMember>(RemindMember.class));
	}

	/**
	 * 保存提醒人（添加/修改）
	 * 
	 * @param id					主键
	 * @param remindId		提醒参数ID
	 * @param userId			提醒人ID
	 * @throws Exception
	 */
	public void saveMember(Long id, Long remindId, Long userId) throws Exception
	{
		// 重名判断
		if (isFieldDuplicate("Forp_Remind_Member", "FK_UserID", userId, "FK_RemindID=" + remindId + (-1 == id ? "" : " And ID<>" + id)))
			throw new BusinessException("该提醒人已添加，请检查您的输入！");

		if (-1 == id)
		{
			// 添加
			jdbc.update("Insert Into Forp_Remind_Member (FK_RemindID, FK_UserID) Values(?, ?)", new Object[]{remindId, userId});
		}
		else
		{
			// 修改
			jdbc.update("Update Forp_Remind_Member Set FK_UserID=? Where ID=?", new Object[]{userId, id});
		}
	}

	/**
	 * 删除提醒人
	 * 
	 * @param id					主键
	 * @throws Exception
	 */
	public void removeMember(Long id) throws Exception
	{
		jdbc.update("Delete From Forp_Remind_Member Where ID=?", new Object[]{id});
	}
}