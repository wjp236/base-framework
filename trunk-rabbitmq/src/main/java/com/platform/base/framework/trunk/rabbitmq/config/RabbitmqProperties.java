package com.platform.base.framework.trunk.rabbitmq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitmqProperties {


    /**
     * 是否持久化
     */
    private boolean durable = true;

    /**
     * 是否独有
     */
    private boolean exclueive = false;

    /**
     * 通道是否有事务
     */
    private boolean transactional = false;

    /**
     * 是否自动删除
     */
    private boolean autoDelete = false;

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public boolean isExclueive() {
        return exclueive;
    }

    public void setExclueive(boolean exclueive) {
        this.exclueive = exclueive;
    }

    public boolean isTransactional() {
        return transactional;
    }

    public void setTransactional(boolean transactional) {
        this.transactional = transactional;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }
}
