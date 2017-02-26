/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.rowmapper;

import cn.forp.framework.core.vo.ClassMapping;
import cn.forp.framework.core.vo.DBMappingProperty;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * JDBC同名规则O/R Mapping映射类<br/><br/>
 *          以Spring的BeanPropertyRowMapper类为基础，添加了以下功能优化：<br/>
 * 			(1) 添加Class cache功能，优化Class的属性和数据库字段对应的解析过程。<br/>
 * 			(2) 使用annotation机制来映射命名特殊的数据库字段。
 * 
 * @author	    Bruce
 * @version	    2016-4-1 18:03:07
 * @param <T>   业务类泛型
 */
public class ADBeanPropertyRowMapper<T> implements RowMapper<T>
{
	/**
	 * Logger
	 */
	private final static Logger lg = LoggerFactory.getLogger(ADBeanPropertyRowMapper.class);

	/**
   * Class映射字段列表缓冲
   * 		key    - Class完整包路径
   * 		value - O/R mapping信息
   */
	private static final Map<String, ClassMapping> Mapping_Class_Cache = new HashMap<>();
	/**
	 * Class中普通的非Mapping属性列表Cache
   * 		key    - Class完整包路径
   * 		value - 字段Cache列表（key - 数据库列名（大写）；value - 属性对象）
   */
	private static final Map<String, Map<String, PropertyDescriptor>> Mapping_Class_Normal_Property_Cache = new HashMap<>();
	/**
	 * Cache Lock
	 */
	private static final ReentrantLock Lock = new ReentrantLock();

	/**
	 * The class we are mapping to
	 */
	private Class<T> mappedClass;
	/**
	 * 当前查询结果集字段名称列表Cache
	 * 		key    - 字段索引
	 * 		value - 字段名称
	 */
	private Map<Integer, String> rsmdFieldCache = new HashMap<>();
	/**
	 * Whether we're strictly validating
	 */
	private boolean checkFullyPopulated = false;
	/**
	 * Whether we're defaulting primitives when mapping a null value
	 */
	private boolean primitivesDefaultedForNullValue = false;

//	/** Map of the fields we provide mapping for */
//	private Map<String, PropertyDescriptor> mappedFields;
//
//	/** Set of bean properties we provide mapping for */
//	private Set<String> mappedProperties;
//
//	/**
//	 * Create a new BeanPropertyRowMapper for bean-style configuration.
//	 * 
//	 * @see #setMappedClass
//	 * @see #setCheckFullyPopulated
//	 */
//	public BeanPropertyRowMapper(){}

	/**
	 * 构造方法
	 * 
	 * @param clazz		the class that each row should be mapped to
	 */
	public ADBeanPropertyRowMapper(Class<T> clazz)
	{
		this.mappedClass = clazz;
		initialize(mappedClass);
	}

//	/**
//	 * Create a new BeanPropertyRowMapper.
//	 *
//	 * @param clazz								The class that each row should be mapped to
//	 * @param checkFullyPopulated 	Whether we're strictly validating that all bean properties have been mapped from corresponding database fields
//	 */
//	public ADBeanPropertyRowMapper(Class<T> clazz, boolean checkFullyPopulated)
//	{
//		this.mappedClass = clazz;
////		this.mappedClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//		this.checkFullyPopulated = checkFullyPopulated;
//
//		initialize(mappedClass);
//	}

