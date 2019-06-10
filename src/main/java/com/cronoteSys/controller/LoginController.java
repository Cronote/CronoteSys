package com.cronoteSys.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javafx.util.Duration;
import com.cronoteSys.model.bo.LoginBO;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.GenHash;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Bruno
 */
public class LoginController extends MasterController {

	@FXML
	private Button btnLogin;
	@FXML
	private Hyperlink linkSignUp;
	@FXML
	private JFXTextField txtEmail;
	@FXML
	private JFXPasswordField txtPassword;
	@FXML
	private Hyperlink linkRecover;
	private HashMap<String, Object> hmp;
	private boolean rememberMe = true;
	@FXML
	private AnchorPane pnlLogin;
	@FXML
	private Pane pnlMidBottomArea;
	JFXSnackbar snackbar;

	@FXML
	public void initialize() {
		try {
			Properties prop = getProp();
			txtEmail.setText(prop.getProperty("LoginScreen.username"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		hmp = new HashMap<String, Object>();
		hmp.put("previewScene", "SLogin");
		ScreenUtil.addOnChangeScreenListener(new OnChangeScreen() {
			public void onScreenChanged(String newScreen, HashMap<String, Object> hmap) {
				// por enquanto nada
			}
		});
		Node[] lstFieldsToValidation = { txtEmail, txtPassword };
		Boolean[] areNotNullFields = { true, true };
		Boolean[] areEmailFields = { true, false };
		ScreenUtil.addInlineValidation(lstFieldsToValidation, areNotNullFields, areEmailFields);
		snackbar = new JFXSnackbar(pnlMidBottomArea);
	}

	public void login(LoginVO login) {
		UserVO user = new LoginBO().login(login);
		System.out.println(user != null);
		if (user != null) {
			if (rememberMe) {
				try {
					Properties prop = getProp();
					prop.setProperty("LoginScreen.username", login.getEmail());
					saveProp(prop);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			SessionUtil.getSession().put("loggedUser", user);
			if ((Boolean) SessionUtil.getSession().getOrDefault("addingAccount", false))
				registerNewLogin(user.getIdUser());
			ScreenUtil.openNewWindow(getThisStage(), "Home", true, hmp);

		} else {

			snackbar.fireEvent(new SnackbarEvent(
					new JFXSnackbarLayout("E-mail e/ou senha incorretos!", "Fechar", action -> snackbar.close()),
					Duration.INDEFINITE, null));
		}
	}

	@FXML
	private void linkRecoverClicked() {
		ScreenUtil.openNewWindow(getThisStage(), "SForgotPwd", false, hmp);
	}

	@FXML
	private void linkSignUpClicked() {
		ScreenUtil.openNewWindow(getThisStage(), "SSignUp", false, hmp);
	}

	@FXML
	private void btnLoginClicked(ActionEvent event) {
		if (txtEmail.validate() && txtPassword.validate()) {
			String sUsername = txtEmail.getText().trim(), sPasswd = txtPassword.getText().trim();
			login(new LoginVO(null, sUsername, new GenHash().hashIt(sPasswd)));
		}

	}

	private void registerNewLogin(Integer idUser) {
		try {
			Properties prop = getProp();
			String savedAccounts = prop.getProperty("savedAccounts", "");
			System.out.println(savedAccounts.split(",").length);
			if (savedAccounts.equals(""))
				savedAccounts += idUser.toString();
			else
				savedAccounts += "," + idUser.toString();
			prop.setProperty("savedAccounts", savedAccounts);
			saveProp(prop);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}