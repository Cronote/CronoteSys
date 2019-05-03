package com.cronoteSys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.mail.EmailException;

import com.cronoteSys.model.bo.LoginBO;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.util.EmailUtil;
import com.cronoteSys.util.GenCode;
import com.cronoteSys.util.GenHash;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ForgotPwdController extends MasterController {

	@FXML
	private TextField txtEmail;
	@FXML
	private Button btnSend;
	@FXML
	private Button btnConfirm;
	@FXML
	private TextField txtCode;
	@FXML
	private Button btnCancel;
	@FXML
	private PasswordField txtPwd;
	@FXML
	private PasswordField txtConfirmPwd;
	@FXML
	private Label lblErrorsIndex;
	@FXML
	private Pane pnlVerification;
	@FXML
	private Label lblEmailSend;
	@FXML
	private Label lblCode;
	@FXML
	private Label lblNewPwd;
	@FXML
	private Label lblConfirmPwd;
	@FXML
	private Pane pnlSendEmail;

	private String sVerificationCode;
	private boolean bPasswordOk;
	private LoginVO objLogin;

	@FXML
	protected void initialize() {
		final List<Node> lst = new ArrayList<Node>();
		lst.add(txtPwd);
		lst.add(txtConfirmPwd);
		sVerificationCode = "";
		bPasswordOk = false;
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
					List<Node> lstLabel = new ArrayList<Node>();
					lstLabel.add(lblNewPwd);
					lstLabel.add(lblConfirmPwd);
					bPasswordOk = ScreenUtil.verifyPassFields(txtPwd.getText().trim(), txtConfirmPwd.getText().trim(), lst, lstLabel);
				}

			}
		});
		txtConfirmPwd.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					List<Node> lstLabel = new ArrayList<Node>();
					lstLabel.add(lblNewPwd);
					lstLabel.add(lblConfirmPwd);
					bPasswordOk = ScreenUtil.verifyPassFields(txtConfirmPwd.getText().trim(), txtPwd.getText().trim(), lst, lstLabel);
				}
			}
		});
	}

	@FXML
	public void btnSendClicked() {
		txtEmail.getStyleClass().remove("error");
		if (new ScreenUtil().isFilledFields(getThisStage(), pnlSendEmail,false)) {
			lblEmailSend.getStyleClass().remove("show");
			lblEmailSend.getStyleClass().add("hide");
			String email = txtEmail.getText().trim();
			if (!EmailUtil.validateEmail(email)) { // Email em formato valido
				lblEmailSend.setText("Email inválido");
				txtEmail.getStyleClass().add("error");
				lblEmailSend.getStyleClass().remove("hide");
				lblEmailSend.getStyleClass().add("show");
				//TODO: exibir mensagem de email invalido
				return;
			}

			objLogin = new LoginBO().loginExists(email);
			if (objLogin == null) {// Conta com este email n existe
				lblEmailSend.setText("Email não existente");
				txtEmail.getStyleClass().add("error");
				lblEmailSend.getStyleClass().remove("hide");
				lblEmailSend.getStyleClass().add("show");
				//TODO: exibir mensagem de email inexistente
				return;
			}

			boolean bEmailSent = false;
			sVerificationCode = new GenCode().genCode();
			try {
				bEmailSent = new EmailUtil().sendEmail(email, "Olá,\n Aqui está seu código de confirmação: "
						+ sVerificationCode + "\nUse-o no sistema para trocar sua senha.", "Alteração de senha");
			} catch (EmailException e) {
				e.printStackTrace();
			}
			if(bEmailSent) {
				pnlVerification.getStyleClass().remove("hide");
				pnlVerification.getStyleClass().add("show");
			}
		}
	}

	@FXML
	public void btnConfirmClicked() {
		resetLabels();
		txtCode.getStyleClass().remove("error");
		txtPwd.getStyleClass().remove("error");
		txtConfirmPwd.getStyleClass().remove("error");
		
		if (!new ScreenUtil().isFilledFields(getThisStage(), pnlVerification,false )) {
			return;
		}

		if (sVerificationCode.equalsIgnoreCase(txtCode.getText().trim())) {
			if (!bPasswordOk) {
				lblConfirmPwd.getStyleClass().remove("hide");
				lblConfirmPwd.getStyleClass().add("show");
				JOptionPane.showMessageDialog(null, "Mensagem de falha, senhas n OK ");
				return;
			}

			String sPassPureText = txtPwd.getText().trim();
			objLogin.setPasswd(new GenHash().hashIt(sPassPureText));
			JOptionPane.showMessageDialog(null, "Mensagem de Sucesso!");
			resetScreen();

		} else {
			int iAttempt = Integer.parseInt(lblErrorsIndex.getText());
			iAttempt++;
			lblErrorsIndex.setText(String.valueOf(iAttempt));
			lblCode.setText("Código inválido");
			txtCode.getStyleClass().add("error");
			lblCode.getStyleClass().remove("hide");
			lblCode.getStyleClass().add("show");
			if (iAttempt > 2) {
				JOptionPane.showMessageDialog(null, "Mensagem de falha por estourar numero de tentativas");
				resetScreen();
				return;

			}
		}
	}

	@FXML
	public void btnCancelClicked() {
		resetScreen();
	}
	
	public void resetLabels() {
		lblCode.getStyleClass().remove("show");
		lblNewPwd.getStyleClass().remove("show");
		lblConfirmPwd.getStyleClass().remove("show");
		lblCode.getStyleClass().add("hide");
		lblNewPwd.getStyleClass().add("hide");
		lblConfirmPwd.getStyleClass().add("hide");
	}

	private void resetScreen() {
		new ScreenUtil().clearFields(getThisStage(), pnlVerification);
		txtEmail.setText("");
		lblErrorsIndex.setText("0");
		pnlVerification.setVisible(false);
	}
}
