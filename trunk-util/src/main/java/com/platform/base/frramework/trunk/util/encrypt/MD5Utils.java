package com.platform.base.frramework.trunk.util.encrypt;

/**
 * 对 apache commons-codec 包中DigestUtils类做扩展，增加了返回16进制大写字母的字符串形式MD5值
 */

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.NoSuchAlgorithmException;

/**
 * @Description: 计算数据data的MD5值，以16进制的字符串返回（大写字母）
 * @author tlla
 * @date 2016/11/30 15:52
 */
public class MD5Utils {

    public static String md5Upper(String data) throws NoSuchAlgorithmException {
        byte[] bytes = DigestUtils.md5(data);
        char[] chars = Hex.encodeHex(bytes, false);
        return new String(chars);
    }

}
