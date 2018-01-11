package com.platform.base.framework.trunk.amq.handler;

import com.platform.base.framework.trunk.amq.core.MessageObj;
import com.platform.base.framework.trunk.amq.core.PooledConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class ProducerHandler {

	public static final Logger LOG = LoggerFactory.getLogger(ProducerHandler.class);

	/**
	 * 发送队列消息
	 * 
	 * @param message
	 * @param persisent 是否持久化
	 * @throws Exception
	 */
	@Deprecated
	public static void sendMessageByQueue(MessageObj message, boolean persisent) throws Exception {

		Session session = PooledConnectionUtils.createSession();
		try {
			Destination destination = session.createQueue(message.getDestination());
			MessageProducer producer = session.createProducer(destination);
			if (persisent) {
				producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			}
			ObjectMessage objMess = session.createObjectMessage(message);
			producer.send(objMess);
			LOG.info("send queue message success ,SendType is :{}", message.getSendType());
		} finally {
			session.close();
			LOG.info("close session connection .");
		}
	}

	/**
	 * 发送队列消息
	 * 
	 * @param message json
	 * @param persisent 是否持久化
	 * @throws Exception
	 */
	public static void sendMessageByQueue(String message, String dest, boolean persisent) throws Exception {

		Session session = PooledConnectionUtils.createSession();
		try {
			Destination destination = session.createQueue(dest);
			MessageProducer producer = session.createProducer(destination);
			if (persisent) {
				producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			}
			TextMessage objMess = session.createTextMessage(message);
			producer.send(objMess);
			LOG.info("消息推送：Queue:" + dest + " ,message:" + message);
		} finally {
			session.close();
			LOG.info("close session connection .");
		}
	}

	/**
	 * 发送TOPIC
	 * 
	 * @param message 消息封装体
	 * @param persisent 是否持久化
	 * @throws Exception
	 */
	@Deprecated
	public static void sendMessageByTopic(MessageObj message, boolean persisent) throws Exception {

		Session session = PooledConnectionUtils.createSession();
		try {
			Destination destination = session.createTopic(message.getDestination());
			MessageProducer producer = session.createProducer(destination);
			if (persisent) {
				producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			}
			ObjectMessage objMess = session.createObjectMessage(message);
			producer.send(objMess);
			LOG.info("send topic message success ,SendType is :{}", message.getSendType());
		} finally {
			session.close();
			LOG.info("close session connection .");
		}
	}

	/**
	 * 发送TOPIC
	 * 
	 * @param message 消息封装体
	 * @param persisent 是否持久化
	 * @throws Exception
	 */
	public static void sendMessageByTopic(String message, String dest, boolean persisent) throws Exception {

		Session session = PooledConnectionUtils.createSession();
		try {
			Destination destination = session.createTopic(dest);
			MessageProducer producer = session.createProducer(destination);
			if (persisent) {
				producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			}
			TextMessage objMess = session.createTextMessage(message);
			producer.send(objMess);
			LOG.info("消息推送：topic:" + dest + " ,message:" + message);
		} finally {
			session.close();
			LOG.info("close session connection .");
		}
	}

}
