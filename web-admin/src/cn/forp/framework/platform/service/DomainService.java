/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.service;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.FORP;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 域管理Service
 *
 * @author		Bruce
 * @version	2013-5-11 13:53:36
 */
@Service
public class DomainService extends BaseService
{
//	/**
//	 * Log4j logger
//	 */
//	private static Logger logger = Logger.getLogger(DomainService.class);
//	/**
//	 * SystemService
//	 */
//	private SystemService platformSystemService;
//
//	/**
//	 * 分页查询
//	 * 
//	 * @param id						域编号（主键）
//	 * @param name				域名称
//	 * @param userName		联系人
//	 * @param telephone		联系电话
//	 * @param state				状态
//	 * @param provinceId		省份
//	 * @param cityId				城市
//	 * @param regionId			区县
//	 * @param extjs				ExtJS分页和排序信息
//	 * 
//	 * @return 分页对象
//	 * @throws Exception
//	 */
//	public Page<Domain> search(long id, String name, String userName, String telephone, int state, long provinceId, long cityId, long regionId, PageSort extjs) throws Exception
//	{
//		String sql = "Select * From V_Forp_Domain Where ID>1";
//		List<Object> params = new ArrayList<Object>();
//
//		if (-1 != id)
//		{
//			sql += " And ID =?";
//			params.add(id);
//		}
//
//		if (StringUtils.isNotBlank(name))
//		{
//			sql += " And Name Like ?";
//			params.add("%" + name + "%");
//		}
//
//		if (StringUtils.isNotBlank(userName))
//		{
//			sql += " And UserName Like ?";
//			params.add("%" + userName + "%");
//		}
//
//		if (StringUtils.isNotBlank(telephone))
//		{
//			sql += " And Telephone Like ?";
//			params.add("%" + telephone + "%");
//		}
//
//		if (-1 != state)
//		{
//			sql += " And State=?";
//			params.add(state);
//		}
//
//		if (-1 != provinceId)
//		{
//			sql += " And FK_ProvinceID=?";
//			params.add(provinceId);
//		}
//
//		if (-1 != cityId)
//		{
//			sql += " And FK_CityID=?";
//			params.add(cityId);
//		}
//
//		if (-1 != regionId)
//		{
//			sql += " And FK_RegionID=?";
//			params.add(regionId);
//		}
//
//		logger.debug("SQL：" + sql);
//		return searchPage(sql, params.toArray(new Object[0]), Domain.class, extjs);
//	}
//
//	/**
//	 * 注册客户档案
//	 * 
//	 * @param d				客户档案
//	 * @param user			操作人
//	 * 
//	 * @throws Exception
//	 */
//	public void create(Domain d, User user) throws Exception
//	{
//		// 1 重名检查
//		if (isFieldDuplicate("Forp_Domain", "Name", d.getName(), null))
//			throw new BusinessException("客户名称“" + d.getName() + "”已注册，请检查您的输入！");
//
//		// 2 保存域信息
//		d.setCreateUserId(user.getId());
//		Date now = new Date(System.currentTimeMillis());
//		d.setCreateDate(now);
//		d.setExpireDate(now);
//		long id = insertIntoTable(d, null, new String[]{"sn", "beginDate", "price1", "price2", "money1", "money2", "money3", "priceAuditUserId", "priceAuditDate", "validationCode"});
//		logger.info("注册新客户：" + d.getName() + "[" + id + "]");
//
//		// 3 更新域信息：SN
//		String sn = StringUtil.encryptWithMD5("CS_" + id);
//		getJdbcTemplate().update("Update Forp_Domain Set SN=? Where ID=?", new Object[]{sn, id});
//	}
//
//	/**
//	 * 修改客户档案
//	 * 
//	 * @param d		客户档案
//	 * @throws Exception
//	 */
//	public void update(Domain d) throws Exception
//	{
//		// 2 重名检查
//		if (isFieldDuplicate("Forp_Domain", "Name", d.getName(), "ID<>" + d.getId()))
//			throw new BusinessException("客户名称“" + d.getName() + "”已存在，请检查您的输入！");
//
//		// 3 保存域信息
//		updateTable(d, new String[]{"provinceId", "cityId", "regionId", "category", "name", "address", "userName", "telephone", "item1", "item2", "item3", "item4", "item5","item6", "item7", "item8", "item9", "item10", "managerUserId", "remark"});
//		logger.info("修改客户档案：" + d.getName());
//	}
//
//	//=================================================================
//	//		联系人
//	//=================================================================
//
//	/**
//	 * 查询
//	 * 
//	 * @param domainId		客户ID
//	 * @throws Exception
//	 */
//	public List<DomainContact> searchContact(Long domainId) throws Exception
//	{
//		String sql = "Select * From Forp_Domain_Contact Where FK_DomainID=? Order By Name";
//		return getJdbcTemplate().query(sql, new Object[]{domainId}, new ADBeanPropertyRowMapper<DomainContact>(DomainContact.class));
//	}
//
//	/**
//	 * 新建联系人
//	 * 
//	 * @param dc		联系人
//	 * @throws Exception
//	 */
//	public void createContact(DomainContact dc) throws Exception
//	{
//		// 1 重名检查
//		if (isFieldDuplicate("Forp_Domain_Contact", "Name", dc.getName(), "FK_DomainID=" + dc.getDomainId()))
//			throw new BusinessException("联系人“" + dc.getName() + "”已登记，请检查您的输入！");
//
//		insertIntoTable(dc);
//	}
//
//	/**
//	 * 修改客户档案
//	 * 
//	 * @param dc		客户档案
//	 * @throws Exception
//	 */
//	public void updateContact(DomainContact dc) throws Exception
//	{
//		// 重名检查
//		if (isFieldDuplicate("Forp_Domain_Contact", "Name", dc.getName(), "ID<>" + dc.getId() + " And FK_DomainID=" + dc.getDomainId()))
//			throw new BusinessException("联系人“" + dc.getName() + "”已登记，请检查您的输入！");
//
//		// 更新
//		updateTable(dc, null, new String[]{"domainId"});
//	}
//
//	//=================================================================
//	//		客户服务
//	//=================================================================
//
//	/**
//	 * 查询服务版本
//	 * 
//	 * @return 版本列表
//	 * @throws Exception
//	 */
//	public List<ServiceVersion> searchServiceVersion() throws Exception
//	{
//		return getJdbcTemplate().query("Select * From Forp_ServiceVersion Where State=1 Order By ID",
//				new ADBeanPropertyRowMapper<ServiceVersion>(ServiceVersion.class));
//	}
//
//	/**
//	 * 交费历史记录查询
//	 * 
//	 * @param domainId		域编号
//	 * 
//	 * @return 费用列表
//	 * @throws Exception
//	 */
//	public List<DomainFee> searchFee(Long domainId) throws Exception
//	{
//		String sql = "Select v.* From " +
//								"(Select f.*, u.UserName, sv.Name As ServiceVersion From Forp_Domain_Fee f, Forp_User u, Forp_ServiceVersion sv " +
//								"Where f.Category<3 And f.FK_UserID=u.ID And f.FK_ServiceID=sv.ID) v " +
//							"Where v.FK_DomainID=? Order By v.ID";
//		return getJdbcTemplate().query(sql, new Object[]{domainId}, new ADBeanPropertyRowMapper<DomainFee>(DomainFee.class));
//	}
//
//	/**
//	 * 优惠特批
//	 * 
//	 * @param id						客户ID
//	 * @param serviceId			服务版本
//	 * @param price1				首年优惠费用
//	 * @param price2				后续优惠费用
//	 * @param user					操作人
//	 * 
//	 * @throws Exception
//	 */
//	public void audit(Long id, Long serviceId, Integer price1, Integer price2, User user) throws Exception
//	{
//		// 检查域校验码
//		Domain d = validateDomainCode(id);
//
//		List<Object> params = new ArrayList<Object>();
//		String sql = "Update Forp_Domain Set FK_ServiceID=?, Price1=?, Price2=?, PriceAuditUserID=?, PriceAuditDate=?";
//		params.add(serviceId);
//		params.add(price1);
//		params.add(price2);
//		params.add(user.getId());
//		params.add(new Date());
//
//		// 已开通服务
//		if (3 != d.getState().intValue())
//		{
//			sql += ", ValidationCode=?";
//			String vc = getDomainValidationCode(id, serviceId, d.getExpireDate(), d.getState());
//			params.add(vc);
//		}
//
//		sql += " Where ID=?";
//		params.add(id);
//		getJdbcTemplate().update(sql, params.toArray(new Object[0]));
//		logger.info("优惠特批客户服务费用！");
//	}
//
//	/**
//	 * 启用/停用客户服务
//	 * 
//	 * @param id						编码编号
//	 * @param newState			新状态
//	 * 
//	 * @throws Exception
//	 */
//	public void changeServiceState(long id, int newState) throws Exception
//	{
//		// 检查域校验码
//		Domain d = validateDomainCode(id);
//
//		logger.info("更改客户" + id + "的服务状态为：" + newState);
//		String newVC = getDomainValidationCode(id, d.getServiceId(), d.getExpireDate(), newState);
//		getJdbcTemplate().update("Update Forp_Domain Set State=?, ValidationCode=? Where ID=?", new Object[]{newState, newVC, id});			
//	}
//
//	/**
//	 * 初始化域SQL脚本
//	 * 
//	 * @param domainId				域ID
//	 * @param domainName			域名称
//	 * @param serviceId					服务版本ID
//	 * @param sqlKeyWords			执行SQL过滤关键字（null - 执行所有的语句；非null - 只执行该关键字列表中的sql语句）
//	 * @throws Exception
//	 */
//	private void initDomainSql(Long domainId, String domainName, Long serviceId, List<String> sqlKeyWords) throws Exception
//	{
//		String sqlFileName = getValueById("Forp_ServiceVersion", "SqlFileName", serviceId);
//		logger.info("初始化客户sql初始化脚本：");
//		InputStream is = new FileInputStream(HuaYuIStudy.WEB_APP_PATH + "/disk-file/init-sql/" + sqlFileName);
//		List<String> sqlFileLines = IOUtils.readLines(is, "GBK");
//		IOUtils.closeQuietly(is);
//
//		String sql = null;
//		boolean isValid = false;
//		// 逐条执行SQL命令
//		for (String l : sqlFileLines)
//		{
//			sql = l.trim().toLowerCase();
//
//			// 屏蔽空行，注释，commit语句。
//			if (0 == sql.length() || sql.startsWith("--") || -1 != sql.indexOf("commit"))
//			{
////				logger.debug(l);
//				continue;
//			}
//
//			// 按照关键字过滤需要执行的sql语句
//			if (null != sqlKeyWords)
//			{
//				isValid = false;
//				for (String keyWords : sqlKeyWords)
//				{
//					if (-1 != sql.indexOf(keyWords))
//					{
//						isValid = true;
//						break;
//					}
//				}
//
//				// 不是目标SQL，下一条
//				if (!isValid)
//					continue;
//			}
//
//			// 动态替换参数：<DomainID>，<DomainName>
//			sql = l.replaceAll("<DomainID>", String.valueOf(domainId)).replaceAll("<DomainName>", domainName);
//			logger.debug(sql);
//			getJdbcTemplate().update(sql);
//		}
//
//		// TODO 批量执行时不能正确处理Select子语句： java.sql.BatchUpdateException: Can not issue SELECT via executeUpdate().
//		// getJdbcTemplate().batchUpdate(sqls.toArray(new String[0]));
//	}

