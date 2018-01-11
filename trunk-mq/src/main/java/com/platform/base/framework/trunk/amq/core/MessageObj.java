package com.platform.base.framework.trunk.amq.core;



import com.platform.base.framework.trunk.amq.utils.SendType;

import java.io.Serializable;

public class MessageObj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 178951327459455475L;

	/**
	 * 发送类型
	 */
	private SendType sendType;

	/**
	 * 发送对象
	 */
	private Object obj;

	/**
	 * 队列名称
	 */
	private String destination;

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}


	public SendType getSendType() {
		return sendType;
	}

	public void setSendType(SendType sendType) {
		this.sendType = sendType;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}


}
