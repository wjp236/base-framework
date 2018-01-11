package com.platform.base.framework.trunk.redis.core;

import com.platform.base.framework.trunk.redis.config.RedisClusterConfig;
import com.platform.base.framework.trunk.redis.config.RedisSentinelConfig;
import com.platform.base.framework.trunk.redis.utils.JedisClusterUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ RedisClusterConfig.class, RedisSentinelConfig.class, JedisClusterUtils.class })
public class RedisAutoConfiguration {

}
