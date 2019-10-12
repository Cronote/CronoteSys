package com.cronoteSys.controller.components.listcell;

import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.util.ScreenUtil;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class SimplifiedTeamCellController extends ListCell<TeamVO> {

	Label lblName = new Label();
	HBox root = new HBox();
	Pane colorPnl = new Pane();

	{
		root.getStyleClass().addAll("pane");
		root.setSpacing(5.0);
		colorPnl.setPrefWidth(5);

		lblName.setFont(new Font("System", 15));
		lblName.getStyleClass().addAll("letters_box_icons");
		root.getChildren().addAll(colorPnl, lblName);
	}

	@Override
	protected void updateItem(TeamVO item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null || !empty) {
			lblName.setText(item.getName());
			colorPnl.setStyle("-fx-background-color:rgba(" + ScreenUtil.colorToRGBString(item.getTeamColor()) + ")");
			setGraphic(root);
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			setAlignment(Pos.CENTER);
			getStyleClass().addAll("themed-list-cell");
		} else {
			setGraphic(null);
			setText(null);
		}
		setStyle("-fx-background-color:transparent;");
	}
}
