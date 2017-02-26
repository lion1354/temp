/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.forp.framework.core.rowmapper.ADBeanPropertyRowMapper;
import cn.forp.framework.core.util.Redis;
import cn.forp.framework.core.vo.ClassMapping;
import cn.forp.framework.core.vo.DBMappingProperty;
import cn.forp.framework.core.vo.Menu;
import cn.forp.framework.core.vo.MenuPrivilege;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.core.vo.PageSort;
import cn.forp.framework.platform.vo.Department;
import cn.forp.framework.platform.vo.Log;
import cn.forp.framework.platform.vo.User;
import oracle.sql.TIMESTAMP;
import redis.clients.jedis.JedisPool;

/**
 * Spring Service基类
 *
 * @author	Bruce
 * @version	2016-3-31 14:45:07
 */
@Service
public abstract class BaseService
{
  /**
   * Logger
   */
	private static final Logger lg = LoggerFactory.getLogger(BaseService.class);
  /**
   * Jdbc Template
   */
  @Autowired
  public JdbcTemplate jdbc;

	//=================================================================
	//		O/R Mapping
	//=================================================================

	/**
	 * 解析O/R mapping的字段和值列表信息
	 * 
	 * @param obj			      VO对象
	 * @param includeFields 插入的属性列表
	 * @param excludeFields	排除的属性列表
	 * @param mapping		    Class mapping信息
	 * 
	 * @return List数组：0 - 数据库字段列表；1 - 值列表
	 */
	private List<?>[] getClassMappingFieldAndValueList(Object obj, String[] includeFields, String[] excludeFields, ClassMapping mapping)
	{
		List<?>[] result = new ArrayList[2];

		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(obj);
		// Fields
		List<String> fields = new ArrayList<>();			// 要Insert的字段列表
		List<Object> fieldValues = new ArrayList<>();		// 字段值列表

		Map<String, DBMappingProperty> columns = mapping.getAllProperties();
		DBMappingProperty p;
		String colunmName;
		for (String s : columns.keySet())
		{
			colunmName = s;
			// 忽略PrimaryKey字段
			if (mapping.getPkColumn().equals(colunmName))
				continue;

			p = columns.get(colunmName);

			// 指定插入的字段
			if (null != includeFields && !ArrayUtils.contains(includeFields, p.getName()))
				continue;

			// 指定排除的字段
			if (null != excludeFields && ArrayUtils.contains(excludeFields, p.getName()))
				continue;

			// 加入该字段
			fields.add(colunmName);
			fieldValues.add(bw.getPropertyValue(p.getName()));
		}

		result[0] = fields;
		result[1] = fieldValues;
		return result;
	}

	/**
	 * 保存对象至Table
	 * 
	 * @param obj	VO对象
	 * 
	 * @return 数据库主键
	 */
	public long insertIntoTable(final Object obj) throws Exception
	{
		return insertIntoTable(obj, null, null);
	}

	/**
	 * 保存对象至Table
	 * 
	 * @param obj			      VO对象
	 * @param includeFields	插入的属性列表
	 * 
	 * @return 数据库主键
	 */
	public long insertIntoTable(final Object obj, final String[] includeFields) throws Exception
	{
		return insertIntoTable(obj, includeFields, null);
	}

