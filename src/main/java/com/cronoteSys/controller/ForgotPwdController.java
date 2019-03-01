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
					bPasswordOk = ScreenUtil.verifyPassFields(txtPwd.getText().trim(), txtConfirmPwd.getText().trim(), lst);
				}

			}
		});
		txtConfirmPwd.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					bPasswordOk = ScreenUtil.verifyPassFields(txtConfirmPwd.getText().trim(), txtPwd.getText().trim(), lst);
				}
			}
		});
	}

	public boolean verifyPassFields(String sPass1, String sPass2, List<Node> lst) {
		if (!sPass1.equals(sPass2)) {
			new ScreenUtil().addORRemoveErrorClass(lst, true);
			return false;
		}
		new ScreenUtil().addORRemoveErrorClass(lst, false);
		if (!new LoginBO().validatePassword(sPass1)) {
			JOptionPane.showMessageDialog(null, "Mensagem de falha por senhas fora de formato ");
			return false;
		}
		return true;
	}

	@FXML
	public void btnSendClicked() {
		if (new ScreenUtil().isFilledFields(getThisStage(), pnlRoot)) {
			String email = txtEmail.getText().trim();
			if (!new EmailUtil().validateEmail(email)) { // Email em formato valido
				JOptionPane.showMessageDialog(null, "Mensagem de falha por email inválido");
				return;
			}

			objLogin = new LoginBO().loginExists(email);
			if (objLogin == null) {// Conta com este email n existe
				JOptionPane.showMessageDialog(null, "Mensagem de falha por n existir conta com esse email");
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
			pnlVerification.setVisible(bEmailSent);
		}
	}

	@FXML
	public void btnConfirmClicked() {
		if (!new ScreenUtil().isFilledFields(getThisStage(), pnlRoot)) {
			return;
		}

		if (sVerificationCode.equalsIgnoreCase(txtCode.getText().trim())) {
			if (!bPasswordOk) {
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

	private void resetScreen() {
		new ScreenUtil().clearFields(getThisStage(), pnlVerification);
		txtEmail.setText("");
		lblErrorsIndex.setText("0");
		pnlVerification.setVisible(false);
	}
}
