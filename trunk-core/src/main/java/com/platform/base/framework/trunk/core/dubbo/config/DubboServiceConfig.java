package com.platform.base.framework.trunk.core.dubbo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * dubbo提供者配置
 * @author John
 */
@Configuration
@ImportResource("classpath:/dubbo-service.xml")
public class DubboServiceConfig {

}