	/**
	 * 插入对象至Table
	 * 
	 * @param obj			      VO对象
	 * @param includeFields	插入的属性列表
	 * @param excludeFields	排除的属性列表
	 * 
	 * @return 数据库主键
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected long insertIntoTable(final Object obj, final String[] includeFields, final String[] excludeFields) throws Exception
	{
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbc.update(connection ->
		{
			ClassMapping mapping = ADBeanPropertyRowMapper.getClassMapping(obj.getClass());

			List<?>[] fv = getClassMappingFieldAndValueList(obj, includeFields, excludeFields, mapping);
			// 要Insert的字段列表
			List<String> fields = (List<String>) fv[0];
			// 字段值列表
			List<Object> fieldValues = (List<Object>) fv[1];

			// SQL
			String sql = String.format("insert into %s (<fields>) values(<values>)", mapping.getTableName());
			sql = sql.replace("<fields>", StringUtils.join(fields, ", "));
			sql = sql.replace("<values>", StringUtils.repeat("?, ", fieldValues.size() - 1) + "?");
			lg.debug("Insert SQL：{}", sql);

	    PreparedStatement ps = connection.prepareStatement(sql, new String[]{mapping.getPkColumn()});
	    // 参数列表
	    Object param = null;
	    for (int i = 0; i < fieldValues.size(); i++)
			{
				param = fieldValues.get(i);
				if ("Oracle".equalsIgnoreCase(FORP.DB_TYPE) && param instanceof Date)
					ps.setObject(i + 1, new TIMESTAMP(new java.sql.Timestamp(((java.util.Date) param).getTime())));
				else
					ps.setObject(i + 1, param);
			}

			return ps;
		}, keyHolder);

		Number number = keyHolder.getKey();
		if (number == null)
		{
			return -1;
		}
		return number.longValue();
	}

	/**
	 * 更新对象属性至Table
	 * 
	 * @param obj			      VO对象
	 * @param includeFields	更新的属性列表
	 * @param excludeFields	排除的属性列表
	 */
	@SuppressWarnings("unchecked")
	protected void updateTable(Object obj, String[] includeFields, String[] excludeFields) throws Exception
	{
    ClassMapping mapping = ADBeanPropertyRowMapper.getClassMapping(obj.getClass());

    List<?>[] fv = getClassMappingFieldAndValueList(obj, includeFields, excludeFields, mapping);
    // 要Update的字段列表
    List<String> fields = (List<String>) fv[0];
    String column;
    for (int i = 0; i < fields.size(); i++)
		{
    	column = fields.get(i);
			fields.set(i, column + "=?");
		}

    // 字段值列表
    List<Object> fieldValues = (List<Object>) fv[1];
    // 主键字段值
    BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(obj);
    fieldValues.add(bw.getPropertyValue(mapping.getPkProperty()));

    // SQL
		String sql = "update " + mapping.getTableName() + " set " + StringUtils.join(fields, ", ") + " where " + mapping.getPkColumn() + "=?";
		lg.debug("Update SQL：{}", sql);

		jdbc.update(sql, fieldValues.toArray(new Object[0]));
	}

	/**
	 * 更新对象属性至Table
	 * 
	 * @param obj			      VO对象
	 * @param includeFields	更新的属性列表
	 */
	protected void updateTable(Object obj, String[] includeFields) throws Exception
	{
		updateTable(obj, includeFields, null);
	}
	
	/**
	 * 更新对象属性至Table
	 * 
	 * @param obj			      VO对象
	 * @param includeFields	更新的属性列表
	 * @param excludeFields	排除的属性列表
	 * @param idFieldName	主键名称
	 * @param idFieldValue	主键值
	 */
	@SuppressWarnings("unchecked")
	protected void updateTable(Object obj, String[] includeFields, String[] excludeFields, String idFieldName,
			String idFieldValue) throws Exception
	{
		ClassMapping mapping = ADBeanPropertyRowMapper.getClassMapping(obj.getClass());

		List<?>[] fv = getClassMappingFieldAndValueList(obj, includeFields, excludeFields, mapping);
		// 要Update的字段列表
		List<String> fields = (List<String>) fv[0];
		String column;
		for (int i = 0; i < fields.size(); i++)
		{
			column = fields.get(i);
			fields.set(i, column + "=?");
		}

		// 字段值列表
		List<Object> fieldValues = (List<Object>) fv[1];
		// 主键字段值
		fieldValues.add(idFieldValue);

		// SQL
		String sql = "update " + mapping.getTableName() + " set " + StringUtils.join(fields, ", ") + " where "
				+ idFieldName + "=?";
		lg.debug("Update SQL：{}", sql);

		jdbc.update(sql, fieldValues.toArray(new Object[0]));
	}

	//=================================================================
	//		引用检测
	//=================================================================

