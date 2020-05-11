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
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
public class TopicFactory {
	private String username;
	private String password;
	private String url;
	private Connection connection;
	private Session session;
	private MessageConsumer consumer;
	public TopicFactory(String username,String password,String url) {
		// TODO Auto-generated constructor stub
		this.username=username;
		this.password=password;
		this.url= "failover:("+url+")"; 
	}
	/**
	 * 关闭服务
	 */
	public void close() {
		try {
			consumer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获取Topic消息
	 * @return
	 * @throws Exception
	 */
	public MessageConsumer CreateConsumerReceive(String topicName) throws Exception {
		// 第一步：创建一个ConnectionFactory对象。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.username,this.password,this.url);
		// 第二步：从ConnectionFactory对象中获得一个Connection对象。
		connection = connectionFactory.createConnection();
		// 第三步：开启连接。调用Connection对象的start方法。
		connection.start();
		// 第四步：使用Connection对象创建一个Session对象。
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。
		Topic topic = session.createTopic(topicName);
		// 第六步：使用Session对象创建一个Consumer对象。
		consumer = session.createConsumer(topic);
		return consumer;
	}
	
}
