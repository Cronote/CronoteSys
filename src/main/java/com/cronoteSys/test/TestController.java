package com.cronoteSys.test;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import com.cronoteSys.test.ActivityListController.btnAddActivityClickedI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class TestController implements Initializable {

	@FXML
	HBox root;

	private FXMLLoader loadTemplate(String template) {
		FXMLLoader root = null;

		System.out.println("template>>>> " + template);
		root = new FXMLLoader(getClass().getResource("/fxml/Templates/" + template + ".fxml"));
		return root;
	}

	private HBox recoverRoot(Node node) {
		if (root != null) {
			return root;
		}
		while (node.getParent() != null) {
			node = node.getParent();
		}
		return root = (HBox) node;
	}

	private void clearRoot(boolean removeMenu, Node node) {
		int letIndex = removeMenu ? 0 : 1;
		while (root.getChildren().size() > letIndex) {
			root.getChildren().remove(letIndex);
		}
	}

	private Node addNode(FXMLLoader fxml) {
		try {
			Node node = (Node) fxml.load();
			root.getChildren().add(node);
			return node;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void addNode(Node node) {
		root.getChildren().add((Node) node);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXMLLoader menu = loadTemplate("Menu_short");
		menu.setController(new MenuController());
		addNode(menu);
		ActivityListController.addBtnAddActivityClickedListener(new btnAddActivityClickedI() {

			@Override
			public void btnAddActivityClicked(String newScreen, HashMap<String, Object> hmap) {
				if (root.getChildren().size() > 2)
					root.getChildren().remove(2);
				System.out.println("hueuehue ouvindo");
				FXMLLoader detailsFxml = loadTemplate("ThirdPanel");
				Node nodeDetails = addNode(detailsFxml);
				root.setHgrow(nodeDetails, Priority.ALWAYS);

			}
		});

	}

	class MenuController implements Initializable {
		@FXML
		ToggleButton btnActivity;
		@FXML
		ToggleButton btnProject;
		@FXML
		ToggleButton btnReport;

		public void btnActivityClicked(ActionEvent e) {
			clearRoot(false, (Node) e.getSource());
			if (btnActivity.isSelected()) {
				FXMLLoader p = loadTemplate("ActivityList");
				try {
					root.getChildren().addAll((Node) p.load());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		public void btnProjectClicked(ActionEvent e) {
			// TODO: implementar
			clearRoot(false, (Node) e.getSource());
		}

		public void btnReportClicked(ActionEvent e) {
			// TODO: implementar
			clearRoot(false, (Node) e.getSource());
		}

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			btnActivity.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					btnActivityClicked(event);

				}
			});
			btnProject.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					btnProjectClicked(event);

				}
			});
			btnReport.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					btnReportClicked(event);

				}
			});
		}
	}

}
