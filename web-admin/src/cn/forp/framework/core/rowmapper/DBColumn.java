/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.rowmapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DB O/R mapping annotation类
 *
 * @author	Bruce
 * @version	2016-4-1 18:03:45
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBColumn
{
	/**
	 * 数据库列名称
	 */
	String name() default "";

	/**
	 * 是否主键
	 */
	boolean isPrimaryKey() default false;
}