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
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;

import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
			SessionUtil.getSession().put("loggedUser", user);
			if ((Boolean) SessionUtil.getSession().getOrDefault("addingAccount", false))
				registerNewLogin(user.getIdUser());
			ScreenUtil.openNewWindow(getThisStage(), "Home", true, hmp);

		} else {
			List<Node> lst = new ArrayList<Node>();
			lst.add(txtEmail);
			lst.add(txtPassword);
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
		if (txtEmail.validate() && txtPassword.validate()) {
			String sUsername = txtEmail.getText().trim(), sPasswd = txtPassword.getText().trim();
			login(new LoginVO(null, sUsername, new GenHash().hashIt(sPasswd)));
		}

	}

	private void registerNewLogin(Integer idUser) {
		try {
			Properties prop = getProp();
			String savedAccounts = prop.getProperty("savedAccounts");
			if (!savedAccounts.contains(idUser.toString()))
				savedAccounts += "," + idUser.toString();
			prop.setProperty("savedAccounts", savedAccounts);
			saveProp(prop);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}