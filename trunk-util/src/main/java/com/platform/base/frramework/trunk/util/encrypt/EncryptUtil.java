package com.platform.base.frramework.trunk.util.encrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加解密工具类 这类工具类原则上在联调和测试阶段出現问题就不可能上线, So, 不标记抛出异常
 */
public class EncryptUtil {

	private static final String UTF8 = "utf-8";
	private static final String DES = "DESede";
	// 定义 加密算法,可用 DES,DESede,Blowfish
	private static final String ALGORITHM_DESEDE = "DESede";

	/**
	 * MD5数字签名
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String md5(String src) {
		// 定义数字签名方法, 可用：MD5, SHA-1
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] b = md.digest(src.getBytes(UTF8));
			return byte2HexStr(b);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * BASE64编码
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String base64Encoder(String src) {
		BASE64Encoder encoder = new BASE64Encoder();
		try {
			return encoder.encode(src.getBytes(UTF8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * BASE64解码
	 * 
	 * @param dest
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static String base64Decoder(String dest) {
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			return new String(decoder.decodeBuffer(dest), UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 3DES加密
	 * 
	 * @param src
	 * @param key
	 * @return
	 */
	public static String desedeEncoder(String src, String key) {
		SecretKey secretKey;
		try {
			secretKey = new SecretKeySpec(build3DesKey(key), ALGORITHM_DESEDE);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DESEDE);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] b = cipher.doFinal(src.getBytes(UTF8));
			return byte2HexStr(b);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 3DES解密
	 * 
	 * @param dest
	 * @param key
	 * @return
	 */
	public static String desedeDecoder(String dest, String key) {
		try {
			SecretKey secretKey = new SecretKeySpec(build3DesKey(key), ALGORITHM_DESEDE);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DESEDE);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] b = cipher.doFinal(str2ByteArray(dest));
			return new String(b, UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字节数组转化为大写16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2HexStr(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			String s = Integer.toHexString(b[i] & 0xFF);
			if (s.length() == 1) {
				sb.append("0");
			}
			sb.append(s.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 字符串转字节数组
	 * 
	 * @param s
	 * @return
	 */
	private static byte[] str2ByteArray(String s) {
		int byteArrayLength = s.length() / 2;
		byte[] b = new byte[byteArrayLength];
		for (int i = 0; i < byteArrayLength; i++) {
			byte b0 = (byte) Integer.valueOf(s.substring(i * 2, i * 2 + 2), 16).intValue();
			b[i] = b0;
		}
		return b;
	}

	/**
	 * 构造3DES加解密方法key
	 * 
	 * @param keyStr
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	private static byte[] build3DesKey(String keyStr) {
		try {
			byte[] key = new byte[24];
			byte[] temp;
			temp = keyStr.getBytes(UTF8);
			if (key.length > temp.length) {
				System.arraycopy(temp, 0, key, 0, temp.length);
			} else {
				System.arraycopy(temp, 0, key, 0, key.length);
			}
			return key;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
