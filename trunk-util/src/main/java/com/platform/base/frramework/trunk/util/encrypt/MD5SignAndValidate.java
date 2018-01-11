package com.platform.base.frramework.trunk.util.encrypt;

import java.util.*;
import java.util.Map.Entry;

public class MD5SignAndValidate {

	/**
	 * 对Map数据进行MD5签名
	 * 
	 * @param data
	 * @param key
	 * @return Map<String, String>
	 */
	public static Map<String, String> signData(Map<String, String> data,
			String key) {

		Entry<String, String> obj = null;
		Map<String, String> submitFromData = new HashMap<String, String>();
		for (Iterator<Entry<String, String>> it = data.entrySet().iterator(); it
				.hasNext();) {
			obj = (Entry<String, String>) it.next();
			String value = obj.getValue();
			if (value != null && !value.equals("")) {
				// 对value值进行去除前后空处理
				submitFromData.put(obj.getKey(), value.trim());
			}
		}

		String coverMap2String = coverMap2String(submitFromData);
		String hmacSign = DigestUtil.hmacSign(coverMap2String, key);

		submitFromData.put("signature", hmacSign);

		return submitFromData;
	}

	/**
	 * 将Map数据转换成key=value的形式按照名称排序，然后以&作为连接符拼接成待签名串
	 * 
	 * @param data
	 * @return String
	 */
	public static String coverMap2String(Map<String, String> data) {
		TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = (Entry<String, String>) it
					.next();
			if (!"signature".equals(((String) en.getKey()).trim())) {
				tree.put(en.getKey(), en.getValue());
			}
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = (Entry<String, String>) it
					.next();
			sf.append(new StringBuilder().append((String) en.getKey())
					.append("=").append((String) en.getValue()).append("&")
					.toString());
		}

		return sf.substring(0, sf.length() - 1);
	}

	/**
	 * 对Map数据进行MD5验签
	 * 
	 * @param data
	 * @param key
	 * @return boolean
	 */
	public static boolean validateData(Map<String, String> data, String key) {

		String signature = data.get("signature");

		if (signature == null) {
			return false;
		}

		String coverMap2String = coverMap2String(data);
		String hmacSign = DigestUtil.hmacSign(coverMap2String, key);

		return hmacSign.equals(signature);
	}

	/**
	 * 生成长度为length的随机字符串
	 * 
	 * @param length
	 * @return String
	 */
	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabccdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

}
