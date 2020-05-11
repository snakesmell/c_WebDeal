package com.enjoy.traffic.redisManger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enjoy.traffic.util.Common;

import redis.clients.jedis.Jedis;
@Component
@Scope("singleton")
public class RedisFactory {
	private RedisUtil ru=null;
	private static List<RedisUtil> list=new ArrayList<RedisUtil>();
	public RedisFactory(){
		//读取配置文件
    	String num=(String) Common.getProperties().get(Common.RedisNum);
		for(int i=0;i<Integer.parseInt(num);i++){
			list.add(new RedisUtil());
		}
	}
	/**
	 * @param i 获取第几个redis
	 * @return
	 */
	public static Jedis gerListRedis(int i){
		i=i-1;
		RedisUtil redis = list.get(i);
		return redis.getJedis(); 
	}
	public RedisUtil getRedis(){
		if(ru==null){
			return new RedisUtil();
		}else{
			return ru;
		}
	}
}
