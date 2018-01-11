package com.platform.base.framework.trunk.core.dubbo.config;

import com.alibaba.dubbo.config.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Resource;

/**
 * dubbo基础配置
 */
@Configuration
@EnableConfigurationProperties(DubboProperties.class)
public class DubboConfig {

    @Resource
    private DubboProperties dubboProperties;

	@Bean
	public ApplicationConfig application() {
		ApplicationConfig applicationConfig = new ApplicationConfig(dubboProperties.getAppName());
		applicationConfig.setMonitor(monitor());
		applicationConfig.setRegistry(registry());
		return applicationConfig;
	}

	@Bean
	public RegistryConfig registry() {
		RegistryConfig registryConfig = new RegistryConfig(dubboProperties.getResAddress());
		registryConfig.setUsername(dubboProperties.getResUsername());
		registryConfig.setPassword(dubboProperties.getResPassowrd());
		registryConfig.setProtocol(dubboProperties.getProtocol());
		registryConfig.setPort(dubboProperties.getPort());
		registryConfig.setFile(dubboProperties.getFile());
		return registryConfig;
	}

	@Bean
	public ProtocolConfig protocol() {
		ProtocolConfig protocolConfig = new ProtocolConfig(dubboProperties.getProtocol(), dubboProperties.getPort());
		protocolConfig.setThreads(dubboProperties.getProtocolThreads());
		protocolConfig.setThreadpool(dubboProperties.getProtocolThreadpool());
		return protocolConfig;
	}

	@Bean
	public ProviderConfig provider() {
		ProviderConfig config = new ProviderConfig();
		config.setAccepts(dubboProperties.getAccepts());
		config.setConnections(dubboProperties.getConnections());
        config.setFilter(dubboProperties.getFilters());
        config.setTimeout(dubboProperties.getTimeout());
        config.setRetries(dubboProperties.getRetries());
        config.setVersion(dubboProperties.getVersion());
		return config;
	}

    @Bean
    public ConsumerConfig consumer() {
        ConsumerConfig config = new ConsumerConfig();
        config.setConnections(dubboProperties.getConnections());
        config.setFilter(dubboProperties.getFilters());
        config.setTimeout(dubboProperties.getTimeout());
        config.setRetries(dubboProperties.getRetries());
        config.setVersion(dubboProperties.getVersion());
        return config;
    }

	@Bean
	public MonitorConfig monitor() {
		MonitorConfig monitorConfig = new MonitorConfig();
		monitorConfig.setProtocol("registry");
		return monitorConfig;
	}
}
