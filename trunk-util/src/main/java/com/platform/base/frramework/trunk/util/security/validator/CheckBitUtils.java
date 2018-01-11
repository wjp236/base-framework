/**
 * CopyRight 2015 必拓电子商务有限公司
 */
package com.platform.base.frramework.trunk.util.security.validator;

/**
 * 校验位帮助类（加权取余算法）
 * @author John<wangcyg@enn.cn>
 */
public class CheckBitUtils {

	/**
	 * 不允许实例化
	 */
	private CheckBitUtils() {
		super();
	}

	/**
	 * 生成校验位（加权取余算法）
	 * @param source 原串（全数字的字符串）
	 * @return 0-9的数字
	 */
	public static long gen(String source) {
		long result = 0;
		char[] sequence = source.toCharArray();
		for (int i = 0; i < sequence.length; i++) {
			int position = Integer.parseInt(String.valueOf(sequence[i]));
			result += position << i;
		}
		// 余数R直接作为校验值。
		return result % 10L;
	}

	/**
	 * 添加校验位
	 * @param source 原串（全数字的字符串）
	 * @return 添加校验位后的字符串
	 */
	public static String add(String source) {
		return source + String.valueOf(gen(source));
	}

	/**
	 * 校验是否合法
	 * @param source 原串（全数字的字符串）
	 * @return true-合法 false-非法
	 */
	public static boolean check(String source) {
		int sourceLen = source.length() - 1;
		return gen(source.substring(0, sourceLen)) == Long.parseLong(source.substring(sourceLen));
	}
}
