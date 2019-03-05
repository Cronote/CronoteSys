package com.cronoteSys.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.cronoteSys.util.ScreenUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MasterController {

	private Stage thisStage;
	@FXML
	private Button btnBack;
	@FXML
	protected Pane pnlRoot;

	private String sPreviewsScene;

	public void btnBackClicked() {
		new ScreenUtil().openNewWindow(getThisStage(), sPreviewsScene, false);
	}

	protected Stage getThisStage() {
		thisStage = (Stage) pnlRoot.getScene().getWindow(); // get the stage
															// off this screen
		return thisStage;
	}

	public static Properties getProp() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream("./application.properties");
		props.load(file);
		return props;

	}

	public static void saveProp(Properties prop)throws IOException {
		FileOutputStream file = new FileOutputStream("./application.properties");
		prop.store(file, "");
	}

	/**
	 * @param sPreviewsScene the sPreviewsScene to set
	 */
	public void setsPreviewsScene(String sPreviewsScene) {
		this.sPreviewsScene = sPreviewsScene;
	}
}
