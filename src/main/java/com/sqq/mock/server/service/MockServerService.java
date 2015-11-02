package com.sqq.mock.server.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class MockServerService {

	public static final Logger log = Logger.getLogger(MockServerService.class);
	public static Map<String, WxDeveloper> usermap = new ConcurrentHashMap<String,WxDeveloper>();
	private static long time = 0L;

	/*
	 * 通过document来解析xml文件，并将结果以map形式表示出来
	 */
	private static Map<String, WxDeveloper> getConfigMap(Document document) throws DocumentException {
		Map<String, WxDeveloper> map = new HashMap<String, WxDeveloper>();
		if (document == null) {
			return map;
		}
		Element wxusers = document.getRootElement();
		if (wxusers == null) {
			return map;
		}
		List<Element> wxuserList = wxusers.elements();
		for (Element element : wxuserList) {
			//<ip,<appid,openid>>
			Map<String, Map<String,String>> wxConfigMap=new HashMap<String,Map<String,String>>();
			WxDeveloper developer=new WxDeveloper();
			
			String username=element.element("username").getText();
			developer.setUserName(username);
			String hosts = element.element("host").getText();
			if (StringUtils.isBlank(hosts)) {
				continue;
			}
			
			// 支持一个用户多个IP地址<appid,openid>
			Map<String, String> openidConfig = new HashMap<String, String>();
			//用户映射<ip,userinfo>
			Map<String,String> userinfoMap=new HashMap<String,String>();
			
			//userinfo字符串
			Element userinfo=element.element("userinfo");
			userinfoMap.put("nickname", userinfo.elementText("nickname").trim());
			userinfoMap.put("sex",userinfo.elementText("sex").trim());
			userinfoMap.put("province",userinfo.elementText("province").trim());
			userinfoMap.put("city",userinfo.elementText("city").trim());
			userinfoMap.put("country",userinfo.elementText("country").trim());
			userinfoMap.put("headimgurl", userinfo.elementText("headimgurl").trim());
			developer.setUserInfo(userinfoMap);			
			
			String[] allHost=hosts.split(",");
			for(String host:allHost){
				wxConfigMap.put(host.trim(), openidConfig);
				map.put(host, developer);
			}
			
			Element wxconfigs = element.element("wxconfigs");
			if (wxconfigs == null) {
				continue;
			}
			
			// 封装appid和openId
			List<Element> allConfig = wxconfigs.elements("wxconfig");
			for (Element wxconfig : allConfig) {
				String appid = wxconfig.element("appid").getText().trim();
				String openid = wxconfig.element("openid").getText().trim();
				if (StringUtils.isNotBlank(appid) && StringUtils.isNotBlank(openid)) {
					openidConfig.put(appid, openid);
				}
			}
			developer.setWxConfigMap(wxConfigMap);
		}
		return map;
	}
	
	public static void analyXml() {
		String path = MockServerService.class.getResource("/").getPath().toString();
		if (path.startsWith("file:/")) {
			path = path.substring(6);
		}
		File config = new File(path + "config.xml");
		if (config.lastModified() > time) {
			log.info("config.xml was changed");
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(config);
				usermap = getConfigMap(document);
			} catch (DocumentException e) {
				log.error("read config.xml error!!!!!");
			}
		}
		time = config.lastModified();
	}

}