	/**
	 * 检查字段是否存在重复值
	 * 
	 * @param table			表名称
	 * @param fieldName	字段名
	 * @param value			字段值
	 * @param where			其它查询限制条件
	 * 
	 * @return boolean
	 */
	protected boolean isFieldDuplicate(String table, String fieldName, Object value, String where) throws Exception
	{
		String sql = "select ID from " + table + " where " + fieldName + "=?";
		if (StringUtils.isNotBlank(where))
			sql += " and " + where;

		SqlRowSet rs = jdbc.queryForRowSet(sql, value);
		return rs.next();
	}

	/**
	 * 检查其它表是否有引用
	 * 
	 * @param table			表名称
	 * @param fieldName	字段名
	 * @param value			字段值
	 * @param where			其它查询限制条件
	 * @return boolean
	 */
	protected boolean isUsedByOtherTable(String table, String fieldName, Object value, String where)
	{
		String sql = "select Count(*) as Num from " + table + " where " + fieldName + "=?" + (StringUtils.isNotBlank(where) ? " and " + where : "");
		SqlRowSet rs = jdbc.queryForRowSet(sql, value);
		rs.next();
		return (0 != rs.getInt("Num"));
	}

	//=================================================================
	//		查询
	//=================================================================

	/**
	 * 加载指定对象记录
	 * 
	 * @param voClazz	映射Class类
	 * @param id		  主键
	 */
	protected <T> T load(Class<T> voClazz, Long id) throws Exception
	{
		ClassMapping mapping = ADBeanPropertyRowMapper.getClassMapping(voClazz);
		List<T> oneRow = findByList("select * from " + mapping.getTableName() + " where " + mapping.getPkColumn() + "=?", voClazz, id);
		if (null != oneRow && 1 == oneRow.size())
			return oneRow.get(0);
		else
			return null;
	}

	/**
	 * 列表查询
	 * 
	 * @param sql		  SQL语句
	 * @param voClazz	映射Class类
	 * @param params	SQL参数列表
	 */
	protected <T> List<T> findByList(String sql, Class<T> voClazz, Object...params) throws Exception
	{
		return findByList(sql, params, voClazz);
	}

	/**
	 * 列表查询
	 * 
	 * @param sql		  SQL语句
	 * @param voClazz	映射Class类
	 * @param params	SQL参数列表
	 */
	protected <T> List<T> findByList(String sql, Object[] params, Class<T> voClazz) throws Exception
	{	
		return jdbc.query(sql, params, new ADBeanPropertyRowMapper<>(voClazz));
	}

	/**
	 * 分页查询
	 * 
	 * @param sql				SQL语句
	 * @param voClazz		O/R Mapping后的值对象Class
	 * @param ps				分页和排序信息
	 * @param params		查询条件
   * @return page
	 */
	protected <T> Page<T> findByPage(String sql, Class<T> voClazz, PageSort ps, Object...params) throws Exception
	{
		return findByPage(sql, params, voClazz, ps);
	}

