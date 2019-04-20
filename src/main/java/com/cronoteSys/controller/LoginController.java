/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.cronoteSys.model.bo.LoginBO;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.GenHash;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.SessionUtil;
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Bruno
 */
public class LoginController extends MasterController {

	@FXML
	private Label lblEmail;
	@FXML
	private Label lblPassword;
	@FXML
	private Button btnLogin;
	@FXML
	private Hyperlink linkSignUp;
	@FXML
	private TextField txtEmail;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private Hyperlink linkRecover;
	private HashMap<String, Object> hmp;
	private boolean rememberMe = true;
	@FXML
	AnchorPane pnlLogin;

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
		txtEmail.setOnKeyPressed(new javafx.event.EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					btnLoginClicked(null);
				}
			}
		});
		txtPassword.setOnKeyPressed(new javafx.event.EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					btnLoginClicked(null);
				}
			}
		});

	}

	public void login(LoginVO login) {

		UserVO user = new LoginBO().login(login);
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
			System.out.println(user);
			SessionUtil.getSESSION().put("loggedUser", user);
			ScreenUtil.openNewWindow(getThisStage(), "Home", false, hmp);
		} else {
			List<Node> lst = new ArrayList<Node>();
			lst.add(txtEmail);
			lst.add(txtPassword);
			lst.add(lblEmail);
			lst.add(lblPassword);
			new ScreenUtil().addORRemoveErrorClass(lst, true);
			HashMap<String, Object> hmapValues = new HashMap<String, Object>();
			hmapValues.put("msg", "Usuário ou senha incorretos!");
			System.out.println("deu errado");
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
		if (new ScreenUtil().isFilledFields(getThisStage(), pnlLogin, false)) {
			String sUsername = txtEmail.getText().trim(), sPasswd = txtPassword.getText().trim();
			login(new LoginVO(null, sUsername, new GenHash().hashIt(sPasswd)));
		}

	}
}