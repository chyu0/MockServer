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

		Record result = new Record();
		if (wxDeveloper == null || StringUtils.isBlank(appId)) {
			renderJson(result);
			return;
		}

		Map<String, String> wxconfig = wxDeveloper.getWxConfigMap().get(appId);
		if (wxconfig == null) {
			renderJson(result);
			return;
		}

		String openId = wxconfig.get("openid");
		result.set("openid", openId != null ? openId : "");
		result.set("access_token", "efefs");
		renderJson(result);
	}

	public void authorize() {
		if (getPara("redirect_uri").contains("?")) {
			redirect(getPara("redirect_uri") + "&code=abcdefg|state=wx");
		} else {
			redirect(getPara("redirect_uri") + "?code=abcdefg|state=wx");
		}
	}

	public void userInfo() {
		String ip = MockServerUtil.getUserIp(this.getRequest());
		WxDeveloper developer = MockServerService.usermap.get(ip);

		if (developer == null) {
			renderJson("");
			return;
		}
		Map<String, String> userinfoMap = developer.getUserInfo();
		renderJson(userinfoMap);
	}
}
