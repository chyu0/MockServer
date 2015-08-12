package com.sqq.mock.server.jfinal;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.sqq.mock.server.controller.MockServerController;
import com.sqq.mock.server.service.MockServerService;

public class MockServerConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
		me.setDevMode(true);
	}

	@Override
	public void configRoute(Routes me) {
		// TODO Auto-generated method stub
		me.add("/oauth2", MockServerController.class);
	}

	@Override
	public void configPlugin(Plugins me) {

	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterJFinalStart() {
		Timer timer = new Timer();
		final Logger log=Logger.getLogger(MockServerConfig.class.getName());
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				MockServerService.analyXml();
				log.info("调度analyXml");
			}

		}, new Date(), 20 * 1000);
	}

}
