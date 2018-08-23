/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cronoteSys.model.bo.LoginBO;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.GenHash;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Bruno
 */
public class LoginController extends MasterController {

	@FXML
	private Button btnLogin;
	@FXML
	private Button btnSignUp;
	@FXML
	private TextField txtEmail;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private Hyperlink linkRecover;
	@FXML 
	private ImageView imgEye;
	private HashMap<String, Object> hmp;

	
	@FXML
	public void initialize() {
		hmp = new HashMap<String,Object>();
		hmp.put("previewScene", "SLogin");
		ScreenUtil.addOnChangeScreenListener(new OnChangeScreen() {
			public void onScreenChanged(String newScreen, HashMap<String, Object> hmap) {
				//por enquanto nada
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
		imgEye.setOnMousePressed(new javafx.event.EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				System.out.println("pressionado");
			}
		});
		imgEye.setOnMouseReleased(new javafx.event.EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				System.out.println("'soltado'");
			}
		});
	}

	public void login(LoginVO login) {

		UserVO user = new LoginBO().login(login);
		if (user != null) {
			System.out.println("Logou!!");
			//new ScreenUtil().openNewWindow(getThisStage(), "Scene", false);
			//getThisStage().close();
		} else {
			List<Node> lst = new ArrayList<Node>();
			lst.add(txtEmail);
			lst.add(txtPassword);
			new ScreenUtil().addORRemoveErrorClass(lst, true);
			HashMap<String, Object> hmapValues = new HashMap<String, Object>();
			hmapValues.put("msg", "Usuário ou senha incorretos!");
			new ScreenUtil().openNewWindow(getThisStage(), "AlertDialog", true, hmapValues);
		}
	}

	@FXML
	private void linkRecoverClicked() {
		new ScreenUtil().openNewWindow(getThisStage(), "SForgotPwd", false, hmp);
	}

	@FXML
	private void btnSignUpClicked() {
		new ScreenUtil().openNewWindow(getThisStage(), "SSignUp", false, hmp);
	}

	@FXML
	private void btnLoginClicked(ActionEvent event) {
		if (new ScreenUtil().isFilledFields(getThisStage(), pnlRoot)) {
			String sUsername = txtEmail.getText().trim(), sPasswd = txtPassword.getText().trim();
			login(new LoginVO(null, sUsername, new GenHash().hashIt(sPasswd)));
		}
		
	}
}