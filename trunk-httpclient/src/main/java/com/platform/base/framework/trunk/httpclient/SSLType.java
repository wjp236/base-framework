package com.platform.base.framework.trunk.httpclient;

/**
 * SSL类型
 * (这里用一句话描述这个类的作用)
 * @author zhangding<zhangding@enn.com>
 */
public enum SSLType {

	/**
	 * 不使用SSL
	 */
	NOSSL,
	/**
	 * 忽略SSL
	 */
	IGNORESSL,
	/**
	 * 使用SSL证书进行校验
	 */
	VERIFYSSL;
}
