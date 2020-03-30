package com.enjoy.traffic.deal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enjoy.traffic.dao.DataBaseDao;
import com.enjoy.traffic.dao.DataBaseDao2;
import com.enjoy.traffic.util.MqHelper;
@Component
public class WaterDeal {
	public WaterDeal() {
		// TODO Auto-generated constructor stub
		logger.getRootLogger().info("waterDeal init ....");
	}
	@Autowired
	private DataBaseDao dataBaseDao;
	@Autowired
	private DataBaseDao2 dataBaseDao2;
	private final Logger logger= LogManager.getLogger(this.getClass());
	public boolean dealWaterRecord(Map<String,String> map,DictionaryPool dictionaryPool,MqHelper mqHelper) {
		   try {
			   dataBaseDao2.save("");
			   return true;
		   } catch (Exception e) {
			   e.printStackTrace();
			   return false;
		   }
	}
}
