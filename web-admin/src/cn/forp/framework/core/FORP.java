/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core;

import cn.forp.framework.core.thread.BaseThread;
import com.alibaba.media.MediaConfiguration;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 西点软件全局类
 *
 * @author	Bruce
 * @version	2016-4-19 22:20:37
 */
public class FORP
{
	//=================================================================
	//		全局常量
	//=================================================================

	/**
	 * 当前登录用户信息缓冲键值
	 */
	public final static String SESSION_USER = "session_user";
	/**
	 * MD5加密盐
	 */
	public final static String MD5_SALT_PREFIX = "Forp_";

	//=================================================================
	//		全局通用变量
	//=================================================================

	/**
	 * 当前web应用的名称（ROOT应用-""；其它－"/"开头的具体应用名称，末尾没有"/"符号）
	 */
	public static String WEB_APP_CONTEXT = null;
	/**
	 * 当前web应用的磁盘绝对路径
	 */
	public static String WEB_APP_PATH = null;
	/**
	 * Spring上下文对象
	 */
	public static ApplicationContext SPRING_CONTEXT = null;
	/**
	 * 系统运行状态：dev - 开发环境；deploy - 发布环境
	 */
	public static String STATUS = "dev";
	/**
	 * 附件存储引擎：file - 磁盘文件, mongodb - MongoDB，alibaba - 顽兔平台
	 */
	public static String ATTACHMENT_ENGINE = "file";
	/**
	 * 系统后台服务列表（key - 完整Class名称）
	 */
	public static Map<String, BaseThread> THREADS = new HashMap<String, BaseThread>();

	//=================================================================
	//		默认日期、时间格式
	//=================================================================

  /**
   * 日期格式
   */
  public final static String PATTERN_DATE = "yyyy-MM-dd";
  /**
   * 时间格式
   */
  public final static String PATTERN_TIME = "HH:mm:ss";
  /**
   * 日期时间格式
   */
  public final static String PATTERN_DATE_TIME = PATTERN_DATE + " " + PATTERN_TIME;

	//=================================================================
	//		Cache Key
	//=================================================================

	/**
	 * 用户权限：List<Long>
	 */
	public static final String CACHE_USER_PERMISSION = "user-permission:";
	/**
	 * 域参数：Map<String, String>
	 */
	public static final String CACHE_DOMAIN_PROFILE = "domain-profile:";
	/**
	 * 域根部门：Department
	 */
	public static final String CACHE_ROOT_DEPT = "domain-root-dept:";
	/**
	 * 菜单：Menu
	 * TODO 检查代码，确定Cache是否有意义；以域为单位粗粒度缓冲，方便优化加载性能（一个域一个List结构）。
	 */
	public static final String CACHE_MENU = "menu:";
	/**
	 * 菜单细粒度权限：List<MenuPrivilege>
	 * TODO 检查代码，确定Cache是否有意义
	 */
	public static final String CACHE_MENU_PRIVILEGE = "menu-privilege:";
	/**
	 * 临时用户头像：byte[]
	 */
	public static final String CACHE_TEMP_AVATAR = "avatar-temp:";

	//=================================================================
	//		Database
	//=================================================================

	/**
	 * 数据库类型
   */
	public static String DB_TYPE = null;
  /**
   * 数据库名称
   */
	public static String DB_NAME = null;
  /**
   * 数据库端口
   */
  public static String DB_PORT = null;
  /**
   * 数据库IP
   */
  public static String DB_IP = null;
  /**
   * 数据库用户名称
   */
  public static String DB_USER_NAME = null;
  /**
   * 数据库密码
   */
  public static String DB_PASSWORD = null;

	//=================================================================
	//		淘宝 - 顽兔
	//=================================================================

	/**
	 * 阿里百川 - 顽兔多媒体配置
	 */
	public static MediaConfiguration ALIBABA_MEDIA_CFG = null;
}