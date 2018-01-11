package com.platform.base.framework.trunk.amq.core;

import com.platform.base.frramework.trunk.util.properties.PropertyConfigUtils;
import com.platform.base.frramework.trunk.util.text.StringUtils;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnection;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Session;

public class PooledConnectionUtils {

	public static final Logger LOG = LoggerFactory.getLogger(PooledConnectionUtils.class);

	private static PooledConnectionFactory poolFactory;

	static {
		String url = PropertyConfigUtils.getProperty("trunk.amq.url");
		// String userName =
		// SourceUtils.getSimpleMessage("trunk.rabbitmq.factor.username");
		// String password =
		// SourceUtils.getSimpleMessage("trunk.rabbitmq.factor.password");
		String maxConnections = PropertyConfigUtils.getProperty("trunk.amq.pool.maxConnections", "100");
		String activeSessionPerConnection = PropertyConfigUtils.getProperty("trunk.amq.pool.activeSessionPerConnection",
				"200");
		String expirationCheckMillis = PropertyConfigUtils.getProperty("trunk.amq.pool.expirationCheckMillis", "10000");
		if (StringUtils.isEmpty(url)) {
			LOG.warn("Wraning: amq.factor.url is empty!!");
		}
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
		poolFactory = new PooledConnectionFactory(factory);
		// 池中借出的对象的最大数目
		poolFactory.setMaxConnections(Integer.valueOf(maxConnections));
		poolFactory.setMaximumActiveSessionPerConnection(Integer.valueOf(activeSessionPerConnection));
		// 后台对象清理时，休眠时间超过了10000毫秒的对象为过期
		poolFactory.setTimeBetweenExpirationCheckMillis(Integer.valueOf(expirationCheckMillis));
		LOG.info(" PooledConnectionFactory create success,maxConnections:[{}],activeSessionPerConnection:[{}]",
				maxConnections, activeSessionPerConnection);

	}

	/**
	 * 1.对象池管理connection和session,包括创建和关闭等
	 *
	 * @return * @throws JMSException
	 */
	public static Session createSession() {

		PooledConnection pooledConnection = null;
		try {
			pooledConnection = (PooledConnection) poolFactory.createConnection();
			// false 参数表示 为非事务型消息，后面的参数表示消息的确认类型（见4.消息发出去后的确认模式）
			return pooledConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (Exception e) {
			LOG.error("create session error !{}", e.getMessage());
			LOG.info("当前线程池状态 ,最大线程池数:[{}],当前线程池连接数:[{}],当前线程session数:[{}],总session数量:[{}]",
					poolFactory.getMaxConnections(), poolFactory.getNumConnections(),
					pooledConnection.getNumActiveSessions(), pooledConnection.getNumSessions());
		}
		return null;
	}

	/**
	 * 获得链接池工厂
	 */
	public static PooledConnectionFactory getPooledConnectionFactory() {
		return poolFactory;
	}

}
