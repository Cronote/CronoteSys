package com.cronoteSys;

import com.cronoteSys.util.ScreenUtil;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		new ScreenUtil().openNewWindow(null, "SLogin", false);
	}

	
	public static void main(String[] args) {
		launch(args);

	}

}