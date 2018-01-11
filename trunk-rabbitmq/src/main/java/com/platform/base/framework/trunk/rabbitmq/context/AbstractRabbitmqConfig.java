package com.platform.base.framework.trunk.rabbitmq.context;

import com.platform.base.framework.trunk.rabbitmq.config.RabbitmqProperties;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 *
 * 提供基础数据源功能
 *
 */
@Configuration
@EnableConfigurationProperties(RabbitmqProperties.class)
public abstract class AbstractRabbitmqConfig {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRabbitmqConfig.class);

	@Resource
	private RabbitmqProperties rabbitmqProperties;

	public ConnectionFactory initConnectionFactory(String addresses, String username, String password, String queueName) throws IOException {
	    return createConnectionFactory(addresses, username, password, queueName, rabbitmqProperties);
    }

    /**
     * 创建连接工厂
     * @param addresses
     * @param username
     * @param password
     * @param queueName
     * @param rabbitmqProperties
     * @return
     */
    private CachingConnectionFactory createConnectionFactory(
            String addresses,
            String username,
            String password,
            String queueName,
            RabbitmqProperties rabbitmqProperties
    ) throws IOException {
        logger.info("createConnectionFactory rabbitmq");
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(addresses);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);

        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(rabbitmqProperties.isTransactional());
        channel.queueDeclare(queueName,
                rabbitmqProperties.isDurable(),
                rabbitmqProperties.isExclueive(),
                rabbitmqProperties.isAutoDelete(), null);

        return connectionFactory;
    }

    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory, String queueName) throws IOException {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(queueName);
        return template;
    }

    public SimpleMessageListenerContainer simpleMessageListenerContainer(
            ConnectionFactory connectionFactory,
            ChannelAwareMessageListener messageListener, String queueName
    ) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        // 消息监听注册
        container.setMessageListener(new MessageListenerAdapter(messageListener));
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return container;
    }



}
