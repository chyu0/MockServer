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
	
	public static final Logger log=Logger.getLogger(MockServerService.class);
	public static Map<String, Map<String, String>> usermap = new ConcurrentHashMap<String, Map<String, String>>();
	private static long time=0L;
	/*
	 * 通过document来解析xml文件，并将结果以map形式表示出来
	 */
	private static Map<String, Map<String, String>> getConfigMap(Document document) throws DocumentException {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		if(document==null) {
			return map;	
		}
		Element wxusers=document.getRootElement();
		if (wxusers == null){
			return map;
		}
		List<Element> wxuserList = wxusers.elements();
		for (Element element : wxuserList) {
			String host = element.element("host").getText().trim();
			if (!StringUtils.isNotBlank(host)) {
				continue;
			}
			Element wxconfigs = element.element("wxconfigs");
			Map<String, String> configmap = new HashMap<String, String>();
			if (wxconfigs == null) {
				map.put(host, configmap);
				continue;
			}
			List<Element> allConfig = wxconfigs.elements("wxconfig");
			for (Element wxconfig : allConfig) {
				String appid = wxconfig.element("appid").getText().trim();
				String openid = wxconfig.element("openid").getText().trim();
				if(StringUtils.isNotBlank(appid)&&StringUtils.isNotBlank(openid)){
					configmap.put(appid, openid);
				}
			}
			map.put(host, configmap);
		}
		return map;
	}
	
	/*
	 * 解析config.xml文件，将返回的map赋值给静态变量usermap，
	 * 便于可以直接从内存中读xml文件的数据，而不需要重新解析
	 * 而time记录的是文件最后修改时间，只要没有解析xml文件time的值就不会改变
	 */
	public static void analyXml(){
		String path = MockServerService.class.getResource("/").getPath().toString();
		if(path.startsWith("file:/")){
			path=path.substring(6);
		}
		File config=new File(path+"config.xml");
		if(config.lastModified()>time){
			SAXReader reader=new SAXReader();
			log.info("xml文件被改变，已重新读取");
			try {
				Document document=reader.read(config);
				usermap=getConfigMap(document);
			} catch (DocumentException e) {
				log.error("读取xml文件出错了,程序终止"+e.getMessage());
				e.printStackTrace();
			}
		}
		time=config.lastModified();
	}


}
