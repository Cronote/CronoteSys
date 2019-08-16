package com.cronoteSys.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.cronoteSys.controller.ActivityListController.ActivitySelectedI;
import com.cronoteSys.controller.ProjectListController.BtnProjectClickedI;
import com.cronoteSys.controller.ProjectListController.ProjectSelectedI;
import com.cronoteSys.controller.TeamListController.TeamSelectedI;
import com.cronoteSys.controller.TeamViewController.BtnNewTeamClickedI;
import com.cronoteSys.interfaces.LoadActivityInterface;
import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.ActivityBO.OnActivityDeletedI;
import com.cronoteSys.model.bo.LoginBO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.observer.ShowEditViewActivityObservableI;
import com.cronoteSys.observer.ShowEditViewActivityObserverI;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTabPane;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class HomeController implements Initializable {

	@FXML
	protected HBox root;
	@FXML protected StackPane stackPaneOne;
	private MenuController menuControl;

	private void loadProjectManager(ProjectVO project, String mode) {
		removeIndexFromRoot(2);
		FXMLLoader projectFXML = ScreenUtil.loadTemplate("ProjectManager");
		ProjectManagerController control = SessionUtil.getInjector().getInstance(ProjectManagerController.class);
		projectFXML.setController(control);
		JFXTabPane projectManager = (JFXTabPane) FXMLLoaderToNode(projectFXML);
		control.setSelectedProject(project, mode);
		projectManager.setMaxHeight(Double.POSITIVE_INFINITY);
		projectManager.setMaxWidth(Double.POSITIVE_INFINITY);
		HBox.setHgrow(projectManager, Priority.ALWAYS);
		addNode(projectManager);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadMenu();
		ProjectListController.addOnBtnProjectClickedListener(new BtnProjectClickedI() {
			@Override
			public void onBtnProjectClicked() {
				loadProjectManager(null, "cadastro");
			}

		});
		ProjectListController.addOnProjectSelectedListener(new ProjectSelectedI() {
			@Override
			public void onProjectSelect(ProjectVO project) {
				if (project != null)
					loadProjectManager(project, "View");
			}
		});
		TeamViewController.addOnBtnNewTeamClickedListener(new BtnNewTeamClickedI() {
			@Override
			public void onBtnNewTeamClicked(TeamVO team, String mode) {
				switchVBoxTeamContent(team, mode);
			}
		});
		TeamListController.addOnBtnNewTeamClickedListener(new TeamSelectedI() {
			@Override
			public void onTeamSelected(TeamVO team) {
				switchVBoxTeamContent(team, "view");
			}
		});
		root.getChildren().addListener(new ListChangeListener<Node>() {
			@Override
			public void onChanged(Change<? extends Node> c) {
				ScreenUtil.paintScreen(root);
			}
		});
	}

	private void switchVBoxTeamContent(TeamVO team, String mode) {
		FXMLLoader loader = null;
		VBox box = null;
		box = (VBox) root.lookup("#team");
		if (box == null)
			box = new VBox();
		while (box.getChildren().size() > 1) {
			box.getChildren().remove(1);
		}
		try {
			AnchorPane ap = null;
			if (mode.equals("view")) {
				loader = ScreenUtil.loadTemplate("TeamView");
				ap = (AnchorPane) loader.load();
				TeamViewController controller = (TeamViewController) loader.getController();
				controller.setViewingTeam(team);
			} else {
				loader = ScreenUtil.loadTemplate("TeamEdit");
				ap = (AnchorPane) loader.load();
				if (team != null) {
					TeamEditController controller = (TeamEditController) loader.getController();
					controller.setEditingTeam(team);
				}
			}

			box.getChildren().add(ap);
			VBox.setVgrow(ap, Priority.ALWAYS);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		HBox.setHgrow(box, Priority.ALWAYS);
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
	protected ActivitySelectedI actSelectedListener = new ActivitySelectedI() {
		@Override
		public void onActivitySelected(HashMap<String, Object> hmap) {
			FXMLLoader loader = null;
			removeIndexFromRoot(2);
			String mode = (String) hmap.getOrDefault("action", "view");
			ActivityVO activity = (ActivityVO) hmap.get("activity");
			AnchorPane ap = null;
			if (mode.equals("view")) {
				loader = ScreenUtil.loadTemplate("ActivityDetailsView");

			} else {
				loader = ScreenUtil.loadTemplate("ActivityDetailsInserting");
			}
			ap = (AnchorPane) addNode(loader);
			if (activity != null)
				((LoadActivityInterface) loader.getController()).loadActivity(activity);

			HBox.setHgrow(ap, Priority.ALWAYS);
		}
	};

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
	private ToggleButton btnTeam;
	@FXML
	private ToggleGroup btnWindows;
	@FXML
	private Tooltip ttpUserEmail;
	@FXML
	private AnchorPane loggedCard;
	@FXML
	private StackPane initialPnl;
	@FXML
	private Label lblUserInitial;
	@FXML
	private Label lblUsername;
	@FXML
	private Label lblUserEmail;
	private ToggleButton btnSizeToggle = new ToggleButton();
	private HomeController homeControl;
	BooleanProperty shortLarge = new SimpleBooleanProperty(false);

	public MenuController(HomeController homeController) {
		homeControl = homeController;

	}

	public void btnActivityClicked(ActionEvent e) throws IOException {
		ScreenUtil.jfxDialogOpener(homeControl.stackPaneOne);
		homeControl.clearRoot(false, (Node) e.getSource());
		ActivityBO.removeOnActivityDeletedListener(homeControl.listenerActivityDelete);
		if (btnActivity.isSelected()) {
			adjustMenu(false);
			FXMLLoader p = ScreenUtil.loadTemplate("ActivityList");
			homeControl.addNode(p);
			((ActivityListController) p.getController()).setList(null);
			ActivityListController.addOnActivitySelectedListener(homeControl.actSelectedListener);
			ActivityBO.addOnActivityDeletedListener(homeControl.listenerActivityDelete);
		}
	}

	public void btnProjectClicked(ActionEvent e) {
		homeControl.clearRoot(false, (Node) e.getSource());
		if (btnProject.isSelected()) {
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

	public void btnTeamClicked(ActionEvent event) {
		// TODO: implementar
		homeControl.clearRoot(false, (Node) event.getSource());
		if (btnTeam.isSelected()) {
			adjustMenu(true);

			FXMLLoader teamLstLoader = ScreenUtil.loadTemplate("TeamList");
			FXMLLoader teamViewLoader = ScreenUtil.loadTemplate("TeamView");

			VBox box = null;
			try {
				box = (VBox) homeControl.root.lookup("#team");
			} catch (Exception e) {
				box = new VBox();
			}
			try {
				if (box == null) {
					box = new VBox();
					box.setId("team");
				}
				box.getChildren().add(((HBox) teamLstLoader.load()));
				AnchorPane ap = (AnchorPane) teamViewLoader.load();
				box.getChildren().add(ap);
				VBox.setVgrow(ap, Priority.ALWAYS);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			homeControl.addNode(box);
			HBox.setHgrow(box, Priority.ALWAYS);
		}
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
				try {
					btnActivityClicked(event);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
		btnTeam.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				btnTeamClicked(event);
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

		loggedCard.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				JFXPopup popup = new JFXPopup();
				Pane p = new Pane();
				p.setPrefSize(300, 200);
				FXMLLoader fxml = SessionUtil.getInjector().getInstance(FXMLLoader.class);
				fxml.setLocation(HomeController.class.getResource("/fxml/Templates/popups/Logout-LoggedAccounts.fxml"));
				try {
					popup.setPopupContent((Region) fxml.load());
				} catch (Exception e) {
					// TODO: handle exception
				}
				popup.setAutoFix(true);
				popup.show(loggedCard);
			}
		});
	}

	private void initDesign() {
		ttpUserEmail.textProperty().bind(lblUserEmail.textProperty());
		for (Toggle node : btnWindows.getToggles()) {
			ToggleButton btn = ((ToggleButton) node);
			node.setUserData(btn.getGraphicTextGap());
		}
		btnSizeToggle.getStyleClass().addAll("btnTransparent");
		adjustBtnIcon(false);
		btnSizeToggle.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		UserVO loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");
		lblUserInitial.setText(loggedUser.getCompleteName().substring(0, 1).toUpperCase());
		String[] userNames = loggedUser.getCompleteName().split(" ");
		String name = userNames.length > 1 ? userNames[0] + " " + userNames[(userNames.length - 1)] : userNames[0];
		lblUsername.setText(name);
		LoginVO login = loggedUser.getLogin();
		lblUserEmail.setText(login.getEmail());

	}

	protected void adjustMenu(boolean makeItShort) {
		if (makeItShort) {
			menu.setPrefWidth(SHORT_WIDTH);
			imgLogo.setImage(null);
			imgLogo.setTranslateX(0);
			imgLogo.maxWidth(SHORT_WIDTH);
			lblUserEmail.setVisible(false);
			lblUsername.setVisible(false);
			loggedCard.maxWidth(SHORT_WIDTH);
			initialPnl.setTranslateX(72);
			for (Toggle node : btnWindows.getToggles()) {
				ToggleButton btn = ((ToggleButton) node);
				btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			}
		} else {
			menu.setPrefWidth(LARGE_WIDTH);
			imgLogo.setImage(new Image(getClass().getResourceAsStream("/image/cronote_logo_white.png")));
			imgLogo.maxWidth(LARGE_WIDTH);
			loggedCard.maxWidth(LARGE_WIDTH);
			lblUserEmail.setVisible(true);
			lblUsername.setVisible(true);
			loggedCard.maxWidth(LARGE_WIDTH);
			initialPnl.setTranslateX(-10);
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
