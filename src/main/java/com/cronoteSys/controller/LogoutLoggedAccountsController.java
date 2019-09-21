package com.cronoteSys.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import com.cronoteSys.controller.components.cellfactory.SavedAccountCellFactory;
import com.cronoteSys.controller.components.cellfactory.TeamMemberCellFactory;
import com.cronoteSys.model.bo.LoginBO;
import com.cronoteSys.model.bo.UserBO;
import com.cronoteSys.model.dao.UserDAO;
import com.cronoteSys.model.interfaces.ThreatingUser;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.model.vo.view.SimpleUser;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;

public class LogoutLoggedAccountsController implements Initializable {

	@FXML
	private VBox logoutLoggedRoot;
	@FXML
	private ListView<ThreatingUser> loggedAccounts;
	@FXML
	private AnchorPane pnlControls;
	@FXML
	private JFXButton btnAddAccount;
	@FXML
	private Label lblUserEmail;
	@FXML
	private Label lblUsername;
	@FXML
	private Tooltip ttpUserEmail;
	@FXML
	private StackPane initialPnl;
	@FXML
	private Label lblUserInitial;
	private Properties props = new Properties();
	private String ids;
	private UserVO loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		UserBO uDAO = new UserBO();
		initEvents();
		loadProperties();
		List<UserVO> lst = uDAO.listLoggedUsers(ids, loggedUser.getIdUser().toString());
		;
		loggedAccounts.setCellFactory(new TeamMemberCellFactory(true));
		loggedAccounts.setItems(FXCollections.observableArrayList(lst));
	}

	private void loadProperties() {
		FileInputStream file;
		try {
			file = new FileInputStream("./application.properties");
			props.load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ids = props.getProperty("savedAccounts", "0");

		LoginVO login = loggedUser.getLogin();

		lblUserEmail.setText(login.getEmail());
		ttpUserEmail.setText(login.getEmail());
		String[] userNames = loggedUser.getCompleteName().split(" ");
		String name = userNames.length > 1 ? userNames[0] + " " + userNames[(userNames.length - 1)] : userNames[0];
		String initials = "";
		for (String string : userNames) {
			initials += string.substring(0, 1).toUpperCase();
		}
		lblUserInitial.setText(initials.replaceAll(" ", ""));
		double textSize = initials.length();
		textSize = (1 - (textSize * 10 / 100.0)) * 25.0;
		lblUserInitial.setFont(new Font(textSize));
		lblUsername.setText(name);
	}

	private void initEvents() {

		loggedAccounts.getSelectionModel().selectedItemProperty()
				.addListener((ChangeListener<ThreatingUser>) (observable, oldValue, newValue) -> {
					SessionUtil.clearSession();
					JFXPopup popup = (JFXPopup) btnAddAccount.getScene().getWindow();
					SessionUtil.getSession().put("loggedUser", (UserVO) newValue);
					ScreenUtil.openNewWindow((Stage) popup.getOwnerWindow(), "Home", false);
					popup.hide();
				});
		btnAddAccount.setOnMouseEntered(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				btnAddAccount.setStyle("-fx-background-color:#7C4DB8;");

			}
		});
		btnAddAccount.setOnMouseExited(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				btnAddAccount.setStyle("-fx-background-color:transparent;");

			}
		});

		btnAddAccount.setOnAction(event -> {
			SessionUtil.clearSession();
			JFXPopup popup = (JFXPopup) btnAddAccount.getScene().getWindow();
			popup.hide();
			SessionUtil.getSession().put("addingAccount", true);
			ScreenUtil.openNewWindow((Stage) popup.getOwnerWindow(), "Slogin", false);

		});
	}

}