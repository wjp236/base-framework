package com.platform.base.framework.trunk.model.mail;

import com.platform.base.framework.trunk.model.utils.BaseModel;

import java.util.Map;

/**
 * Mail 基础对象模型
 * 
 * @author QIANG
 *
 */
public class BaseMail  extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7157718749202027501L;

	/**
	 * 主题
	 */
	private String subject;

	/**
	 * 内容
	 */
	private String text;

	/**
	 * from
	 */
	private String from;

	/**
	 * to
	 */
	private String[] to;

	/**
	 * cc
	 */
	private String[] cc;

	/**
	 * bcc
	 */
	private String[] bcc;

	/**
	 * 附件地址
	 */
	private Map<String, String> paths;

	public String[] getCc() {
		return cc;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String[] getBcc() {
		return bcc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}

	public Map<String, String> getPaths() {
		return paths;
	}

	public void setPaths(Map<String, String> paths) {
		this.paths = paths;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("&主题:").append(subject).append("&to:").append(to).append("&paths:").append(paths);
		return builder.toString();
	}
}
