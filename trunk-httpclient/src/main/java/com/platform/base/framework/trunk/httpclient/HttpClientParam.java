package com.platform.base.framework.trunk.httpclient;

import java.util.Map;

/**
 * 通讯工具参数类
 * @author zhangding<zhangding@enn.com>
 */
public class HttpClientParam {

	/**
	 * 请求地址
	 */
	private String url;
	/**
	 * 请求参数
	 */
	private String requestParam;
	/**
	 * 请求参数(Map<String, String>)
	 */
	private Map<String, String> requestMapParam;
	/**
	 * 文件头
	 */
	private String mimeType;
	/**
	 * 连接超时时间,单位毫秒
	 */
	private int connectionTimeOut;
	/**
	 * 读取超时时间,单位毫秒
	 */
	private int readTimeOut;
	/**
	 * 编码格式
	 */
	private String encoding;
	/**
	 * 请求方法,GET或者POST
	 */
	private HttpMethodType httpMethodType;
	/**
	 * http类型,Https或者Http
	 */
	/**
	 * SSL证书路径
	 */
	private String keyUrl;
	/**
	 * SSL证书密码
	 */
	private String keyPassword;
	/**
	 * 是否忽略SSL验证
	 */
	private SSLType SSLType;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRequestParam() {
		return requestParam;
	}
	public void setRequestParam(String requestParam) {
		this.requestParam = requestParam;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public int getConnectionTimeOut() {
		return connectionTimeOut;
	}
	public void setConnectionTimeOut(int connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}
	public int getReadTimeOut() {
		return readTimeOut;
	}
	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public HttpMethodType getHttpMethodType() {
		return httpMethodType;
	}
	public void setHttpMethodType(HttpMethodType httpMethodType) {
		this.httpMethodType = httpMethodType;
	}
	public String getKeyUrl() {
		return keyUrl;
	}
	public void setKeyUrl(String keyUrl) {
		this.keyUrl = keyUrl;
	}
	public String getKeyPassword() {
		return keyPassword;
	}
	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}
	public SSLType getSSLType() {
		return SSLType;
	}
	public void setSSLType(SSLType sSLType) {
		SSLType = sSLType;
	}
	/**
	 * @return the requestMapParam
	 */
	public Map<String, String> getRequestMapParam() {
		return requestMapParam;
	}
	/**
	 * @param requestMapParam the requestMapParam to set
	 */
	public void setRequestMapParam(Map<String, String> requestMapParam) {
		this.requestMapParam = requestMapParam;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HttpClientParam [url=");
		builder.append(url);
		builder.append(", requestParam=");
		builder.append(requestParam);
		builder.append(", requestMapParam=");
		builder.append(requestMapParam);
		builder.append(", mimeType=");
		builder.append(mimeType);
		builder.append(", connectionTimeOut=");
		builder.append(connectionTimeOut);
		builder.append(", readTimeOut=");
		builder.append(readTimeOut);
		builder.append(", encoding=");
		builder.append(encoding);
		builder.append(", httpMethodType=");
		builder.append(httpMethodType);
		builder.append(", keyUrl=");
		builder.append(keyUrl);
		builder.append(", keyPassword=");
		builder.append(keyPassword);
		builder.append(", SSLType=");
		builder.append(SSLType);
		builder.append("]");
		return builder.toString();
	}
	
}
