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
import com.cronoteSys.model.bo.LoginBO;
import com.cronoteSys.model.dao.UserDAO;
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
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;

public class LogoutLoggedAccountsController implements Initializable {

	@FXML
	VBox logoutLoggedRoot;
	@FXML
	JFXListView<SimpleUser> loggedAccounts;
	@FXML
	AnchorPane pnlControls;
	@FXML
	JFXButton btnAddAccount;
	@FXML
	JFXButton btnLogout;
	Properties props = new Properties();
	String ids;
	@FXML
	Label lblUserEmail;
	@FXML
	Label lblUsername;
	@FXML
	Tooltip ttpUserEmail;
	@FXML
	StackPane initialPnl;
	@FXML
	Label lblUserInitial;
	UserVO loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		UserDAO uDAO = new UserDAO();
		initEvents();
		loadProperties();
		List<SimpleUser> lst = uDAO.listLoggedUsers(ids, loggedUser.getIdUser().toString());
		loggedAccounts.setCellFactory(new SavedAccountCellFactory());
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
		ids = props.getProperty("savedAccounts");

		lblUserInitial.setText(loggedUser.getCompleteName().substring(0, 1).toUpperCase());
		String[] userNames = loggedUser.getCompleteName().split(" ");
		String name = userNames.length > 1 ? userNames[0] + " " + userNames[(userNames.length - 1)] : userNames[0];
		lblUsername.setText(name);
		LoginVO login = new LoginBO().getLogin(loggedUser);
		lblUserEmail.setText(login.getEmail());
		ttpUserEmail.setText(login.getEmail());
	}

	private void initEvents() {
		btnLogout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				SessionUtil.clearSession();
				JFXPopup popup = (JFXPopup) btnLogout.getScene().getWindow();
				popup.hide();
				ScreenUtil.openNewWindow((Stage) popup.getOwnerWindow(), "Slogin", false);

			}

		});

		loggedAccounts.getSelectionModel().selectedItemProperty()
				.addListener((ChangeListener<SimpleUser>) (observable, oldValue, newValue) -> {
					System.out.println("here we are bitches!!");
					SessionUtil.clearSession();
					JFXPopup popup = (JFXPopup) btnLogout.getScene().getWindow();
					SessionUtil.getSession().put("loggedUser", new UserDAO().find(newValue.getIdUser()));
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
			JFXPopup popup = (JFXPopup) btnLogout.getScene().getWindow();
			popup.hide();
			SessionUtil.getSession().put("addingAccount", true);
			ScreenUtil.openNewWindow((Stage) popup.getOwnerWindow(), "Slogin", false);
			
		});
	}

}