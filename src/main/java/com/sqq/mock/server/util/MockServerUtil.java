package com.sqq.mock.server.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

public class MockServerUtil {
	/**
	 * 获取用户ip地址
	 */
	public static String getUserIp(HttpServletRequest request) {
		String ip = "127.0.0.1";
		ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.equals("") || "unknow".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.equals("") || "unknow".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.equals("") || "unknow".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (ip.equals("127.0.0.1")) {
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
					ip = inet.getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
			if (ip.equals("0:0:0:0:0:0:0:1")) {
				ip = "127.0.0.1";
			}
		}
		// 如果有多个代理服务器，就会有多个ip地址，而只有第一个才是用户本地ip地址
		if (ip != null && ip.length() > 15) {
			return ip.split(",")[0];
		}
		return ip;
	}
}
