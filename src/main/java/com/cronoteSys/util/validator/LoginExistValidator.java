package com.cronoteSys.util.validator;

import com.cronoteSys.model.bo.LoginBO;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.base.ValidatorBase;

public class LoginExistValidator extends ValidatorBase {

	public LoginExistValidator() {
		message.set("E-MAIL JÁ EM USO!");
	}

	@Override
	protected void eval() {
		String email = ((JFXTextField) srcControl.getValue()).getText();
		hasErrors.set(new LoginBO().loginExists(email) > 0);
	}

	@Override
	protected void onEval() {
		// TODO Auto-generated method stub
		super.onEval();
		if (hasErrors.get())
			((JFXTextField) srcControl.getValue()).requestFocus();
	}
}
