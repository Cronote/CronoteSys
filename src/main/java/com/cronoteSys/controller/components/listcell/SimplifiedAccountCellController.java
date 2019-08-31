package com.cronoteSys.controller.components.listcell;

import java.io.File;
import java.io.IOException;

import com.cronoteSys.model.vo.view.SimpleUser;
import com.cronoteSys.util.SessionUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class SimplifiedAccountCellController extends ListCell<SimpleUser> {
	@FXML
	private StackPane initialPnl;
	@FXML
	private AnchorPane pane;
	@FXML
	private Label lblUserInitial;
	@FXML
	private Label lblUsername;
	@FXML
	private Label lblUserEmail;
	@FXML
	private Tooltip ttpUserEmail;
	{
		FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
		try {
			loader.setLocation(new File(getClass().getResource("/fxml/Templates/cell/SimplifiedAccount.fxml").getPath())
					.toURI().toURL());
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateItem(SimpleUser item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null || !empty) {
			lblUserEmail.setText(item.getEmail());
			ttpUserEmail.setText(item.getEmail());
			lblUserInitial.setText(item.getCompleteName().substring(0, 1).toUpperCase());
			String[] userNames = item.getCompleteName().split(" ");
			String name = userNames.length > 1 ? userNames[0] + " " + userNames[(userNames.length - 1)] : userNames[0];
			lblUsername.setText(name);
			setGraphic(pane);
		} else {
			setText(null);
			setGraphic(null);
		}
		setStyle("-fx-background-color:transparent;");
	}

	@Override
	public void updateSelected(boolean selected) {
		super.updateSelected(selected);
		if (selected) {
			pane.getStyleClass().addAll("cardSelected");
		} else
			pane.getStyleClass().removeAll("cardSelected");
	}
}
