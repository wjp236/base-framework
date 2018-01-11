package com.platform.base.frramework.trunk.util.encrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * base64加解密
 */
@SuppressWarnings("restriction")
public class Base64Util {

    private static final String UTF8 = "utf-8";

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
}
