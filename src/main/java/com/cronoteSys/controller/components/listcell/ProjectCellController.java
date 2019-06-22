package com.cronoteSys.controller.components.listcell;

import java.io.File;
import java.io.IOException;

import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.util.SessionUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

public class ProjectCellController extends ListCell<ProjectVO> {
	@FXML
	private Label lblTitle;
	@FXML
	private Label lblProgress;
	@FXML
	private ProgressBar pgbProgress;
	@FXML
	private AnchorPane projectCardRoot;

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
			lblTitle.setText(item.getTitle());
			setGraphic(projectCardRoot);
		} else {
			setText(null);
			setGraphic(null);
		}
		setStyle("-fx-background-color:transparent;");
	}
}