	/**
	 * 分页查询
	 * 
	 * @param sql				SQL语句
	 * @param params		查询条件
	 * @param voClazz		O/R Mapping后的值对象Class
	 * @param ps				分页和排序信息
   * @return page
	 */
	protected <T> Page<T> findByPage(String sql, Object[] params, Class<T> voClazz, PageSort ps) throws Exception
	{
		Page<T> page = new Page<>();

		// 统计记录总数
		String temp = "select Count(*) " + sql.substring(sql.toLowerCase().indexOf("from"));
		SqlRowSet rs = jdbc.queryForRowSet(temp, params);
		if (rs.next())
			page.setTotal(rs.getLong(1));

		// 如果开始记录位置 > 记录总数，则后退1页。
		if (null != ps)
		{
			if (((ps.getPageNumber() - 1) * ps.getPageSize()) >= page.getTotal())
				ps.setPageNumber(ps.getPageNumber() - 1);

			// 检查倒退一页数据后，获取位置是否小于1
			if (ps.getPageNumber() < 1)
				ps.setPageNumber(1);

			lg.debug("分页参数：pageNumber-{}，pageSize-{}，sortName-{}，sortOrder-{}", ps.getPageNumber(), ps.getPageSize(), ps.getSortName(), ps.getSortOrder());
		}

		// 排序字段（如果有映射-转换为数据库字段名称）
		String sortField = "";
		if (null != ps && StringUtils.isNotBlank(ps.getSortName()))
		{
			// 默认为VO属性名称
			sortField = ps.getSortName();

			Map<String, DBMappingProperty> columns = ADBeanPropertyRowMapper.getClassMapping(voClazz).getAllProperties();
			String dbColumName;	DBMappingProperty pp;
			for (String s : columns.keySet())
			{
				// Key - 数据库字段名称
				dbColumName = s;
				pp = columns.get(dbColumName);

				if (sortField.equals(pp.getName()))
				{
					sortField = dbColumName;
					lg.debug("数据库排序字段：{}->{}", ps.getSortName(), sortField);
					break;
				}
			}
		}

		// 排序
		if (StringUtils.isNotBlank(sortField))
		{
			assert ps != null;
			sql += " order by " + sortField + " " + ps.getSortOrder();
		}

		// 分页
		if ("MySQL".equalsIgnoreCase(FORP.DB_TYPE))
		{
			// 使用MySQL数据库函数进行分页
			if (null != ps && -1 != ps.getPageSize())
				sql = sql + " limit " + (ps.getPageNumber() - 1) * ps.getPageSize() + "," + ps.getPageSize();		//start下标从0开始
		}
		else
			if ("Oracle".equalsIgnoreCase(FORP.DB_TYPE))
			{
				if (null != ps && -1 != ps.getPageSize())
					sql = "select * from (select t.*, RowNum RN from ("	+ sql + ") t) where RN>" +
							(ps.getPageNumber() - 1) * ps.getPageSize() + " and RN<=" + ps.getPageNumber() * ps.getPageSize();
			}

		lg.debug("分页查询SQL：{}", sql);
		List<T> rows = jdbc.query(sql, params, new ADBeanPropertyRowMapper<>(voClazz));
		page.setRows(rows);

		return page;
	}

	/**
	 * 通用前N条记录查询方法
	 * 
	 * @param sql			SQL语句
	 * @param params	查询条件
	 * @param topN		记录个数
	 * 
   * @return page
	 */
	protected SqlRowSet getTopNData(String sql, Object[] params, int topN) throws Exception
	{
		if ("MySQL".equalsIgnoreCase(FORP.DB_TYPE))
			sql = sql + " Limit 0," + topN;		//start下标从0开始
		else
			if ("Oracle".equalsIgnoreCase(FORP.DB_TYPE))
				sql = "Select RowNum, T.* From ("	+ sql + ") T Where RowNum<=" + topN;

		lg.debug("TopN查询SQL：{}", sql);
		return jdbc.queryForRowSet(sql, params);
	}

	//=================================================================
	//		日志
	//=================================================================	

	/**
	 * 记录系统操作日志
	 * 
	 * @param user		当前操作用户
	 * @param request	Http请求信息
	 * @param content	操作内容简述
	 * @param params	相关参数
	 * @param remark	备注
	 */
	public void writeSystemLog(User user, HttpServletRequest request, String content, String params, String remark)
	{
		try
		{
			Log log = new Log();

			Menu menu = JSON.parseObject(Redis.getString(FORP.CACHE_MENU + user.getCurrentModuleId(), null), Menu.class);
			if (null != menu)
			{
				log.setModuleId(menu.getId());
				// TODO 转换Full模块名称（考虑全局模块缓冲）
				// log.setModuleName();
			}

			log.setDomainId(user.getDomainId());
			log.setUserId(user.getId());
			log.setUserName(user.getUserName());
			log.setIpAddress(request.getRemoteAddr());
			log.setMachineName(request.getRemoteHost());
			log.setContent(content);
			log.setParameters(params);
			log.setRemark(remark);

			insertIntoTable(log, null, new String[]{"createDate", "loginName"});
		}
		catch (Exception e)
		{
			lg.error("系统日志记录失败：", e);
		}
	}