	/**
	 * 生成域验证码
	 * 
	 * @param domainId
	 * @param productId
	 * @param expireDate
	 * @param productState
	 * 
	 * @return 域校验码
	 */
	public String getDomainValidationCode(long domainId, long productId, Date expireDate, int productState)
	{
		return DigestUtils.md5Hex(FORP.MD5_SALT_PREFIX + domainId + "_" + productId + "_" + DateFormatUtils.format(expireDate, FORP.PATTERN_DATE) + "_" + productState);
	}

//	/**
//	 * 开通服务
//	 * 
//	 * @param id								客户ID
//	 * @param name						客户名称
//	 * @param serviceId					服务版本
//	 * @param servicePeriod			服务缴费周期
//	 * @param action						开通方式：1 - 缴费；2 - 试用。
//	 * @param beginDate				开通日期
//	 * @param months					试用周期
//	 * @param money					缴费金额
//	 * @param feeDate					缴费日期
//	 * @param feeRemark				备注
//	 * @param user							操作人
//	 * @throws Exception
//	 */
//	public void registerService(Long id, String name, Long serviceId, int servicePeriod, int action, String beginDate, int months, int money,
//			String feeDate, String feeRemark, User user) throws Exception
//	{
//		Calendar today = Calendar.getInstance();
//		Date bgDate = DateUtil.getDate(beginDate);
//
//		// 1 保存收费记录
//		DomainFee df = new DomainFee();
//		df.setDomainId(id);
//		df.setServiceId(serviceId);
//		df.setUserId(user.getId());
//		df.setAction(action);
//		df.setMoney(1 == action ? money : 0);
//		df.setOrigExpireDate(bgDate);
//		df.setMonths(1 == action ? servicePeriod : months);
//		// 操作日期（记录当前时分秒信息）
//		df.setCreateDate(DateUtil.getDate(feeDate));
//		df.setCreateDate(DateUtils.setHours(df.getCreateDate(), today.get(Calendar.HOUR_OF_DAY)));
//		df.setCreateDate(DateUtils.setMinutes(df.getCreateDate(), today.get(Calendar.MINUTE)));
//		df.setCreateDate(DateUtils.setSeconds(df.getCreateDate(), today.get(Calendar.SECOND)));
//		df.setRemark(feeRemark);
//		insertIntoTable(df);
//
//		// 2 更新域信息：服务版本，开通日期，过期日期，校验码，状态，实收总额
//		String sql = "Update Forp_Domain Set FK_ServiceID=?, BeginDate=?, ExpireDate=?, ValidationCode=?, State=1, " +
//							"Money2=(Select Sum(Money) From Forp_Domain_Fee Where FK_DomainID=? And Action<=2) Where ID=?";
//		Date expireDate = DateUtils.addMonths(bgDate, (1 == action ? servicePeriod : months));
//		String vc = getDomainValidationCode(id, serviceId, expireDate, 1);
//		getJdbcTemplate().update(sql, new Object[]{serviceId, bgDate, expireDate, vc, id, id});
//
//		// 3 初始化域SQL脚本
//		initDomainSql(id, name, serviceId, null);
//
//		// 4 加载域参数Cache
//		platformSystemService.loadGlobalParameters(id);
//		platformSystemService.loadMenuCache(id);
//	}
//
//	/**
//	 * 检查域校验码
//	 * 
//	 * @param id		域ID
//	 * 
//	 * @return 域对象
//	 * @throws Exception
//	 */
//	private Domain validateDomainCode(Long id) throws Exception
//	{
//		Domain d = new Domain();
//		d.setId(id);
//
//		String sql = "Select Name, ExpireDate, State, FK_ServiceID, ValidationCode From Forp_Domain Where ID=?";
//		SqlRowSet rs = getJdbcTemplate().queryForRowSet(sql, new Object[]{id});
//		rs.next();
//		d.setName(rs.getString("Name"));
//		d.setState(rs.getInt("State"));
//		d.setServiceId(rs.getLong("FK_ServiceID"));
//		d.setValidationCode(rs.getString("ValidationCode"));
//
//		if ("Oracle".equalsIgnoreCase(HuaYuIStudy.DB_TYPE))
//			d.setExpireDate(((TIMESTAMP) rs.getObject("ExpireDate")).timestampValue());
//		else
//			d.setExpireDate(rs.getTimestamp("ExpireDate"));
//
//		String vc = getDomainValidationCode(id, d.getServiceId(), d.getExpireDate(), d.getState());
//		// 未开通服务时不检查校验码 || 校验码正确
//		if (3 == d.getState().intValue() || vc.equals(d.getValidationCode()))
//			return d;
//		else
//			throw new BusinessException("该客户档案的服务记录变动异常，操作失败！<br/>请及时联系系统管理员！");
//	}
//
//	/**
//	 * 缴费续期
//	 * 
//	 * @param domainId				客户ID
//	 * @param serviceId					服务版本ID
//	 * @param servicePeriod			服务周期
//	 * @param expireDate				目前过期日期
//	 * @param money					交费金额
//	 * @param feeDate					交费日期
//	 * @param feeRemark				交费备注
//	 * @param user							操作人
//	 * @throws Exception
//	 */
//	@SuppressWarnings("unchecked")
//	public void recharge(Long domainId, Long serviceId, int servicePeriod, String expireDate, Integer money, String feeDate, String feeRemark, User user) throws Exception
//	{
//		Calendar today = Calendar.getInstance();
//		// 1 检查域校验码
//		Domain d = validateDomainCode(domainId);
//
//		// 2 保存缴费记录
//		DomainFee df = new DomainFee();
//		df.setDomainId(domainId);
//		df.setServiceId(serviceId);
//		df.setUserId(user.getId());
//		df.setAction(1);
//		df.setMoney(money);
//		df.setOrigExpireDate(DateUtil.getDate(expireDate));
//		df.setMonths(servicePeriod);
//		// 操作日期（记录当前时分秒信息）
//		df.setCreateDate(DateUtil.getDate(feeDate));
//		df.setCreateDate(DateUtils.setHours(df.getCreateDate(), today.get(Calendar.HOUR_OF_DAY)));
//		df.setCreateDate(DateUtils.setMinutes(df.getCreateDate(), today.get(Calendar.MINUTE)));
//		df.setCreateDate(DateUtils.setSeconds(df.getCreateDate(), today.get(Calendar.SECOND)));
//		df.setRemark(feeRemark);
//		insertIntoTable(df);
//
//		// 3 更新域信息：服务版本，过期日期，校验码，状态，实收总额
//		String sql = "Update Forp_Domain Set FK_ServiceID=?, ExpireDate=?, ValidationCode=?, State=1, " +
//							"Money2=(Select Sum(Money) From Forp_Domain_Fee Where FK_DomainID=? And Action<=2) Where ID=?";
//		Date newExpireDate = DateUtils.addMonths(df.getOrigExpireDate(), servicePeriod);
//		String vc = getDomainValidationCode(domainId, serviceId, newExpireDate, 1);
//		getJdbcTemplate().update(sql, new Object[]{serviceId, newExpireDate, vc, domainId, domainId});
//
//		// 4 更换服务版本
//		if (d.getServiceId().longValue() != serviceId)
//		{
//			// 删除原功能菜单 TODO 比对菜单，不删除已有的模块（保持原有的角色权限）
//			getJdbcTemplate().update("Delete From Forp_Menu Where FK_DomainID=?", domainId);
//			// 重新初始化域SQL脚本
//			initDomainSql(domainId, d.getName(), serviceId, HuaYuIStudy.VERSION_CHANGE_SQL_KEY_WORDS);
//			// initDomainAdminPermission(df.getDomainId());
//
//			// 删除原Menu cache，重新加载最新的Menu cache
//			Menu m = null;
//			for (Long key : (List<Long>) HuaYuIStudy.MENU_CACHE.getKeys())
//			{
//				m = (Menu) (HuaYuIStudy.MENU_CACHE.get(key).getObjectValue());
//				if (domainId.longValue() == m.getDomainId().longValue())
//					HuaYuIStudy.MENU_CACHE.remove(key);
//			}
//			platformSystemService.loadMenuCache(domainId);
//
//			logger.info("更换域[" + domainId + "]服务版本：" + d.getServiceId() + "-->" + serviceId);
//		}
//
//		logger.info("域[" + domainId + "]缴费续期：￥" + df.getMoney() + "，" + df.getMonths() + "个月");
//
//		// TODO 给管理员发送通知信息：mail，sms
//	}
//
//	//=================================================================
//	//		客户档案查询
//	//=================================================================
//
//	/**
//	 * 缴费历史分页查询
//	 * 
//	 * @param name				域名称
//	 * @param userId				缴费人
//	 * @param fromDate		缴费开始日期
//	 * @param toDate				缴费结束日期
//	 * @param extjs				ExtJS分页和排序信息
//	 * 
//	 * @return 分页
//	 * @throws Exception
//	 */
//	public Page<DomainFee> searchFeeLog(String name, Long userId, String fromDate, String toDate, PageSort extjs) throws Exception
//	{
//		String sql = "Select v.* From " +
//								"(Select a.*, b.Name As DomainName, c.UserName, b.ServiceVersion, b.ManagerUserName " +
//								"From Forp_Domain_Fee a, V_Forp_Domain b, Forp_User c " +
//								"Where a.FK_DomainID=b.ID And a.FK_UserID=c.ID) v " +
//							"Where v.CreateDate>=? And v.CreateDate<=?";
//
//		List<Object> params = new ArrayList<Object>();
//		params.add(DateUtil.getDateTime(fromDate + " 00:00:00"));
//		params.add(DateUtil.getDateTime(toDate + " 23:59:59"));
//
//		if (StringUtils.isNotBlank(name))
//		{
//			sql += " And v.DomainName Like ?";
//			params.add("%" + name + "%");
//		}
//
//		if (-1 != userId)
//		{
//			sql += " And v.FK_UserID=?";
//			params.add(userId);
//		}
//
//		logger.debug("SQL：" + sql);
//		return searchPage(sql, params.toArray(new Object[0]), DomainFee.class, extjs);
//	}
//
//
//
//
//
//
//
//	/**
//	 * @param platformSystemService the platformSystemService to set
//	 */
//	public void setPlatformSystemService(SystemService platformSystemService)
//	{
//		this.platformSystemService = platformSystemService;
//	}
}