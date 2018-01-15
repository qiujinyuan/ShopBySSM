package com.cdsxt.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisUtil {
	
	/**
	 * --------------------------------------------------------
	 */
	// Redis服务器IP
	private static String ADDR = "localhost";
	// Redis的端口号
	private static int PORT = 6379;
	// 密码:没有密码就设置为null
	private static String PASSWORD ;
	
	/**
	 * --------------------------------------------------------
	 */
	//超时信息
	private static int TIMEOUT = 30000;

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int MAX_IDLE = 200;
	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = true;

	// 连接池-单例对象
	private static JedisPool jedisPool = null;
	/**
	 * 初始化Redis连接池
	 */
	static {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(MAX_IDLE);
			config.setTestOnBorrow(TEST_ON_BORROW);

			jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取Jedis实例（连接池中）
	 */
	public  synchronized static Jedis getJedisFromPool() {
		try {
			Jedis resource =jedisPool.getResource();
			return resource;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获取Jedis实例（单个连接）
	 */
	public  static Jedis getJedisFromSingle() {
		try {
			Jedis resource = new Jedis(ADDR, PORT, TIMEOUT);
			// resource.auth(PASSWORD);
			return resource;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 释放jedis资源:使用 Jedis的close
	 */
	
}
