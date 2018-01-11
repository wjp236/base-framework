package com.platform.base.framework.trunk.core.configure.auto.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jmx.support.ConnectorServerFactoryBean;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;

/**
 * JMX监控
 * 
 * @author QIANG
 *
 */
@Configuration
public class JmxAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(JmxAutoConfiguration.class);

	@Value("${jmx.rmi.host:localhost}")
	private String rmiHost;

	@Value("${jmx.rmi.port:60099}")
	private Integer rmiPort;

	@Bean
	public RmiRegistryFactoryBean rmiRegistry() {
		final RmiRegistryFactoryBean rmiRegistryFactoryBean = new RmiRegistryFactoryBean();
		rmiRegistryFactoryBean.setPort(rmiPort);
		rmiRegistryFactoryBean.setAlwaysCreate(true);
		LOGGER.info("RmiRegistryFactoryBean create success !!");
		return rmiRegistryFactoryBean;
	}

	@Bean
	@DependsOn("rmiRegistry")
	public ConnectorServerFactoryBean connectorServerFactoryBean() throws Exception {
		final ConnectorServerFactoryBean connectorServerFactoryBean = new ConnectorServerFactoryBean();
		connectorServerFactoryBean.setObjectName("connector:name=rmi");
		connectorServerFactoryBean.setServiceUrl(
				String.format("service:jmx:rmi://%s:%s/jndi/rmi://%s:%s/jmxrmi", rmiHost, rmiPort, rmiHost, rmiPort));
		LOGGER.info("ConnectorServerFactoryBean create success !!");
		return connectorServerFactoryBean;
	}
}