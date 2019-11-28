package com.cronoteSys.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.cronoteSys.model.bo.LoginBO;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.GenHash;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Bruno
 */
public class LoginController extends MasterController {

	@FXML
	private StackPane stackPane;
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
	@FXML
	private AnchorPane pnlLogin;
	@FXML
	private JFXCheckBox chkRememberMe;
	@FXML
	private Pane pnlMidBottomArea;

	@FXML
	public void initialize() {
		try {
			Properties prop = getProp();
			chkRememberMe.setSelected(Boolean.valueOf(prop.getProperty("LoginScreen.rememberMe", "false")));
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
	}

	public void login(LoginVO login) {
		UserVO user = new LoginBO().login(login);
		if (user != null) {
			if (chkRememberMe.isSelected()) {
				try {
					Properties prop = getProp();
					prop.setProperty("LoginScreen.rememberMe", String.valueOf(chkRememberMe.isSelected()));
					prop.setProperty("LoginScreen.username", login.getEmail());
					saveProp(prop);

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Properties prop = getProp();
					prop.setProperty("LoginScreen.rememberMe", String.valueOf(chkRememberMe.isSelected()));
					prop.setProperty("LoginScreen.username", "");
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
			ScreenUtil.jfxDialogOpener(stackPane,"Aviso!", "Credenciais incorretas");
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
			login(new LoginVO( sUsername, new GenHash().hashIt(sPasswd)));
		}
		
	}

	private void registerNewLogin(Integer idUser) {
		try {
			Properties prop = getProp();
			String savedAccounts = prop.getProperty("savedAccounts", "");
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