package com.platform.base.framework.trunk.redis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.HashSet;

@Configuration
@EnableConfigurationProperties(TrunkRedisProperties.class)
@ConditionalOnProperty(name = "cache.sentinel")
public class RedisSentinelConfig {

	private static final Logger LOG = LoggerFactory.getLogger(RedisSentinelConfig.class);

	@Resource
	private TrunkRedisProperties redisProperties;

	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
		jedisPoolConfig.setMaxTotal(redisProperties.getMaxTotal());
		jedisPoolConfig.setMaxWaitMillis(redisProperties.getMaxWaitMillis());
		return jedisPoolConfig;

	}

	public RedisSentinelConfiguration jedisSentinelConfig() {
		String[] hosts = redisProperties.getHostName().split(",");
		HashSet<String> sentinelHostAndPorts = new HashSet<>();
		for (String hn : hosts) {
			sentinelHostAndPorts.add(hn);
		}
		return new RedisSentinelConfiguration(redisProperties.getMasterName(), sentinelHostAndPorts);

	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {

		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisSentinelConfig(),
				jedisPoolConfig());
		if (!StringUtils.isEmpty(redisProperties.getPassword()))
			jedisConnectionFactory.setPassword(redisProperties.getPassword());
		return jedisConnectionFactory;
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate() {

		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setDefaultSerializer(new StringRedisSerializer());
		LOG.info("create redisTemplate success");
		return redisTemplate;
	}

}
