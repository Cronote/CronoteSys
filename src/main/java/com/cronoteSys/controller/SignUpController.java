package com.cronoteSys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import com.cronoteSys.model.bo.LoginBO;
import com.cronoteSys.model.bo.UserBO;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.EmailUtil;
import com.cronoteSys.util.GenHash;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class SignUpController extends MasterController {

	@FXML
	private TextField txtName;
	@FXML
	private DatePicker dateBirthday;
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtSecondEmail;
	@FXML
	private Button btnProfile;
	@FXML
	private ImageView imgProfile;
	@FXML
	private Hyperlink linkProfile;
	@FXML
	private Button btnSignUp;
	@FXML
	private PasswordField txtPwd;
	@FXML
	private PasswordField txtConfirmPwd;
	@FXML
	private AnchorPane pnlInput;
	@FXML
	private Label lblName;
	@FXML
	private Label lblBirthDate;
	@FXML
	private Label lblEmail;
	@FXML
	private Label lblSecondEmail;
	@FXML
	private Label lblPwd;
	@FXML
	private Label lblConfirmPwd;

	private List<Node> allLabels;
	private boolean bPasswordOk;
	private LoginVO objLogin;

	@FXML
	protected void initialize() {
		final List<Node> lstPasswordNodes = new ArrayList<Node>();
		final List<Node> lstPasswordLabelNodes = new ArrayList<Node>();
		lstPasswordNodes.add(txtPwd);
		lstPasswordNodes.add(txtConfirmPwd);
		lstPasswordLabelNodes.add(lblPwd);
		lstPasswordLabelNodes.add(lblConfirmPwd);
		objLogin = new LoginVO();
		ScreenUtil.addOnChangeScreenListener(new OnChangeScreen() {
			public void onScreenChanged(String newScreen, HashMap<String, Object> hmap) {
				if (hmap.get("previewScene") != null) {
					setsPreviewsScene((String) hmap.get("previewScene"));
				}
			}
		});

		txtPwd.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					bPasswordOk = ScreenUtil.verifyPassFields(txtPwd.getText().trim(), txtConfirmPwd.getText().trim(),
							lstPasswordNodes, lstPasswordLabelNodes);
				}

			}
		});
		txtConfirmPwd.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					bPasswordOk = ScreenUtil.verifyPassFields(txtConfirmPwd.getText().trim(), txtPwd.getText().trim(),
							lstPasswordNodes, lstPasswordLabelNodes);
				}
				lblConfirmPwd.getStyleClass().remove("show");
				lblConfirmPwd.getStyleClass().add("hide");
			}
		});
	}

	@FXML
	public void btnSignUpClicked() {
		hiddenAllLabels();
		if (new ScreenUtil().isFilledFields(getThisStage(), pnlInput, false)) {
			String sEmail = txtEmail.getText().trim();
			if (!EmailUtil.validateEmail(sEmail)) {
				lblEmail.setText("Email fora do formato");
				lblEmail.getStyleClass().remove("hide");
				lblEmail.getStyleClass().add("show");
				return;
			}
			
			LoginVO loginVO = new LoginBO().loginExists(sEmail);
			if (loginVO != null) {
				lblEmail.setText("Email já cadastrado");
				txtEmail.getStyleClass().add("error");
				lblEmail.getStyleClass().remove("hide");
				lblEmail.getStyleClass().add("show");
			}
			if (!bPasswordOk) {
				lblPwd.getStyleClass().remove("hide");
				lblConfirmPwd.getStyleClass().remove("hide");
				lblPwd.getStyleClass().add("show");
				lblConfirmPwd.getStyleClass().add("show");
				txtConfirmPwd.getStyleClass().add("error");
				txtPwd.getStyleClass().add("error");
				lblPwd.setText("Senhas diferentes");
				lblConfirmPwd.setText("Senhas diferentes");
				return;
			}
			String sPassPureText = txtPwd.getText().trim();
			String sPassEncrypted = new GenHash().hashIt(sPassPureText);

			UserVO objUser = new UserVO();
			objUser.setCompleteName(txtName.getText().trim());
			objUser.setEmailRecover(txtSecondEmail.getText().trim());
			objUser.setBirthDate(dateBirthday.getValue());
			objUser.setStats(Byte.parseByte("1"));
			objUser = new UserBO().save(objUser);
			objLogin.setTbUser(objUser);
			objLogin.setEmail(sEmail);
			objLogin.setPasswd(sPassEncrypted);
			objLogin = new LoginBO().save(objLogin);
			if (objUser != null && objLogin != null) {
				JOptionPane.showMessageDialog(null, "Mensagem de sucesso");
				new ScreenUtil().clearFields(getThisStage(), pnlInput);
				hiddenAllLabels();
			} else {
				JOptionPane.showMessageDialog(null, "Mensagem de falha");
			}
		}
	}

	private void hiddenAllLabels() {
		allLabels = new ArrayList<Node>();
		allLabels.add(lblName);
		allLabels.add(lblEmail);
		allLabels.add(lblSecondEmail);
		allLabels.add(lblPwd);
		allLabels.add(lblConfirmPwd);

		for (Node n : allLabels) {
			if (n.getStyleClass().contains("show")) {
				n.getStyleClass().remove("show");
				n.getStyleClass().add("hide");
			}
		}
	}
}