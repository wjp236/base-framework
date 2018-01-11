package com.platform.base.frramework.trunk.util.property;

import java.util.*;

/**
 *  属性文件读取帮助类
 *  PropertyUtil 此处填写需要参考的类
 * @version 2015年5月21日 下午3:14:02
 * @author wangjp
 */
public class PropertyUtil {
	private static Map<String, PropertyUtil> instance = Collections.synchronizedMap(new HashMap<String, PropertyUtil>());
	private static Object lock = new Object();
	protected String sourceUrl;
	protected ResourceBundle resourceBundle;
	private static Map<String, String> convert = Collections.synchronizedMap(new HashMap<String,String>());

	protected PropertyUtil(String sourceUrl) {
		this.sourceUrl = sourceUrl;
		load();
	}

	public static PropertyUtil getInstance(String sourceUrl) {
		synchronized (lock) {
			PropertyUtil manager = (PropertyUtil) instance.get(sourceUrl);
			if (manager == null) {
				manager = new PropertyUtil(sourceUrl);
				instance.put(sourceUrl, manager);
			}
			return manager;
		}
	}

	private synchronized void load() {
		try {
			this.resourceBundle = ResourceBundle.getBundle(this.sourceUrl);
		} catch (Exception e) {
			throw new RuntimeException("sourceUrl = " + this.sourceUrl + " file load error!", e);
		}
	}

	public synchronized String getProperty(String key) {
		return this.resourceBundle.getString(key);
	}

	public Map<String, String> readyConvert() {
		Enumeration<?> enu = this.resourceBundle.getKeys();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			String value = this.resourceBundle.getString(key);
			convert.put(value, key);
		}
		return convert;
	}

	public Map<String, String> readyConvert(ResourceBundle resourcebundle) {
		Enumeration<?> enu = resourcebundle.getKeys();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			String value = resourcebundle.getString(key);
			convert.put(value, key);
		}
		return convert;
	}

	public static void main(String[] args) {
		PropertyUtil manager = getInstance("essc");
		System.out.println(manager.getProperty("system"));
	}
}