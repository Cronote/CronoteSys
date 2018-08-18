package com.cronoteSys.controller;

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

	/**
	 * @param sPreviewsScene the sPreviewsScene to set
	 */
	public void setsPreviewsScene(String sPreviewsScene) {
		this.sPreviewsScene = sPreviewsScene;
	}
}
