package com.cronoteSys.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.cronoteSys.controller.ShowEditViewActivityObservable.ShowEditViewActivityI;
import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.ActivityBO.OnActivityDeletedI;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class HomeController implements Initializable {

	@FXML
	protected HBox root;
	private HashMap<String, Object> hmp;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXMLLoader menu = loadTemplate("Menu_large");
		menu.setController(new MenuController());
		addNode(menu);

		ShowEditViewActivityObservable.addShowEditViewActivityListener(new ShowEditViewActivityI() {
			@Override
			public void showEditViewActivity(HashMap<String, Object> hmap) {
				removeIndexFromRoot(2);
				FXMLLoader detailsFxml = loadTemplate("DetailsInserting");
				if (hmap == null) {
					detailsFxml.setController(new ActivityDetailsController((UserVO) hmp.get("user")));
				} else {
					String action = (String) hmap.get("action");
					ActivityVO activity = (ActivityVO) hmap.get("activity");
					if (action.equalsIgnoreCase("view")) {
						detailsFxml = loadTemplate("ActivityDetailsView");
					}
					detailsFxml.setController(new ActivityDetailsController(activity, action));
				}
				Node nodeDetails = FXMLLoaderToNode(detailsFxml);
				TitledPane titledPane = new TitledPane("DETALHES", nodeDetails);
				titledPane.setCollapsible(false);
				titledPane.setAlignment(Pos.CENTER);
				titledPane.getStyleClass().add("activityDetails");
				titledPane.setMaxHeight(Double.POSITIVE_INFINITY);
				addNode(titledPane);
				HBox.setHgrow(titledPane, Priority.ALWAYS);
			}
		});

		ActivityBO.addOnActivityDeletedListener(new OnActivityDeletedI() {
			@Override
			public void onActivityDeleted(ActivityVO act) {
				removeIndexFromRoot(2);

			}
		});

		ScreenUtil.addOnChangeScreenListener(new OnChangeScreen() {
			public void onScreenChanged(String newScreen, HashMap<String, Object> hmap) {
				hmp = hmap;
			}
		});

	}

	class MenuController implements Initializable {
		@FXML
		private ToggleButton btnActivity;
		@FXML
		private ToggleButton btnProject;
		@FXML
		private ToggleButton btnReport;

		public void btnActivityClicked(ActionEvent e) {
			clearRoot(false, (Node) e.getSource());
			if (btnActivity.isSelected()) {
				FXMLLoader p = loadTemplate("ActivityList");
				p.setController(new ActivityListController((UserVO) hmp.get("user")));
				try {
					root.getChildren().addAll((Node) p.load());
				} catch (IOException e1) {
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

	private FXMLLoader loadTemplate(String template) {
		FXMLLoader root = null;

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

	private void addNode(Node node) {
		root.getChildren().add(node);
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

	private Node FXMLLoaderToNode(FXMLLoader fxml) {
		try {
			return ((Node) fxml.load());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void removeIndexFromRoot(int index) {
		if (root.getChildren().size() > index)
			root.getChildren().remove(index);
	}
}
