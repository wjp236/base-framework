package com.platform.base.framework.trunk.core.configure.auto.redis;

import com.platform.base.framework.trunk.redis.core.RedisAutoConfiguration;
import com.platform.base.framework.trunk.redis.utils.JedisClusterUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(JedisClusterUtils.class)
@Import(RedisAutoConfiguration.class)
public class RedisAutoConfig {

}
