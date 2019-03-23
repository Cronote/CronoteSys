package com.cronoteSys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class Home2Controller {

	@FXML
	private Button btnActivity;
	@FXML
	private Button btnProject;
	@FXML
	private HBox root;

	public void btnActivityClicked() {

		clearPanes();

		Pane panel1 = new Pane();
		panel1.setStyle("-fx-border-color:red;-fx-border-width:1px;");
		panel1.setMaxWidth(350);
		Pane panel2 = new Pane();
		panel2.setStyle("-fx-border-color:green;-fx-border-width:1px;");

		root.getChildren().addAll(panel1, panel2);
		HBox.setHgrow(panel1, Priority.ALWAYS);
		HBox.setHgrow(panel2, Priority.ALWAYS);
		System.out.println("fim");
	}

	public void btnProjectClicked() {
		
		clearPanes();

		Pane panel1 = new Pane();
		panel1.setStyle("-fx-border-color:red;-fx-border-width:1px;");
		panel1.setMaxWidth(175);
		Pane panel2 = new Pane();
		panel2.setStyle("-fx-border-color:blue;-fx-border-width:1px;");
		panel2.setMaxWidth(175);
		Pane panel3 = new Pane();
		panel3.setStyle("-fx-border-color:green;-fx-border-width:1px;");

		root.getChildren().removeAll(panel1, panel2, panel3);
		root.getChildren().addAll(panel1, panel2, panel3);
		HBox.setHgrow(panel1, Priority.ALWAYS);
		HBox.setHgrow(panel2, Priority.ALWAYS);
		HBox.setHgrow(panel3, Priority.ALWAYS);
	}

	private void clearPanes() {
		while (root.getChildren().size() > 1)
			root.getChildren().remove(1);
	}
}
