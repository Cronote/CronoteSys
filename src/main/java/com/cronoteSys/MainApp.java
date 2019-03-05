
package com.cronoteSys;

import com.cronoteSys.util.ScreenUtil;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {

		System.setProperty("javax.xml.bind.JAXBContextFactory", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		new ScreenUtil().openNewWindow(null, "SLogin", false);
		//new ScreenUtil().openNewWindow(null, "SFacebookLogin", true, null);

	}

	public static void main(String[] args) {
		launch(args);

	}

}