	/**
	 * 初始化指定Class的O/R Mapping信息
	 * 
	 * @param clazz		要Mapping的类
	 */
	private static void initialize(Class<?> clazz)
	{
		// 检测缓冲
		if (Mapping_Class_Cache.containsKey(clazz.getName()))
			return;

		Lock.lock();
		try
		{
			// Double check
			if (Mapping_Class_Cache.containsKey(clazz.getName()))
				return;

			// 映射规则
			ClassMapping mapping = new ClassMapping();

			// Table名称
			Annotation at;
			// Annotation文本
			String atText = clazz.getSimpleName();			// 默认为Class名称
			// 解析Table annotation配置信息
			if (clazz.getAnnotations().length > 0)
			{
				at = clazz.getAnnotations()[0];
				if (at instanceof DBTable)
					atText = ((DBTable) at).name();
			}
			mapping.setTableName(atText);
			lg.info("");
			lg.info(">>> {} --> {} <<<", clazz.getName(), mapping.getTableName());

			// Table字段
			Field field;
			DBColumn col;
			PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clazz);
			for (PropertyDescriptor pd : pds)
			{
				if (null == pd.getWriteMethod() || null == pd.getReadMethod())
				{
					// logger.warn("无效的类属性-" + pd.getName() + "，忽略该属性的数据库映射。");
					continue;
				}

				atText = pd.getName();		// 默认为当前Field名称
				try
				{
					// 解析annotation信息
					field = clazz.getDeclaredField(pd.getName());
					if (null != field.getAnnotations() && field.getAnnotations().length > 0)
					{
						at = field.getAnnotations()[0];
						if (at instanceof DBColumn)
						{
							col = (DBColumn) at;
							// 主键
							if (col.isPrimaryKey())
							{
								mapping.setPkProperty(pd.getName());
								mapping.setPkColumn(atText.toUpperCase());

//								// 主键不加入fields列表
//								continue;
							}
							else
							{
								// 普通映射字段
								if (StringUtils.isNotBlank(col.name()) && !"Null".equalsIgnoreCase(col.name()))
									atText = col.name();
								else
								{
									// name属性为空或者Null时表示save和update不映射该Property，但是查询时需要映射
									lg.debug("普通属性字段：{}", pd.getName());
									if (!Mapping_Class_Normal_Property_Cache.containsKey(clazz.getName()))
										Mapping_Class_Normal_Property_Cache.put(clazz.getName(), new HashMap<>());

									Mapping_Class_Normal_Property_Cache.get(clazz.getName()).put(pd.getName().toUpperCase(), pd);

									continue;
								}
							}
						}
					}
				}
				catch (Exception e)
				{
					lg.error("{}的o/r mapping annotation信息获取失败：{}", pd.getName(), e.getMessage());
				}

				// 数据库字段名称 --> VO属性
				lg.info(pd.getName() + " -> " + StringUtils.capitalize(atText));

				DBMappingProperty pp = new DBMappingProperty();
				pp.setName(pd.getName());
				pp.setType(pd.getPropertyType());
				mapping.addProperty(atText.toUpperCase(), pp);
			}
			lg.info("Primary Key：{}", mapping.getPkColumn());
			lg.info("");

			// 加入Cache
			Mapping_Class_Cache.put(clazz.getName(), mapping);
			// logger.info("添加" + clazz.getName() + "类O/R Mapping规则");
		}
		finally
		{
			Lock.unlock();
		}
	}

	/**
	 * 获取指定类的Mapping对应关系
	 */
	public static ClassMapping getClassMapping(Class<?> clazz)
	{
		// 确保该Class Mapping信息已正确解析
		initialize(clazz);
		return Mapping_Class_Cache.get(clazz.getName());
	}

	/**
	 * Extract the values for all columns in the current row（查询映射不限于DBColumn配置属性列表，不在配置范围内的属性根据名称相符原则进行处理）.
	 * 
	 * <p>Utilizes public setters and result set metadata.
	 * @see java.sql.ResultSetMetaData
	 */
	public T mapRow(ResultSet rs, int rowNumber) throws SQLException
	{
		// 实例化VO对象
		T rowObject = BeanUtils.instantiate(this.mappedClass);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(rowObject);

		// Class Mapping信息
		ClassMapping mappingRole = Mapping_Class_Cache.get(mappedClass.getName());

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		// Set<String> populatedProperties = (isCheckFullyPopulated() ? new HashSet<String>() : null);

		// VO属性名称
		String columnName, propertyName;
		Class<?> propertyClass;
		DBMappingProperty dbMP;
		PropertyDescriptor pd;
		// 普通非save和update映射属性
		Map<String, PropertyDescriptor> normalFieldCache = Mapping_Class_Normal_Property_Cache.get(this.mappedClass.getName());
		for (int index = 1; index <= columnCount; index++)
		{
			// 获取字段名称
			if (rsmdFieldCache.containsKey(index))
				 columnName = rsmdFieldCache.get(index);
			else
			{
				columnName = JdbcUtils.lookupColumnName(rsmd, index).toUpperCase();
				rsmdFieldCache.put(index, columnName);
			}

			// 根据Maping规则组织对象属性
			if (mappingRole.hasProperty(columnName))
			{
				dbMP = mappingRole.getProperty(columnName);
				propertyName = dbMP.getName();
				propertyClass = dbMP.getType();
			}
			else
				if (null != normalFieldCache && normalFieldCache.containsKey(columnName))
				{
					pd = normalFieldCache.get(columnName);
					propertyName = pd.getName();
					propertyClass = pd.getPropertyType();
				}
				else
					continue;		// 继续下一个属性

			try
			{
				// 获取column值
				Object value = getColumnValue(rs, index, propertyClass);
				// 设置VO属性
				bw.setPropertyValue(propertyName, value);
			}
			catch (NotWritablePropertyException ex)
			{
				throw new DataRetrievalFailureException("Unable to map column " + columnName + " to property " + propertyName, ex);
			}
		}

//		if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties))
//			throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields " +
//					"necessary to populate object of class [" + this.mappedClass + "]: " + this.mappedProperties);

		return rowObject;
	}

	/**
	 * Retrieve a JDBC object value for the specified column.
	 * <p>The default implementation calls
	 * {@link JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)}.
	 * Subclasses may override this to check specific value types upfront,
	 * or to post-process values return from <code>getResultSetValue</code>.
	 * 
	 * @param rs				The ResultSet of holding the data
	 * @param index				The column index
	 * @param propertyType		The bean property that each result object is expected to match (or <code>null</code> if none specified)
	 * 
	 * @return the Object value
	 * 
	 * @throws SQLException in case of extraction failure
	 * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)
	 */
	private Object getColumnValue(ResultSet rs, int index, Class<?> propertyType) throws SQLException
	{
		return JdbcUtils.getResultSetValue(rs, index, propertyType);
	}

	/**
	 * Set whether we're strictly validating that all bean properties have been
	 * mapped from corresponding database fields.
	 * <p>Default is <code>false</code>, accepting unpopulated properties in the
	 * target bean.
	 */
	public void setCheckFullyPopulated(boolean checkFullyPopulated)
	{
		this.checkFullyPopulated = checkFullyPopulated;
	}

	/**
	 * Return whether we're strictly validating that all bean properties have been
	 * mapped from corresponding database fields.
	 */
	public boolean isCheckFullyPopulated()
	{
		return this.checkFullyPopulated;
	}

	/**
	 * Set whether we're defaulting Java primitives in the case of mapping a null value from corresponding database fields.
	 * 
	 * <p>Default is <code>false</code>, throwing an exception when nulls are mapped to Java primitives.
	 */
	public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue)
	{
		this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
	}

	/**
	 * Return whether we're defaulting Java primitives in the case of mapping a null value from corresponding database fields.
	 */
	public boolean isPrimitivesDefaultedForNullValue()
	{
		return primitivesDefaultedForNullValue;
	}
}