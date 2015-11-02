package com.sqq.mock.server.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.sqq.mock.server.service.MockServerService;
import com.sqq.mock.server.service.WxDeveloper;
import com.sqq.mock.server.util.MockServerUtil;

public class MockServerController extends Controller {

	public static final Logger log = Logger.getLogger(MockServerController.class);

	public void access_token() {

		String ip = MockServerUtil.getUserIp(this.getRequest());
		WxDeveloper wxDeveloper = MockServerService.usermap.get(ip);
		String appId = getPara("appid");
		
		Map<String, String> wxconfigMap =wxDeveloper.getWxConfigMap().get(appId);
		Record result = new Record();
		if (wxconfigMap == null || StringUtils.isBlank(appId)) {
			renderJson(result);
			return;
		}

		String openId = wxconfigMap.get(appId);
		result.set("openid", openId != null ? openId : "");
		renderJson(result);
	}

	public void authorize() {
		if (getPara("redirect_uri").contains("?")) {
			redirect(getPara("redirect_uri") + "&code=abcdefg|state=wx");
		} else {
			redirect(getPara("redirect_uri") + "?code=abcdefg|state=wx");
		}
	}
	
	public void userInfo(){
		Record result = new Record();
		result.set("openid", null);
		renderJson(result);
	}
}
