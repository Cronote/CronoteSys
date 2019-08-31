package com.cronoteSys.controller.components.listcell;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.cronoteSys.filter.ActivityFilter;
import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.SessionUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class ProjectCellController extends ListCell<ProjectVO> {
	@FXML
	private Label lblTitle;
	@FXML
	private Label lblProgress;
	@FXML
	private Label lblTeamMembers;
	@FXML
	private Label lblActivityDoneTotal;
	@FXML
	private ProgressBar pgbProgress;
	@FXML
	private AnchorPane projectCardRoot;
	@FXML
	private StackPane stkColorTag;

	@Override
	public void updateSelected(boolean selected) {
		super.updateSelected(selected);
		if (selected) {
			projectCardRoot.getStyleClass().add("cardSelected");

		} else
			projectCardRoot.getStyleClass().removeAll("cardSelected");
	}

	@Override
	public void updateItem(ProjectVO item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null || !empty) {
			FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
			try {
				loader.setLocation(new File(getClass().getResource("/fxml/Templates/cell/ProjectCard.fxml").getPath())
						.toURI().toURL());
				loader.setController(this);
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			loadInfo(item);
			setGraphic(projectCardRoot);
		} else {
			setText(null);
			setGraphic(null);
		}
		setStyle("-fx-background-color:transparent;");
	}

	private void loadInfo(ProjectVO item) {
		lblTitle.setText(item.getTitle());
		if (item.getTeam() != null) {
			String color = item.getTeam() != null ? ScreenUtil.colorToRGBString(item.getTeam().getTeamColor())
					: "1,1,1,1";
			stkColorTag.setStyle("-fx-background-radius: 0 0 20 20;" + "-fx-background-color:rgba(" + color + ");");
			stkColorTag.setVisible(true);
		}else
			stkColorTag.setVisible(false);
		activitiesInfo(item);
		lblTeamMembers.setVisible(item.getTeam() != null);
		if (item.getTeam() != null) {
			lblTeamMembers.setText(String.valueOf(item.getTeam().getMembers().size() + 1));
		}

	}

	private void activitiesInfo(ProjectVO item) {
		List<ActivityVO> lst = new ActivityBO().listAll(new ActivityFilter(item.getId(), item.getUserVO().getIdUser()));
		Integer total = lst.size();
		Integer done = 0;
		for (ActivityVO ac : lst) {
			if (StatusEnum.itsFinalized(ac.getStats()))
				done++;
		}
		lblActivityDoneTotal.setText(done + "/" + total);
	}
}