	/**
	 * 添加告警日志内容
	 * 
	 * @param category	日志类别：1 - 邮件；2 - 短信。
	 * @param level		  级别：1 - 警告；2 - 错误。
	 * @param content   告警内容
	 */
	protected void writeWarningLog(int category, int level, String content)
	{
		try
		{
			jdbc.update("insert into Forp_WarnLog (Category, LogLevel, Content, CreateDate) values(?, ?, ?, ?)",
						category, level, content.length() > 500 ? content.substring(0, 500) : content, new Date());
		}
		catch (Exception e)
		{
			lg.error("系统告警日志记录失败：", e);
		}
	}

	//=================================================================
	//		应用Cache
	//=================================================================	

	/**
	 * 加载域全局参数Cache
	 * 
	 * @param domainId	域ID
	 */
	public void loadDomainProfileCache(Long domainId) throws Exception
	{
		if ("deploy".equals(FORP.STATUS) && Redis.isCached(FORP.CACHE_DOMAIN_PROFILE + domainId, null))
			return;

		lg.info("加载域[{}]全局参数......", domainId);

		// 1 缓存根部门
		String sql = "select * from Forp_Dept where NodeNo='001' and FK_DomainID=" + domainId;
		lg.debug("SQL：" + sql);
		List<Department> rows = jdbc.query(sql, new ADBeanPropertyRowMapper<>(Department.class));
		Redis.cacheString(FORP.CACHE_ROOT_DEPT + domainId, JSON.toJSONString(rows.get(0)), null);

		// 2 缓存全局参数
		Map<String, String> params = new HashMap<>();
		sql = "select FK_DomainID, SN, Value from Forp_Parameter where Status=1 and FK_DomainID=" + domainId + " order by ID asc";
		lg.debug("SQL：{}", sql);
		SqlRowSet rs = jdbc.queryForRowSet(sql);
		while (rs.next())
		{
			lg.info("{}：{}--->{}", rs.getString("FK_DomainID"), rs.getString("SN"), StringUtils.defaultString(rs.getString("Value")));
			params.put(rs.getString("SN"), rs.getString("Value"));
		}
		Redis.cacheHashMap(FORP.CACHE_DOMAIN_PROFILE + domainId, params, null);
	}

	/**
	 * 加载指定用户的权限列表（合并多个Role中的权限）
	 * 
	 * @param userIds		用户编号列表
	 */
	protected void loadUserPopedomCache(Long[] userIds) throws Exception
	{
		String sql = "select v.FK_MenuID from V_Forp_RoleMenu v where v.FK_RoleID in " +
				"(select r.FK_RoleID from Forp_UserRole r where r.FK_UserID=?) order by v.NodeNo asc";
		SqlRowSet rs;
		List<Long> myPopedom ;
		for (Long id : userIds)
		{
			myPopedom = new ArrayList<>();

			rs = jdbc.queryForRowSet(sql, id);
			while (rs.next())
			{
				myPopedom.add(rs.getLong("FK_MenuID"));
			}

			lg.info("加载用户权限：{}-{}", id, myPopedom.size());
			Redis.cacheString(FORP.CACHE_USER_PERMISSION + id, JSON.toJSONString(myPopedom, SerializerFeature.WriteDateUseDateFormat), null);
			lg.info("同步CACHE：+[{}/{}]", id, FORP.CACHE_USER_PERMISSION + id);
		}
	}

