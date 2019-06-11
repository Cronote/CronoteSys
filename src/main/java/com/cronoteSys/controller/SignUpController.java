package com.cronoteSys.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import com.cronoteSys.model.bo.LoginBO;
import com.cronoteSys.model.bo.UserBO;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.GenHash;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;
import com.cronoteSys.util.validator.LoginExistValidator;
import com.cronoteSys.util.validator.PasswordMatchValidator;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class SignUpController extends MasterController {

	@FXML
	private JFXTextField txtName;
	@FXML
	private JFXDatePicker dateBirthday;
	@FXML
	private JFXTextField txtEmail;
	@FXML
	private JFXTextField txtSecondEmail;
	@FXML
	private Button btnProfile;
	@FXML
	private ImageView imgProfile;
	@FXML
	private Hyperlink linkProfile;
	@FXML
	private Button btnSignUp;
	@FXML
	private JFXPasswordField txtPwd;
	@FXML
	private JFXPasswordField txtConfirmPwd;
	@FXML
	private AnchorPane pnlInput;
	@FXML
	private StackPane popupPoint;
	@FXML
	private Pane pnlMidBottomArea;
	private JFXPopup popupPass = new JFXPopup();
	private LoginVO objLogin;
	private final StackPane passwordExplanationPane = new StackPane();
	private PasswordMatchValidator ps = new PasswordMatchValidator();
	private JFXSnackbar snackbar;

	@FXML
	protected void initialize() {
		snackbar = new JFXSnackbar(pnlMidBottomArea);
		ps.setMessage("SENHAS NÃO COMBINAM");
		try {
			Parent lp = ScreenUtil.loadTemplate("/popups/PasswordExplanation").load();
			passwordExplanationPane.getChildren().add(lp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		popupPass.setPopupContent(passwordExplanationPane);

		Node[] lstFieldsToValidation = { txtName, txtEmail, txtSecondEmail, txtPwd, txtConfirmPwd };
		Boolean[] areNotNullFields = { true, true, false, true, true };
		Boolean[] areEmailFields = { false, true, true, false, false };
		ScreenUtil.addInlineValidation(lstFieldsToValidation, areNotNullFields, areEmailFields);

		objLogin = new LoginVO();
		ScreenUtil.addOnChangeScreenListener(new OnChangeScreen() {
			public void onScreenChanged(String newScreen, HashMap<String, Object> hmap) {
				if (hmap.get("previewScene") != null) {
					setsPreviewsScene((String) hmap.get("previewScene"));
				}
			}
		});
		ScreenUtil.addPasswordFormatValidator(txtPwd);
		txtPwd.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					popupPass.show(popupPoint);
				}
				if (!newValue) {
					popupPass.hide();
					ps.setRegexPattern(txtPwd.getText());
					txtConfirmPwd.getValidators().add(ps);
				}
			}
		});
		txtConfirmPwd.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				ps.setRegexPattern(txtPwd.getText());
				if (!newValue) {
					if (txtConfirmPwd.validate()) {
						txtConfirmPwd.getValidators().remove(ps);
						txtConfirmPwd.getValidators().add(ps);
					}
				}
			}
		});
	}

	@FXML
	public void btnSignUpClicked() {
		if (txtName.validate() && txtEmail.validate() && txtPwd.validate() && txtConfirmPwd.validate()
				&& (txtSecondEmail.validate() || txtSecondEmail.getText().isEmpty())) {
			LoginExistValidator loginValidtor = new LoginExistValidator();
			txtEmail.getValidators().add(loginValidtor);
			if (!txtEmail.validate()) {
				return;
			}
			if (!txtSecondEmail.getText().isEmpty()) {
				txtSecondEmail.getValidators().add(loginValidtor);
				if (!txtSecondEmail.validate()) {
					return;
				}
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
			objLogin.setEmail(txtEmail.getText());
			objLogin.setPasswd(sPassEncrypted);
			objLogin = new LoginBO().save(objLogin);
			if (objUser != null && objLogin != null) {
				snackbar.getStyleClass().removeAll("error-snackbar");
				snackbar.getStyleClass().add("success-snackbar");
				snackbar.fireEvent(new SnackbarEvent(
						new JFXSnackbarLayout("Cadastrado com sucesso!", "Fechar", action -> snackbar.close()),
						Duration.INDEFINITE, null));
				new ScreenUtil().clearFields(getThisStage(), pnlInput);
			} else {
				snackbar.getStyleClass().removeAll("success-snackbar");
				snackbar.getStyleClass().add("error-snackbar");
				snackbar.fireEvent(new SnackbarEvent(
						new JFXSnackbarLayout("Cadastro incorreto!", "Fechar", action -> snackbar.close()),
						Duration.INDEFINITE, null));
			}
		}
	}

}