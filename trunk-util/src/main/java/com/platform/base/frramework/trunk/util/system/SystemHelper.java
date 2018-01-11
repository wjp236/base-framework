package com.platform.base.frramework.trunk.util.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 
 * 本机系统信息
 * 
 * @author wp
 * 
 */
public final class SystemHelper {
	private static Logger logger = LoggerFactory.getLogger(SystemHelper.class);

	// 获得系统属性集
	public static Properties props = System.getProperties();
	// 操作系统名称
	public static String OS_NAME = getPropertery("os.name");

	/**
	 * 
	 * 根据系统的类型获取本服务器的ip地址
	 * 
	 * InetAddress inet = InetAddress.getLocalHost(); 但是上述代码在Linux下返回127.0.0.1。
	 * 主要是在linux下返回的是/etc/hosts中配置的localhost的ip地址，
	 * 而不是网卡的绑定地址。后来改用网卡的绑定地址，可以取到本机的ip地址：）：
	 * 
	 * @throws UnknownHostException
	 */
	public static InetAddress getSystemLocalIp() throws UnknownHostException {
		InetAddress inet = null;
		String osname = getSystemOSName();
		try {
			// 针对window系统
			if (osname.equalsIgnoreCase("Windows 10")) {
				inet = getWinLocalIp();
				// 针对linux系统
			} else if (osname.equalsIgnoreCase("Linux")) {
				inet = getUnixLocalIp();
			}
			if (null == inet) {
				logger.error("主机的ip地址未知");
				inet = InetAddress.getByName("127.0.0.1");
			}
		} catch (SocketException e) {
			logger.error("获取本机ip错误" + e.getMessage());
			inet = InetAddress.getByName("127.0.0.1");
		}
		return inet;
	}

	/**
	 * 获取FTP的配置操作系统
	 * 
	 * @return String
	 */
	public static String getSystemOSName() {
		// 获得系统属性集
		Properties props = System.getProperties();
		// 操作系统名称
		String osname = props.getProperty("os.name");
		if (logger.isDebugEnabled()) {
			logger.info("the ftp client system os Name " + osname);
		}
		return osname;
	}

	/**
	 * 获取属性的值
	 * 
	 * @param propertyName
	 * @return String
	 */
	public static String getPropertery(String propertyName) {
		return props.getProperty(propertyName);
	}

	/**
	 * 获取window 本地ip地址
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	private static InetAddress getWinLocalIp() throws UnknownHostException {
		InetAddress inet = InetAddress.getLocalHost();
		System.out.println("本机的ip=" + inet.getHostAddress());
		return inet;
	}

	/**
	 * 
	 * 可能多多个ip地址只获取一个ip地址 获取Linux 本地IP地址
	 * 
	 * @return
	 * @throws SocketException
	 */
	private static InetAddress getUnixLocalIp() throws SocketException {
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				logger.info("DisplayName:" + ni.getDisplayName());
				logger.info("Name:" + ni.getName());
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					logger.info("IP:" + ips.nextElement().getHostAddress());
					return ips.nextElement();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 
	 * 获取当前运行程序的内存信息
	 * 
	 * @return String
	 */
	public static final String getRAMinfo() {
		Runtime rt = Runtime.getRuntime();
		return "RAM: " + rt.totalMemory() + " bytes total, " + rt.freeMemory()
				+ " bytes free.";
	}
}
