package com.enjoy.traffic.test;

import com.enjoy.traffic.redisManger.RedisUtil;

import redis.clients.jedis.Jedis;

public class TestRedis {
	private static Jedis jedis;
	public static void main(String[] args) {
		RedisUtil ru=new RedisUtil();
		jedis = ru.getJedis();
		set("water27","张三",10);
		set("water28","李四",20);
		set("water29","王五",30);
		
	}
	
	
	public static String set(String key, String value, int cacheSeconds) {
		String result = null;
		try {
			jedis.rpush(key,value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