	/**
	 * 加载模块列表Cache
	 * 
	 * @param domainId	域ID
	 * @return List<Menu>
	 */
	protected List<Menu> loadMenuCache(long domainId) throws Exception
	{
		List<Menu> rows = new ArrayList<>();
		// 1 所有模块细粒度权限
		loadMenuPrivligesCache(domainId);

		String sql = "select * from Forp_Menu where FK_DomainID=" + domainId + " order by FK_DomainID, NodeNo";
		lg.debug("SQL：{}", sql);
		SqlRowSet rs = jdbc.queryForRowSet(sql);
		Menu m;
		JedisPool pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");
		while (rs.next())
		{
			// 映射
			m = new Menu();
			rows.add(m);

			m.setDomainId(rs.getLong("FK_DomainID"));
			m.setId(rs.getLong("ID"));
			m.setName(rs.getString("Name"));
			m.setIcon(rs.getString("Icon"));
			m.setUrl(rs.getString("URL"));
			m.setNodeNo(rs.getString("NodeNo"));
			m.setParentNodeNo(rs.getString("ParentNodeNo"));

			// 细粒度权限
			if (Redis.isCached(FORP.CACHE_MENU_PRIVILEGE + m.getId(), pool))
			{
				m.setPrivileges(JSON.parseArray(Redis.getString(FORP.CACHE_MENU_PRIVILEGE + m.getId(), pool), MenuPrivilege.class));
			}

			if ("deploy".equals(FORP.STATUS) && Redis.isCached(FORP.CACHE_MENU + m.getId(), pool))
			{
				lg.warn("菜单[{}]以及子权限Cache已加载，忽略加载请求！", rs.getString("ID"));
			}
			else
				Redis.cacheString(FORP.CACHE_MENU + m.getId(), JSON.toJSONString(m, SerializerFeature.WriteDateUseDateFormat), pool);
		}

		return rows;
	}

	/**
	 * 加载所有模块的细粒度权限Cache
	 * 
	 * @param domainId	域ID
	 */
	protected void loadMenuPrivligesCache(long domainId)
	{
		Map<Long, List<MenuPrivilege>> tempCache = new HashMap<>();
		String sql = "select a.* from Forp_Menu_Privilege a, Forp_Menu b where a.FK_MenuID=b.ID and b.FK_DomainID=" + domainId;
		SqlRowSet rs = jdbc.queryForRowSet(sql);
//			// 清除上次缓存
//			while (rs.next())
//			{
//				cache.del(HuaYuIStudy.CACHE_MENU_PRIVILEGE + rs.getString("FK_MenuID"));
//			}
//
//			// 加载最新缓存
//			rs.beforeFirst();
		MenuPrivilege p;
		List<MenuPrivilege> privileges;
		while (rs.next())
		{
			if (tempCache.containsKey(rs.getLong("FK_MenuID")))
			privileges = tempCache.get(rs.getLong("FK_MenuID"));
			else
			{
				privileges = new ArrayList<>();
				tempCache.put(rs.getLong("FK_MenuID"), privileges);
			}

			// 缓存数据
			p = new MenuPrivilege();
			p.setId(rs.getLong("ID"));
//				p.setMenuId(rs.getLong("FK_MenuID"));
			p.setName(rs.getString("Name"));
			p.setSn(rs.getString("SN"));
			p.setStandardValue(rs.getInt("Value"));
			p.setUiType(rs.getInt("UIType"));

			privileges.add(p);
		}

		// 批量加入Cache
		Long menuId;
		JedisPool pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");
		for (Long aLong : tempCache.keySet())
		{
			menuId = aLong;
			privileges = tempCache.get(menuId);
			Redis.cacheString(FORP.CACHE_MENU_PRIVILEGE + menuId, JSON.toJSONString(privileges, SerializerFeature.WriteDateUseDateFormat), pool);
			lg.info("加载模块细粒度权限缓存：{}[{}]", menuId, privileges.size());
		}
	}

	//=================================================================
	//		通用方法
	//=================================================================	

