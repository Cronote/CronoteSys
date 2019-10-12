package com.cronoteSys.controller.components.dialogs;

import java.io.File;
import java.io.IOException;

import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXAlert;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.StageStyle;

public class ProjectTeamDialog {

	public void showProjectTeamManagerDialog() {

		try {
			FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
			loader.setLocation(
					new File(getClass().getResource("/fxml/Templates/dialogs/TeamProjectManager.fxml").getPath())
							.toURI().toURL());
			Parent root = loader.load();
			DialogProjectTeamController controller = loader.getController();
			JFXAlert<ActivityVO> alert = new JFXAlert<ActivityVO>();
			alert.setContent(root);
			alert.initStyle(StageStyle.UNDECORATED);
			alert.showAndWait();
			selectedTeam = controller.getSelectedTeam();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private TeamVO selectedTeam;

	public TeamVO getSelectedTeam() {
		return selectedTeam;
	}

}
