/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.util;

import cn.forp.framework.core.FORP;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Redis工具类
 *
 * @author	Bruce
 * @version	2016-5-21 15:08:37
 */
public class Redis
{
	/**
	 * Log4j logger
	 */
	private final static Logger lg = LoggerFactory.getLogger(Redis.class);

  /**
   * 缓存json字符串
   *
   * @param key	  键值
   * @param json	json格式内容
   * @param pool	redis连接池
   */
  public static void cacheString(String key, String json, JedisPool pool)
  {
    cacheString(key, -1, json, pool);
  }

  /**
   * 缓存byte[]
   *
   * @param key	  键值
   * @param bytes	字节数组
   * @param pool	redis连接池
   */
  public static void cacheByteArray(String key, byte[] bytes, JedisPool pool)
  {
    cacheByteArray(key, -1, bytes, pool);
  }

  /**
   * 缓存json字符串
   *
   * @param key			      键值
   * @param expireSeconds	过期秒数（-1 - 永不过期）
   * @param json			    json格式内容
   * @param pool			    redis连接池
   */
  public static void cacheString(String key, int expireSeconds, String json, JedisPool pool)
  {
    if (null == pool)
      pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");

		Jedis cache = pool.getResource();
		try
		{
			if (-1 == expireSeconds)
				cache.set(key, json);
			else
				cache.setex(key, expireSeconds, json);
		}
    finally
    {
      cache.close();
    }
  }

  /**
   * 缓存byte[]
   *
   * @param key			      键值
   * @param expireSeconds	过期秒数（-1 - 永不过期）
   * @param bytes			    字节数组
   * @param pool			    redis连接池
   */
  public static void cacheByteArray(String key, int expireSeconds, byte[] bytes, JedisPool pool)
  {
    if (null == pool)
      pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");

		Jedis cache = pool.getResource();
		try
		{
			if (-1 == expireSeconds)
				cache.set(key.getBytes(), bytes);
			else
				cache.setex(key.getBytes(), expireSeconds, bytes);
    }
    finally
    {
      cache.close();
    }
  }

  /**
   * 是否已缓存Cache对象
   *
   * @param key	  缓存键值
   * @param pool	redis连接池
   */
  public static boolean isCached(String key, JedisPool pool)
  {
    if (null == pool)
      pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");

		Jedis cache = pool.getResource();
		try
		{
      return cache.exists(key);
    }
    finally
    {
      cache.close();
    }
  }

  /**
   * 获取Cache字符串
   *
   * @param key	  缓存键值
   * @param pool	redis连接池
   */
  public static String getString(String key, JedisPool pool)
  {
    if (null == pool)
      pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");

		Jedis cache = pool.getResource();
		try
		{
			return cache.get(key);
    }
    finally
    {
      cache.close();
    }
  }

  /**
   * 获取Cache字节数组
   *
   * @param key	  缓存键值
   * @param pool	redis连接池
   */
  public static byte[] getByteArray(String key, JedisPool pool)
  {
    if (null == pool)
      pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");

		Jedis cache = pool.getResource();
		try
		{
      return cache.get(key.getBytes());
    }
    finally
    {
      cache.close();
    }
  }

  /**
   * 获取Cache对象
   *
   * @param key	  缓存键值
   * @param pool	redis连接池
   */
  public static JSONObject getJSONObject(String key, JedisPool pool)
  {
    if (null == pool)
      pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");

		Jedis cache = pool.getResource();
		try
		{
      return JSONObject.parseObject(cache.get(key));
    }
    finally
    {
      cache.close();
    }
  }

  /**
   * 获取Cache数组对象
   *
   * @param key	  缓存键值
   * @param pool	redis连接池
   */
  public static JSONArray getJSONArray(String key, JedisPool pool)
  {
    if (null == pool)
      pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");

		Jedis cache = pool.getResource();
		try
		{
      return JSONArray.parseArray(cache.get(key));
    }
    finally
    {
      cache.close();
    }
  }

  /**
   * 删除Cache对象
   *
   * @param key	  缓存键值
   * @param pool	redis连接池
   */
  public static void delete(String key, JedisPool pool)
  {
    if (null == pool)
      pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");

		Jedis cache = pool.getResource();
		try
		{
			cache.del(key);
    }
    finally
    {
      cache.close();
    }
  }

	//=================================================================
	//		HashMap
	//=================================================================

