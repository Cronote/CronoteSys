package com.cronoteSys.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.mail.EmailException;

import com.cronoteSys.model.bo.EmailBO;
import com.cronoteSys.model.bo.LoginBO;
import com.cronoteSys.model.vo.EmailVO;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.util.EmailUtil;
import com.cronoteSys.util.GenCode;
import com.cronoteSys.util.GenHash;
import com.cronoteSys.util.RestUtil;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;
import com.cronoteSys.util.validator.PasswordMatchValidator;
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
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ForgotPwdController extends MasterController {

	@FXML
	private StackPane stackPane;
	@FXML
	private JFXTextField txtEmail;
	@FXML
	private Button btnSend;
	@FXML
	private Button btnConfirm;
	@FXML
	private JFXTextField txtCode;
	@FXML
	private Button btnCancel;
	@FXML
	private JFXPasswordField txtPwd;
	@FXML
	private JFXPasswordField txtConfirmPwd;
	@FXML
	private Label lblErrorsIndex;
	@FXML
	private Pane pnlVerification;
	@FXML
	private Label lblCode;
	@FXML
	private Pane pnlSendEmail;
	@FXML
	private StackPane popupPoint;
	@FXML
	private Pane pnlMidBottomArea;

	private String sVerificationCode;
	private JFXPopup popupPass = new JFXPopup();
	private final StackPane passwordExplanationPane = new StackPane();
	private PasswordMatchValidator ps = new PasswordMatchValidator();
	private JFXSnackbar snackbar;

	@FXML
	protected void initialize() {
		try {
			Parent lp = ScreenUtil.loadTemplate("/popups/PasswordExplanation").load();
			passwordExplanationPane.getChildren().add(lp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		snackbar = new JFXSnackbar(pnlMidBottomArea);
		ps.setMessage("SENHAS NÃO COMBINAM");
		sVerificationCode = "";
		popupPass.setPopupContent(passwordExplanationPane);
		initValidations();
		initEvents();

	}

	@FXML
	public void btnSendClicked() throws EmailException {
		if (txtEmail.validate()) {
			String email = txtEmail.getText().trim();
			if (new LoginBO().loginExists(email) == 0) {
				ScreenUtil.jfxDialogOpener(stackPane, "Aviso", "Não há uma conta com o email informado!");
				return;
			}

			boolean bEmailSent = false;
			sVerificationCode = new GenCode().genCode();
			String[] emails = {email};
			EmailVO emailVO = new EmailVO();
			emailVO.setReceiver(emails);
			emailVO.setMessage("Olá,\n Aqui está seu código de confirmação:"+ sVerificationCode + "\nUse-o no sistema para trocar sua senha.");
			emailVO.setSubject("Alteração de senha");
			bEmailSent = new EmailBO().genericEmail(emailVO);
			if (bEmailSent) {
				pnlVerification.getStyleClass().removeAll("hide");
				pnlVerification.getStyleClass().add("show");
			}
		}
	}

	@FXML
	public void btnConfirmClicked() {

		if (txtCode.validate() && txtPwd.validate() && txtConfirmPwd.validate()) {

			if (!sVerificationCode.equalsIgnoreCase(txtCode.getText().trim())) {
				lblErrorsIndex.getParent().setVisible(true);
				int iAttempt = Integer.parseInt(lblErrorsIndex.getText());
				iAttempt++;
				lblErrorsIndex.setText(String.valueOf(iAttempt));
				lblCode.setText("CÓDIGO INVÁLIDO");
				lblCode.getStyleClass().removeAll("hide");
				lblCode.getStyleClass().add("show");
				if (iAttempt > 2) {
					ScreenUtil.jfxDialogOpener(stackPane, "Aviso!", "Estorou o numero de tentativas\nTerá que reenviar o email e colocar o novo código.");
					return;
				}
				return;
			}
			String sPassPureText = txtPwd.getText().trim();
			if (new LoginBO().changePassword(txtEmail.getText(), sPassPureText)) {
				ScreenUtil.jfxDialogOpener(stackPane, "Confirmação!", "Senha alterada com sucesso!");
				resetScreen();
			} else {
				ScreenUtil.jfxDialogOpener(stackPane, "Erro", "Houve algum problema ao alterar a senha!");
			}
		}
	}

	@FXML
	public void btnCancelClicked() {
		resetScreen();
	}

	private void initValidations() {
		Node[] lstFieldsToValidation = { txtEmail, txtCode, txtPwd, txtConfirmPwd };
		Boolean[] areNotNullFields = { true, true, true, true };
		Boolean[] areEmailFields = { true, false, false, false };
		ScreenUtil.addInlineValidation(lstFieldsToValidation, areNotNullFields, areEmailFields);
		ScreenUtil.addPasswordFormatValidator(txtPwd);
	}

	private void initEvents() {
		ScreenUtil.addOnChangeScreenListener(new OnChangeScreen() {
			public void onScreenChanged(String newScreen, HashMap<String, Object> hmap) {
				if (hmap.get("previewScene") != null) {
					setsPreviewsScene((String) hmap.get("previewScene"));
				}
			}
		});
		txtCode.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				lblCode.getStyleClass().removeAll("show");
				lblCode.getStyleClass().add("hide");
			}
		});
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

	private void resetScreen() {
		new ScreenUtil().clearFields(getThisStage(), pnlVerification);
		txtEmail.setText("");
		lblErrorsIndex.setText("0");
		pnlVerification.setVisible(false);
		lblErrorsIndex.getParent().setVisible(false);
	}
}
