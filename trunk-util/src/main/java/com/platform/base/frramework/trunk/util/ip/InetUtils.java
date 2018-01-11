package com.platform.base.frramework.trunk.util.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Enumeration;

/**
 *  网络工具
 *  InetUtils 此处填写需要参考的类
 * @version 2015年7月14日 上午10:05:03
 * @author John<wangcyg@enn.cn>
 */
public class InetUtils {

    private static final Logger logger = LoggerFactory.getLogger(InetUtils.class);

    /**
     *  获取本机用户名
     * @return String
     */
    public static String getLocalHostName() {

        String hostName = "";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("", e);
        }
        return hostName;
    }

    /**
     *  获取本机IP
     * @return String
     */
    public static String getLocalIp() {

        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("", e);
        }
        return ip;
    }

    /**
     *  获取本机网卡IP地址
     * @return String
     */
    public static String getLocalIpByNetCard() {

        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
                .getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        String s = ip.getHostAddress();
                        if ("127.0.0.1".equals(s)) {
                            continue;
                        }
                        return ip.getHostAddress();
                    }
                }
            }
            return "127.0.0.1";
        } catch (SocketException e) {
            return "127.0.0.1";
        }
    }
}