	/**
	 * 缓存HashMap
	 *
	 * @param key     键值
	 * @param field   属性
	 * @param value   属性值
	 * @param pool    redis连接池
	 */
	public static void cacheHashMap(String key, String field, String value, JedisPool pool)
	{
		Map<String, String> fields = new HashMap<>();
		fields.put(field, value);

		cacheHashMap(key, -1, fields, pool);
	}

	/**
	 * 缓存HashMap
	 *
	 * @param key   键值
	 * @param map   属性值列表
	 * @param pool  redis连接池
	 */
	public static void cacheHashMap(String key, Map<String, String> map, JedisPool pool)
	{
		cacheHashMap(key, -1, map, pool);
	}

	/**
	 * 缓存json字符串
	 *
	 * @param key			      键值
	 * @param expireSeconds	过期秒数（-1 - 永不过期）
	 * @param map			      属性值列表
	 * @param pool			    redis连接池
	 */
	public static void cacheHashMap(String key, int expireSeconds, Map<String, String> map, JedisPool pool)
	{
		if (null == pool)
			pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");

		Jedis cache = pool.getResource();
		try
		{
			cache.hmset(key, map);

			if (-1 != expireSeconds)
				cache.expire(key, expireSeconds);
		}
		finally
		{
			cache.close();
		}
	}

	/**
	 * 获取Cache对象
	 *
	 * @param key	  缓存键值
	 * @param pool	redis连接池
	 */
	public static Map<String, String> getAllHashMap(String key, JedisPool pool)
	{
		if (null == pool)
			pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");

		Jedis cache = pool.getResource();
		try
		{
			return cache.hgetAll(key);
		}
		finally
		{
			cache.close();
		}
	}

	/**
	 * 获取Cache对象
	 *
	 * @param key     缓存键值
	 * @param pool    redis连接池
	 * @param fields  属性列表
	 * @return List<String> 属性值列表
	 */
	public static List<String> getHashMap(String key, JedisPool pool, String... fields)
	{
		if (null == pool)
			pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");

		Jedis cache = pool.getResource();
		try
		{
			return cache.hmget(key, fields);
		}
		finally
		{
			cache.close();
		}
	}

	//=================================================================
	//		分布式锁
	//=================================================================

  /**
   * 获取分布式锁
   *
   * @param lockKey			      锁名称
   * @param autoUnLockSeconds	自动解锁秒数（默认60秒）
   * @return Jedis			      解锁的缓存对象
   */
  public static Jedis lock(String lockKey, int autoUnLockSeconds) throws Exception
  {
    JedisPool pool = ((JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool"));
    Jedis cache = pool.getResource();
    while (true)
    {
      if (1 == cache.setnx(lockKey, "1"))
      {
        // 防止异常后出现死锁
        cache.expire(lockKey, autoUnLockSeconds);

        lg.debug("分布式锁获取成功：{}", lockKey);
        break;
      }
      else
      {
        // 10毫秒后再重试
        Thread.sleep(10);
      }
    }

    return cache;
  }

  /**
   * 释放分布式锁
   *
   * @param cache		缓存对象
   * @param lockKey	锁名称
   */
  public static void unlock(Jedis cache, String lockKey)
  {
    lg.debug("释放分布式锁：{}", lockKey);
		try
		{
			cache.del(lockKey);
    }
    finally
    {
      cache.close();
    }
  }

	//=================================================================
	//		自增/递减
	//=================================================================

	/**
	 * +1自增键值数值
	 *
	 * @param key			缓存键值
	 * @param key			过期秒数
	 * @param pool		redis连接池
	 */
	public static Long increase(String key, int seconds, JedisPool pool)
	{
		if (null == pool)
			pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");

		Long newId = null;
		Jedis cache = pool.getResource();
		try
		{
			newId = cache.incr(key);

			// 创建键值 - 设置过期日期
			if (1 == newId && -1 != seconds)
				cache.expire(key, seconds);
		}
		finally
		{
			cache.close();
		}

		lg.debug("[{}]自增至{}", key, newId);
		return newId;
	}

	/**
	 * -1递减键值数值
	 *
	 * @param key			缓存键值
	 * @param pool		redis连接池
	 */
	public static Long decrease(String key, JedisPool pool)
	{
		if (null == pool)
			pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");

		Long newId = null;
		Jedis cache = pool.getResource();
		try
		{
			newId = cache.decr(key);
		}
		finally
		{
			cache.close();
		}

		lg.debug("[{}]递减至{}", key, newId);
		return newId;
	}
}