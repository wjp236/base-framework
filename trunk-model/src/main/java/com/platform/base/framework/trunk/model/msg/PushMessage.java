package com.platform.base.framework.trunk.model.msg;

import com.platform.base.framework.trunk.model.utils.BaseModel;

/**
 * 推送消息体
 */
public class PushMessage  extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5536019769146042130L;

	/**
	 * 唯一标识
	 */
	private String id;

	private String title;

	private String message;

	private String jsonObject;

	/**
	 * 类型
	 */
	private SendType sendType;

	public SendType getSendType() {
		return sendType;
	}

	public void setSendType(SendType sendType) {
		this.sendType = sendType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}

	@Override
	public String toString() {
		return "MessageBody [title=" + title + ", message=" + message + ", jsonObject=" + jsonObject + "]";
	}

}
