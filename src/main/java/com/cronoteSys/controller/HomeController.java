package com.cronoteSys.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.cronoteSys.controller.ProjectCell.ProjectSelectedI;
import com.cronoteSys.controller.ProjectListController.BtnProjectClickedI;
import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.ActivityBO.OnActivityDeletedI;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.observer.ShowEditViewActivityObservableI;
import com.cronoteSys.observer.ShowEditViewActivityObserverI;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;
import com.cronoteSys.util.SessionUtil;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class HomeController implements Initializable {

	@FXML
	protected HBox root;
	private MenuController menuControl;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadMenu();

		ProjectListController.addOnBtnProjectClickedListener(new BtnProjectClickedI() {
			@Override
			public void onBtnProjectClicked() {
				removeIndexFromRoot(2);
				FXMLLoader projectFXML = ScreenUtil.loadTemplate("ProjectManager");
				ProjectManagerController control = SessionUtil.getInjector()
						.getInstance(ProjectManagerController.class);
				control.setState(ProjectManagerState.FIRST_INFO);
				projectFXML.setController(control);
				BorderPane projectManager = (BorderPane) FXMLLoaderToNode(projectFXML);

				TitledPane titledPane = new TitledPane("CADASTRAR PROJETO", projectManager);
				titledPane.setCollapsible(false);
				titledPane.setAlignment(Pos.CENTER);
				titledPane.getStyleClass().add("activityDetails");
				titledPane.setMaxHeight(Double.POSITIVE_INFINITY);
				addNode(titledPane);
				HBox.setHgrow(titledPane, Priority.ALWAYS);
			}
		});
		ProjectCell.addOnProjectSelectedListener(new ProjectSelectedI() {
			@Override
			public void onProjectSelect(ProjectVO project) {
				removeIndexFromRoot(2);

				if (project != null) {
					FXMLLoader projectFXML = ScreenUtil.loadTemplate("ProjectManager");
					ProjectManagerController control = SessionUtil.getInjector()
							.getInstance(ProjectManagerController.class);
					control.setState(ProjectManagerState.FIRST_INFO);
					projectFXML.setController(control);
					control.setSelectedProject(project);
					BorderPane projectManager = (BorderPane) FXMLLoaderToNode(projectFXML);
					TitledPane titledPane = new TitledPane("INFORMAÇÕES DO PROJETO", projectManager);
					titledPane.setCollapsible(false);
					titledPane.setAlignment(Pos.CENTER);
					titledPane.getStyleClass().add("activityDetails");
					titledPane.setMaxHeight(Double.POSITIVE_INFINITY);
					addNode(titledPane);
					HBox.setHgrow(titledPane, Priority.ALWAYS);
				}
			}
		});
		
		root.getChildren().addListener(new ListChangeListener<Node>() {
			@Override
			public void onChanged(Change<? extends Node> c) {
				ScreenUtil.paintScreen(root);
			}
		});
	}

	private void loadMenu() {
		FXMLLoader menu = ScreenUtil.loadTemplate("Menu_large");
		menuControl = new MenuController(this);
		menu.setController(menuControl);
		addNode(menu);
	}

	protected void clearRoot(boolean removeMenu, Node node) {
		int letIndex = removeMenu ? 0 : 1;
		while (root.getChildren().size() > letIndex) {
			root.getChildren().remove(letIndex);
		}
	}

	protected void addNode(Node node) {
		root.getChildren().add(node);
	}

	protected Node addNode(FXMLLoader fxml) {
		Node node = FXMLLoaderToNode(fxml);
		root.getChildren().add(node);
		return node;
	}

	protected Node FXMLLoaderToNode(FXMLLoader fxml) {
		try {
			return ((Node) fxml.load());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected void removeIndexFromRoot(int index) {
		while (root.getChildren().size() > index)
			root.getChildren().remove(index);
	}

	protected OnActivityDeletedI listenerActivityDelete = new OnActivityDeletedI() {
		@Override
		public void onActivityDeleted(ActivityVO act) {
			removeIndexFromRoot(2);

		}
	};
	protected ShowEditViewActivityObserverI showListener = new ShowEditViewActivityObserverI() {
		@Override
		public void showEditViewActivity(HashMap<String, Object> hmap) {
			if (hmap.get("project") != null)
				removeIndexFromRoot(3);
			else
				removeIndexFromRoot(2);
			FXMLLoader detailsFxml = ScreenUtil.loadTemplate("DetailsInserting");

			String action = (String) hmap.get("action");
			ProjectVO project = (ProjectVO) hmap.get("project");
			if (action.equalsIgnoreCase("cadastro")) {
				if (project != null) {
					detailsFxml.setController(new ActivityDetailsController(project));
				}
				detailsFxml.setController(new ActivityDetailsController(null));
			} else {
				ActivityVO activity = (ActivityVO) hmap.get("activity");
				if (action.equalsIgnoreCase("view")) {
					detailsFxml = ScreenUtil.loadTemplate("ActivityDetailsView");
				}
				detailsFxml.setController(new ActivityDetailsController(activity, action));
			}
			AnchorPane ap =(AnchorPane) addNode(detailsFxml);
//			titledPane.setAlignment(Pos.CENTER);
//			titledPane.getStyleClass().add("activityDetails");
			ap.setMaxHeight(Double.POSITIVE_INFINITY);
//			addNode(titledPane);
			HBox.setHgrow(ap, Priority.ALWAYS);
		}
	};

	public ShowEditViewActivityObserverI getShowListener() {
		return showListener;
	}
}

class MenuController implements Initializable {
	private static final double LARGE_WIDTH = 232.0;
	private static final int SHORT_WIDTH = 70;
	@FXML
	private TitledPane menu;
	@FXML
	private ImageView imgLogo;
	@FXML
	private ToggleButton btnActivity;
	@FXML
	private ToggleButton btnReport;
	@FXML
	private ToggleButton btnProject;
	@FXML
	private ToggleGroup btnWindows;

	private ToggleButton btnSizeToggle = new ToggleButton();
	private HomeController homeControl;
	BooleanProperty shortLarge = new SimpleBooleanProperty(false);

	public MenuController(HomeController homeController) {
		homeControl = homeController;
	}

	public void btnActivityClicked(ActionEvent e) {
		homeControl.clearRoot(false, (Node) e.getSource());
		ShowEditViewActivityObservableI.removeShowEditViewActivityListener(homeControl.getShowListener());
		ActivityBO.removeOnActivityDeletedListener(homeControl.listenerActivityDelete);
		if (btnActivity.isSelected()) {
			adjustMenu(false);
			FXMLLoader p = ScreenUtil.loadTemplate("ActivityList");
			homeControl.addNode(p);
			ShowEditViewActivityObservableI.addShowEditViewActivityListener(homeControl.getShowListener());
			ActivityBO.addOnActivityDeletedListener(homeControl.listenerActivityDelete);
		}
		
	}

	public void btnProjectClicked(ActionEvent e) {
		homeControl.clearRoot(false, (Node) e.getSource());
		if (btnProject.isSelected()) {
			ShowEditViewActivityObservableI.removeShowEditViewActivityListener(homeControl.getShowListener());
			ActivityBO.removeOnActivityDeletedListener(homeControl.listenerActivityDelete);
			adjustMenu(true);
			FXMLLoader p = ScreenUtil.loadTemplate("ProjectList");
			homeControl.addNode(p);

		}
	}

	public void btnReportClicked(ActionEvent e) {
		// TODO: implementar
		homeControl.clearRoot(false, (Node) e.getSource());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initDesign();
		initEvents();
	}

	private void initEvents() {
		btnSizeToggle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				adjustMenu(btnSizeToggle.isSelected());
			}
		});
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

		menu.skinProperty().addListener((ChangeListener<Skin<?>>) (observable, oldValue, newValue) -> {
			AnchorPane ap = new AnchorPane();
			ap.getStyleClass().add("title");
			Label label = new Label("HOME");
			ap.getChildren().addAll(label, btnSizeToggle);
			ap.setPrefWidth(Double.POSITIVE_INFINITY);
			AnchorPane.setLeftAnchor(label, 5.0);
			AnchorPane.setBottomAnchor(label, 5.0);
			AnchorPane.setTopAnchor(label, 5.0);
			AnchorPane.setRightAnchor(btnSizeToggle, 0.0);
			menu.setGraphic(ap);
			menu.getGraphic().prefWidth(Double.POSITIVE_INFINITY);
			menu.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			adjustMenu(shortLarge.get());
		});
	}

	private void initDesign() {
		for (Toggle node : btnWindows.getToggles()) {
			ToggleButton btn = ((ToggleButton) node);
			node.setUserData(btn.getGraphicTextGap());
		}
		btnSizeToggle.getStyleClass().addAll("btnTransparent");
		adjustBtnIcon(false);
		btnSizeToggle.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	}

	protected void adjustMenu(boolean makeItShort) {
		if (makeItShort) {
			menu.setPrefWidth(SHORT_WIDTH);
			imgLogo.setImage(new Image(getClass().getResourceAsStream("/image/cronote_logo_dark_short.png")));
			imgLogo.setTranslateX(0);
			imgLogo.maxWidth(SHORT_WIDTH);
			for (Toggle node : btnWindows.getToggles()) {
				ToggleButton btn = ((ToggleButton) node);
				btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			}
		} else {
			menu.setPrefWidth(LARGE_WIDTH);
			imgLogo.setImage(new Image(getClass().getResourceAsStream("/image/cronote_logo_white.png")));
			imgLogo.maxWidth(LARGE_WIDTH);
			for (Toggle node : btnWindows.getToggles()) {
				ToggleButton btn = ((ToggleButton) node);
				btn.setContentDisplay(ContentDisplay.LEFT);
			}
		}
		adjustBtnIcon(makeItShort);
	}

	private void adjustBtnIcon(boolean isShort) {
		GlyphIcon<?> icon = null;
		if (isShort) {
			icon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_RIGHT);
		} else {
			icon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_LEFT);
		}
		icon.setSize("1.5em");
		btnSizeToggle.setGraphic(icon);
	}

}// MenuController
