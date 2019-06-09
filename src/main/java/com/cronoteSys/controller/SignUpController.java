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
import com.cronoteSys.test.PasswordMatchValidator;
import com.cronoteSys.util.EmailUtil;
import com.cronoteSys.util.GenHash;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.animation.Animation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.PopupWindow.AnchorLocation;
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
	JFXPopup popupPass = new JFXPopup();
	private List<Node> allLabels;
	private boolean bPasswordOk;
	private LoginVO objLogin;
	final StackPane passwordExplanationPane = new StackPane();
	PasswordMatchValidator ps = new PasswordMatchValidator();
	
	@FXML
	protected void initialize() {
		ps.setMessage("SENHAS NÃO COMBINAM");
		try {
			Parent lp = ScreenUtil.loadTemplate("/popups/PasswordExplanation").load();
			passwordExplanationPane.getChildren().add(lp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		popupPass.setPopupContent(passwordExplanationPane);

		Node[] lstFieldsToValidation = { txtName, txtEmail, txtPwd, txtConfirmPwd };
		Boolean[] areNotNullFields = { true, true, true, true };
		Boolean[] areEmailFields = { false, true, false, false };
		ScreenUtil.addInlineValidation(lstFieldsToValidation, areNotNullFields, areEmailFields);
		final List<Node> lstPasswordNodes = new ArrayList<Node>();
		lstPasswordNodes.add(txtPwd);
		lstPasswordNodes.add(txtConfirmPwd);
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
					popupPass.setX(txtPwd.getLayoutX());
					popupPass.setY(txtPwd.getLayoutY() + 200);
					popupPass.show(popupPoint);
				}
				if (!newValue) {
					popupPass.hide();
					
				}

			}

		});
		txtConfirmPwd.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				ps.setRegexPattern(txtPwd.getText());
				if (!newValue) {
//					bPasswordOk = ScreenUtil.verifyPassFields(txtConfirmPwd.getText().trim(), txtPwd.getText().trim(),
//							lstPasswordNodes, lstPasswordLabelNodes);
					if(txtConfirmPwd.validate()) {
						txtConfirmPwd.getValidators().remove(ps);
						txtConfirmPwd.getValidators().add(ps);
						bPasswordOk = txtConfirmPwd.validate();
						System.out.println(bPasswordOk);
					}
				}
			}
		});
	}

	@FXML
	public void btnSignUpClicked() {
		hiddenAllLabels();
		//if (new ScreenUtil().isFilledFields(getThisStage(), pnlInput, false)) {
			String sEmail = txtEmail.getText().trim();
			if (!EmailUtil.validateEmail(sEmail)) {
				return;
			}
			
			if (new LoginBO().loginExists(sEmail)>0) {
				txtEmail.getStyleClass().add("error");
			}
			if (!bPasswordOk) {
				txtConfirmPwd.getStyleClass().add("error");
				txtPwd.getStyleClass().add("error");
				return;
			}
			String sPassPureText = txtPwd.getText().trim();
			String sPassEncrypted = new GenHash().hashIt(sPassPureText);

			UserVO objUser = new UserVO();
//			objUser.setIdUser(5);
			objUser.setCompleteName(txtName.getText().trim());
			objUser.setEmailRecover(txtSecondEmail.getText().trim());
			objUser.setBirthDate(dateBirthday.getValue());
			objUser.setStats(Byte.parseByte("1"));
			objUser = new UserBO().save(objUser);
//			objLogin.setIdLogin(0);
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
		//}
	}

	private void hiddenAllLabels() {
		allLabels = new ArrayList<Node>();

		for (Node n : allLabels) {
			if (n.getStyleClass().contains("show")) {
				n.getStyleClass().remove("show");
				n.getStyleClass().add("hide");
			}
		}
	}
}