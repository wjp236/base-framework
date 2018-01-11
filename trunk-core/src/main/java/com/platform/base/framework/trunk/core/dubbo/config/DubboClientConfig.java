/**
 * 
 */
package com.platform.base.framework.trunk.core.dubbo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * dubbo客户端配置
 * @author wangjp<wangjp@enn.com>
 *
 */
@Configuration
@ImportResource("classpath:dubbo-client.xml")
public class DubboClientConfig {

}
