package com.platform.base.framework.trunk.amq.handler;

import com.platform.base.framework.trunk.amq.core.MultiThreadMessageListener;
import com.platform.base.framework.trunk.amq.core.SingleThreadMessageListener;
import com.platform.base.framework.trunk.amq.utils.SendType;
import com.platform.base.frramework.trunk.util.properties.PropertyConfigUtils;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class ConsumerHandler {

	public static final Logger LOG = LoggerFactory.getLogger(ConsumerHandler.class);

	private static String brokerUrl;

	private static String userName;

	private static String password;

	private Connection connection;

	private Session session;

	private Destination destination;
	// 队列名
	private String queue;

	private MessageHandler messageHandler;

	/**
	 * 初始化AMQ消费者
	 * 
	 * @param messageHandler
	 *            消息处理方式
	 * @param queueName
	 *            监听队列名称
	 */
	public ConsumerHandler(MessageHandler messageHandler, String queueName) {
		this.messageHandler = messageHandler;
		queue = queueName;
	}

	static {
		brokerUrl = PropertyConfigUtils.getProperty("trunk.amq.url");
		// userName =
		// PropertyConfigure.getContextProperty("trunk.amq.factor.username");
		// password =
		// PropertyConfigure.getContextProperty("trunk.amq.factor.password");
		LOG.info("初始化brokerUrl,brokerUrl, password");
	}

	/**
	 * 启动长连(单线程执行)
	 *
	 * @param sendType
	 *            判断是否为队列接收服务器(true is queue ,false is topic)
	 */
	public void startServer(SendType sendType) {

		try {
			MessageConsumer consumer = createConsumer(sendType);
			consumer.setMessageListener(new SingleThreadMessageListener(messageHandler));
		} catch (JMSException e) {
			LOG.error("connection broker error ,msg:{}", e.getMessage());
		}
	}

	/**
	 * 启动长连(异步多线程)
	 *
	 * @param sendType
	 *            判断是否为队列接收服务器(true is queue ,false is topic)
	 */
	public void startMultiThreadServer(SendType sendType) {

		try {
			MessageConsumer consumer = createConsumer(sendType);
			consumer.setMessageListener(new MultiThreadMessageListener(messageHandler));
		} catch (JMSException e) {
			LOG.error("connection broker error ,msg:{}", e.getMessage());
		}
	}

	/**
	 * 创建基础连接
	 *
	 * @param sendType
	 * @return
	 * @throws JMSException
	 */
	private MessageConsumer createConsumer(SendType sendType) throws JMSException {

		// ActiveMQ的连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, password, brokerUrl);
		connection = connectionFactory.createConnection();
		connection.start();
		// 会话采用非事务级别，消息到达机制使用自动通知机制
		session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		if (SendType.QUEUE == sendType) {
			destination = session.createQueue(queue);
		} else {
			destination = session.createTopic(queue);

		}
		MessageConsumer consumer = session.createConsumer(destination);
		consumer.setMessageListener(new MultiThreadMessageListener(messageHandler));
		return consumer;
	}

	/**
	 * 关闭连接
	 */
	public void shutdown() {

		try {
			if (session != null) {
				session.close();
				session = null;
			}
			LOG.info("close session");
			if (connection != null) {
				connection.close();
				connection = null;
			}
			LOG.info("close connection");
		} catch (Exception e) {
			LOG.error("close consumer failed. Exception:{}", e.getMessage());
		}
	}

}
