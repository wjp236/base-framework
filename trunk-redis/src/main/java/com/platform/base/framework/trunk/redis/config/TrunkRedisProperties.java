package com.platform.base.framework.trunk.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "redis")
public class TrunkRedisProperties {

	/**
	 * Max number of "idle" connections in the pool. Use a negative value to
	 * indicate an unlimited number of idle connections.
	 */
	private int maxIdle = 10;

	/**
	 * 最大连接数
	 */
	private int maxTotal = 500;

	private int maxWaitMillis = 3000;

	private String hostName = "localhost";

	private String password;

	/**
	 * Maximum number of redirects to follow when executing commands across the
	 * cluster.
	 */
	private int maxRedirects = 10;

	private String masterName;


	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxRedirects() {
		return maxRedirects;
	}

	public void setMaxRedirects(int maxRedirects) {
		this.maxRedirects = maxRedirects;
	}

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }
}
