package com.cronoteSys.controller.components.listcell;

import java.io.File;
import java.io.IOException;

import com.cronoteSys.model.vo.SimpleActivity;
import com.cronoteSys.util.SessionUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

public class SimpleActivityCellController extends ListCell<SimpleActivity> {

	@FXML
	Label lblTitle;
	@FXML
	AnchorPane activityCardRoot;
	@FXML
	Label lblCategory;

	{
		FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
		try {
			loader.setLocation(
					new File(getClass().getResource("/fxml/Templates/cell/SimpleActivityCell.fxml").getPath()).toURI()
							.toURL());
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void updateSelected(boolean selected) {
		super.updateSelected(selected);
		if (selected) {
			activityCardRoot.getStyleClass().add("cardSelected");
		} else
			activityCardRoot.getStyleClass().removeAll("cardSelected");

	}
	@Override
	protected void updateItem(SimpleActivity item, boolean empty) {
		// TODO Auto-generated method stub
		super.updateItem(item, empty);
		if (item != null || !empty) {
			lblTitle.setText(item.getTitle());
			lblCategory.setText(item.getCategoryVO().getDescription());
			setGraphic(activityCardRoot);
		} else {
			setGraphic(null);
		}
		setStyle("-fx-background-color:transparent;");

	}

}
