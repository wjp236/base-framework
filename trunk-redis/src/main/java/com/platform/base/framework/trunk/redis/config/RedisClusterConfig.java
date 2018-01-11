package com.platform.base.framework.trunk.redis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableConfigurationProperties(TrunkRedisProperties.class)
@ConditionalOnProperty(name = "cache.cluster")
public class RedisClusterConfig {

	private Logger LOG = LoggerFactory.getLogger(RedisClusterConfig.class);

	@Resource
	private TrunkRedisProperties redisProperties;

	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(redisProperties.getMaxTotal());
        jedisPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
		jedisPoolConfig.setMaxWaitMillis(redisProperties.getMaxWaitMillis());
		return jedisPoolConfig;

	}

	public RedisClusterConfiguration redisClusterConfiguration() {

		RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
		String[] hosts = redisProperties.getHostName().split(":");
		Set<RedisNode> redisNodes = new HashSet<>();
		redisNodes.add(new RedisClusterNode(hosts[0], Integer.valueOf(hosts[1])));
		redisClusterConfiguration.setClusterNodes(redisNodes);
		redisClusterConfiguration.setMaxRedirects(redisProperties.getMaxRedirects());
		return redisClusterConfiguration;

	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration(), jedisPoolConfig());
		if (!StringUtils.isEmpty(redisProperties.getPassword()))
			jedisConnectionFactory.setPassword(redisProperties.getPassword());
		return jedisConnectionFactory;
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setDefaultSerializer(new StringRedisSerializer());
		LOG.info("create RedisTemplate success");
		return redisTemplate;
	}
}
