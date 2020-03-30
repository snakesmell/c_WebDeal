package com.enjoy.traffic.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

public class MqHelper{
	ConnectionFactory connectionFactory = null;  	
	Connection connection = null;	
	Session topicSession=null;	
	Map<String, MessageProducer> sendQueues = new ConcurrentHashMap<String, MessageProducer>();    
	/**
	 * 初始化创建链接
	 * @param user
	 * @param pw
	 * @param url
	 */
	public MqHelper(String user,String pw,String url) {
		// TODO Auto-generated constructor stub
	    try  
	    {  
	    	Logger.getRootLogger().info(user+pw+url);
	      connectionFactory = new ActiveMQConnectionFactory(user, pw, "failover:("+url+")");  
	      connection = connectionFactory.createConnection();
	      connection.start();
	      topicSession= connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
	      Logger.getRootLogger().info("mq connect success"+user+pw+url);
	    }  
	    catch (Exception e) {  
	      Logger.getRootLogger().error("mq connect error"+user+pw+url);
	      e.printStackTrace();  
	    }  
	}
	/**
	 * 发送消息	
	 * @param topic
	 * @param text
	 * @return
	 */
		public boolean sendTopicMessage(String topic,String text){
	    	boolean ret=false;
	    	try {  
	  	      TextMessage message =topicSession.createTextMessage(text);  
	  	      getTopicMessageProducer(topic).send(message);  
	  	      topicSession.commit();
	  	      ret=true;
	  	    }  
	  	    catch (JMSException e) {  
	  	      e.printStackTrace();  
	  	    }  
	    	return ret;
	    }
	/**
	 * 生产者
	 * @param topicName
	 * @return
	 */
	public MessageProducer getTopicMessageProducer(String topicName) {  
			//log.info("获取MQ订阅消费者");
	    if (sendQueues.containsKey(topicName)){
	    	//Logger.getRootLogger().info("producer have");
	    	return ((MessageProducer)sendQueues.get(topicName));  
	    }  
	    try  
	    {   
	    	Logger.getRootLogger().info("producer create");
	    	Destination destination = topicSession.createTopic(topicName);
	        //得到消息生成者【发送者】
	    	MessageProducer  producer = topicSession.createProducer(destination);
            //设置不持久化，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);	    
            sendQueues.put(topicName, producer);  
            return producer;  
	    } catch (JMSException e) {  
	      e.printStackTrace();  
	    }  	  
	    return ((MessageProducer)sendQueues.get(topicName));  
  }
	
	
	/**
     * 发送topic消息
     * @param queue
     * @param map
     * @param text
     * @return
     */
    public boolean sendTopicMessage(String queue,Map map,String text){
    	boolean ret=false;
    	try {  
  	      TextMessage message =topicSession.createTextMessage(text);  
  	      if(map!=null && map.size()>0){
  	    	Set keys = map.keySet( );         	      
  	    	if(keys != null) {         	    	    
  	    	    Iterator iterator = keys.iterator( );         	    	    
  	    	    while(iterator.hasNext( )) {         	    	    
  	    	        String key = iterator.next( ).toString();         	    	    
  	    	        String value = map.get(key).toString();       
  	    	        message.setStringProperty(key, value);
  	    	    }         	    	    
  	    	}      	    	  
  	      }
  	      getTopicMessageProducer(queue).send(message);  
  	      topicSession.commit();
  	      ret=true;
  	    }  
  	    catch (JMSException e) {  
  	      e.printStackTrace();  
  	    }  
    	return ret;
    }
	
}
