
package com.cronoteSys;

import com.cronoteSys.util.ScreenUtil;

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

//		new ScreenUtil().openNewWindow(null, "Test", false);
		new ScreenUtil().openNewWindow(stage, "SLogin", false);
		// new ScreenUtil().openNewWindow(null, "SFacebookLogin", true, null);

	}

	public static void main(String[] args) {
		launch(args);
	}

}