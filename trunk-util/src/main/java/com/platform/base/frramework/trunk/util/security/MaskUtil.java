package com.platform.base.frramework.trunk.util.security;

/**
 * 对各种数据类型做掩码，即将字符串的中间部分用*替换
 * 
 * @author qijia
 * 
 */
public class MaskUtil {
	
	static String	ACCOUNT_REGEX	= "(\\w{6})(\\w+)(\\w{4})";
	static String	ID_REGEX		= "(\\w{6})(\\w+)(\\w{4})";
	static String	MOBILE_REGEX	= "(\\w{3})(\\w+)(\\w{4})";
	static String	EMAIL_REGEX		= "(\\w{3})(\\w+)(\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+)";
	
	/**
	 * 对账号做掩码
	 * 
	 * @param account
	 * @return
	 */
	static public String maskAccount(String account) {
		return account.replaceAll(ACCOUNT_REGEX, "$1******$3");
	}
	
	/**
	 * 对身份证做掩码
	 * 
	 * @param id
	 * @return
	 */
	static public String maskIdentity(String id) {
		if (id == null)
			return null;
		if (id.length() == 18) {
			return id.replaceAll(ID_REGEX, "$1********$3");
		} else {
			String regex = "(\\w{" + id.length() / 3 + "})(\\w+)(\\w{" + id.length() / 3 + "})";
			String replaceStr = "$1********$3";
			return id.replaceAll(regex, replaceStr);
		}
	}
	
	/**
	 * 对密码做掩码
	 * 
	 * @param password
	 * @return
	 */
	static public String maskPassword(String password) {
		return "******";
	}
	
	/**
	 * 对手机号做掩码
	 * 
	 * @param password
	 * @return
	 */
	static public String maskMobile(String mobile) {
		return mobile.replaceAll(MOBILE_REGEX, "$1****$3");
	}
	
	/**
	 * 对邮箱做掩码
	 * 
	 * @param password
	 * @return
	 */
	static public String maskEmail(String mobile) {
		return mobile.replaceAll(EMAIL_REGEX, "$1*******$3");
	}
}