	/**
	 * 重新排序所有Tree节点列表（同一个节点下按照指定属性列表顺序升序排序）
	 * 注意：Object对象必须有nodeNo和parentNodeNo属性
	 * 
	 * @param allNodes		  原始Tree节点列表
	 * @param result		    排序后的Tree节点列表
	 * @param parentNodeNo	父节点编号（1级节点父节点编号）
	 * @param cc			      排序规则
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected void reOrderTreeNodes(List<?> allNodes, List result, String parentNodeNo, ComparatorChain cc) throws Exception
	{
		// 当前节点下的直接子节点列表
		List<Object> sons = new ArrayList<>();

		for (Object node : allNodes)
		{
			if ((PropertyUtils.getSimpleProperty(node, "parentNodeNo")).equals(parentNodeNo))
				sons.add(node);
		}

		// 按照指定字段列表重新排序
		if (null != cc)
			Collections.sort(sons, cc);

		// 逐个节点处理儿子节点列表
		for (Object node : sons)
		{
			result.add(node);
			reOrderTreeNodes(allNodes, result, (String) PropertyUtils.getSimpleProperty(node, "nodeNo"), cc);
		}
	}

	/**
	 * 根据主键获取名称
	 * 
	 * @param table			  数据库表名称
	 * @param colunmName	列名
	 * @param id			    主键
	 * 
	 * @return 字段值
	 */
	protected String getValueById(String table, String colunmName, long id) throws Exception
	{
		String value = "未知";

		SqlRowSet rs = jdbc.queryForRowSet("select " + colunmName + " from " + table + " where ID=?", id);
		if (rs.next())
			value = rs.getString(colunmName);

		return value;
	}

	/**
	 * 获取指定表，指定树节点下的最大节点编号
	 *
	 * @param table				数据库表名
	 * @param nodeNo			节点编号
	 * @param nodeWidth		节点编号长度
	 * @param others			其它SQL限制条件
	 * 
	 * @return 节点编号
	 */
	protected String getMaxTreeNodeCode(String table, String nodeNo, int nodeWidth, String others)
	{
		lg.debug("---------->getMaxTreeNodeCode()");
		lg.debug("nodeCode:{}, levelWidth:{}", nodeNo, nodeWidth);
		
		SqlRowSet rs = jdbc.queryForRowSet("select NodeNo from " + table + " where ParentNodeNo=? " +
				(StringUtils.isBlank(others) ? "" : " and " + others) + " order by NodeNo DESC", nodeNo);
		String maxNodeCode;
		if (rs.next())
		{
			maxNodeCode = rs.getString("NodeNo");
			if (StringUtils.isNotBlank(maxNodeCode))
			{
        lg.debug("原编号:{}", maxNodeCode);
        int max = Integer.parseInt(maxNodeCode.substring(maxNodeCode.length() - nodeWidth)) + 1;
        maxNodeCode = maxNodeCode.substring(0, maxNodeCode.length() - nodeWidth) +
		                  StringUtils.leftPad(String.valueOf(max), nodeWidth, "0");
			}
			else
      {
				//当前节点下面的第一个子节点
	      maxNodeCode = nodeNo + StringUtils.leftPad(String.valueOf(1), nodeWidth, "0");
      }
		}
		else
		{
			maxNodeCode = nodeNo + StringUtils.leftPad(String.valueOf(1), nodeWidth, "0");
			lg.warn("Max检索条件返回结果为0！");
		}

    lg.debug("新节点编号:{}", maxNodeCode);
    lg.debug("<----------getMaxTreeNodeCode()\r\n");
    return maxNodeCode;
	}

	/**
	 * 获取完整树节点名称（根节点的名称由Cache中的根节点编号来决定，如果没有，则不拼接根节点名称）
	 * 
	 * @param treeNodeNameCache		树节点名称缓冲：key - nodeNo，value - name
	 * @param nodeNo				      目标节点编号
	 * @param nodeWidth           单级节点宽度
	 * @param seperator           分割符号
	 * 
	 * @return 完整树节点字符串
	 */
	protected String getFullTreeNodeName(Map<String, String> treeNodeNameCache, String nodeNo, int nodeWidth, String seperator) throws Exception
	{
		String result = "", tempNodeNo;

		for (int i = 0; i < nodeNo.length() / nodeWidth; i ++)
		{
			tempNodeNo = nodeNo.substring(0, (i + 1) * nodeWidth);

			if (treeNodeNameCache.containsKey(tempNodeNo))
				result += (result.length() > 0 ? seperator : "") + treeNodeNameCache.get(tempNodeNo);
		}

		return result;
	}
}