package com.cronoteSys.controller.components.listcell;

import java.io.File;
import java.io.IOException;

import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.SessionUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;

public class TeamCellController extends ListCell<TeamVO> {

	@FXML
	private AnchorPane teamCardRoot;
	@FXML
	private Label lblTeamName;
	@FXML
	private StackPane stkTeamColor;
	@FXML
	private Label lblProjectsCount;
	@FXML
	private Label lblMembersCount;

	@Override
	public void updateSelected(boolean selected) {
		super.updateSelected(selected);
		if (selected) {
			teamCardRoot.getStyleClass().addAll("cardSelected");
		} else
			teamCardRoot.getStyleClass().removeAll("cardSelected");
	}

	@Override
	public void updateItem(TeamVO item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null || !empty) {
			FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
			try {
				loader.setLocation(new File(getClass().getResource("/fxml/Templates/cell/TeamCard.fxml").getPath())
						.toURI().toURL());
				loader.setController(this);
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			lblTeamName.setText(item.getName());
			lblProjectsCount.setText("0");
//			lblMembersCount.setText(String.valueOf(item.getMembers().size()+1));
			String rgba = item.getTeamColor() != null ? ScreenUtil.colorToRGBString(item.getTeamColor()) : "1,1,1,1";
			stkTeamColor.setStyle(stkTeamColor.getStyle().concat("-fx-background-color:rgba(" + rgba + ");"));
			setGraphic(teamCardRoot);
		} else {
			setText(null);
			setGraphic(null);
		}
		setStyle("-fx-background-color:transparent;");
	}
}
