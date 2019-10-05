package com.cronoteSys.controller.components.dialogs;

import java.io.File;
import java.io.IOException;

import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXAlert;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.StageStyle;

public class DialogTeamCategoryController {
public void showDependencyManagerDialog() {
		
		try {
			FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
			loader.setLocation(
					new File(getClass().getResource("/fxml/Templates/dialogs/TeamProjectManager.fxml").getPath()).toURI()
							.toURL());
			Parent root = loader.load();
			JFXAlert<ActivityVO> alert = new JFXAlert<ActivityVO>();
			alert.setContent(root);
			alert.initStyle(StageStyle.UNDECORATED);
			alert.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
