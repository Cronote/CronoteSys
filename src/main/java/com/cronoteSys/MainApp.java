
package com.cronoteSys;

import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.SessionUtil;
import com.cronoteSys.util.guice.GuiceModule;
import com.google.inject.Guice;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

	@Override
	public void stop() throws Exception {
		super.stop();
		System.exit(0);
	}

	@Override
	public void start(Stage stage) throws Exception {
		SessionUtil.setInjector(Guice.createInjector(new GuiceModule()));
		// new ScreenUtil().openNewWindow(null, "Test", false);
		ScreenUtil.openNewWindow(stage, "SLogin", false);
		// new ScreenUtil().openNewWindow(null, "SFacebookLogin", true, null);

	}

	public static void main(String[] args) {
		launch(args);
	}

}