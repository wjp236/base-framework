/**
 * CopyRight 2016 必拓电子商务有限公司
 */
package com.platform.base.frramework.trunk.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class DESUtils {

	// 算法DESede
	private static final String Algorithm = "DESede";

	// 工作模式CBC(ECB)，填充模式PKCS5Padding(NoPadding)
	// eg: DESede/CBC/PKCS5Padding, DESede/ECB/PKCS5Padding
	private static final String Transformation = "DESede/CBC/PKCS5Padding";

	// 向量iv,ECB不需要向量iv，CBC需要向量iv
	// CBC工作模式下，同样的密钥，同样的明文，使用不同的向量iv加密 会生成不同的密文
	private static final String Iv = "\0\0\0\0\0\0\0\0";

	/**
	 * 3DES加密（加密后用十六进制处理）
	 * 
	 * @param key
	 *            - 加密密钥
	 * @param src
	 *            - 待加密明文
	 * @return - 加密后密文
	 * @throws UnsupportedEncodingException
	 */
	public String encryptWithHex(String key, String src)
			throws UnsupportedEncodingException {
		byte[] encodeByte = encryptMode(getKeyByte(key), src.getBytes());
		return byte2hex(encodeByte);
	}

	/**
	 * 3DES加密（加密后用Base64处理）
	 * 
	 * @param key
	 *            - 加密密钥
	 * @param src
	 *            - 待加密明文
	 * @return - 加密后密文
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("restriction")
	public String encryptWithBase64(String key, String src)
			throws UnsupportedEncodingException {
		byte[] destinate = encryptMode(getKeyByte(key), src.getBytes());
		return new sun.misc.BASE64Encoder().encode(destinate);
	}

	/**
	 * 3DES解密（针对十六进制处理的密文）
	 * 
	 * @param key
	 *            - 密钥
	 * @param encryptData
	 *            - 密文
	 * @return
	 */
	public String decrypt4Hex(String key, String encryptData) {
		byte[] keybyte = null;
		try {
			keybyte = getKeyByte(key);
		} catch (UnsupportedEncodingException e) {

		}

		byte[] src = hex2byte(encryptData.getBytes());
		return decryptMode(keybyte, src);
	}

	/**
	 * 3DES解密（针对Base64处理的密文）
	 * 
	 * @param key
	 *            - 密钥
	 * @param encryptData
	 *            - 密文
	 * @return
	 */
	@SuppressWarnings("restriction")
	public String decrypt4Base64(String key, String encryptData) {
		byte[] encryptByte = null;
		try {
			encryptByte = new sun.misc.BASE64Decoder()
					.decodeBuffer(encryptData);
		} catch (IOException e) {
            System.out.println(e.getMessage());
		}
		String clearText = null;
		try {
			clearText = decryptMode(getKeyByte(key), encryptByte);
		} catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
		}

		return clearText;
	}

	// 3DES加密
	private byte[] encryptMode(byte[] keybyte, byte[] src) {
		try {
			// 根据给定的字节数组和算法构造一个密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 加密`
			IvParameterSpec iv = new IvParameterSpec(Iv.getBytes());
			Cipher c1 = Cipher.getInstance(Transformation);
			c1.init(Cipher.ENCRYPT_MODE, deskey, iv);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	// 3DES解密
	private String decryptMode(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 解密
			IvParameterSpec iv = new IvParameterSpec(Iv.getBytes());
			Cipher c1 = Cipher.getInstance(Transformation);
			c1.init(Cipher.DECRYPT_MODE, deskey, iv);
			byte[] data = c1.doFinal(src);
			return new String(data);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	private String byte2hex(byte[] b) { // 一个字节的数，
		// 转成16进制字符串
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			// 整数转成十六进制表示
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs; // 转成大写
	}

	private byte[] getKeyByte(String key) throws UnsupportedEncodingException {
		// 加密数据必须是24位，不足补0；超出24位则只取前面的24数据
		byte[] data = key.getBytes();
		int len = data.length;
		byte[] newdata = new byte[24];
		System.arraycopy(data, 0, newdata, 0, len > 24 ? 24 : len);
		return newdata;
	}

	private byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}
}
