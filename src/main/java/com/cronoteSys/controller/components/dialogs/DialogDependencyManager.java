package com.cronoteSys.controller.components.dialogs;

import java.io.File;
import java.io.IOException;

import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DialogDependencyManager {
	public DialogDependencyManager(ActivityVO selected) {
		this.selected= selected;
	}

	public void showDependencyManagerDialog() {
		
		try {
			FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
			loader.setLocation(
					new File(getClass().getResource("/fxml/Templates/dialogs/DependencyManager.fxml").getPath()).toURI()
							.toURL());
			Parent root = loader.load();
			DialogDependencyManagerController controller =loader.getController();
			controller.setSelectedActivity(selected);
			JFXAlert<ActivityVO> alert = new JFXAlert<ActivityVO>();
			alert.setContent(root);
			alert.initStyle(StageStyle.UNDECORATED);
			alert.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private ActivityVO selected;

}
