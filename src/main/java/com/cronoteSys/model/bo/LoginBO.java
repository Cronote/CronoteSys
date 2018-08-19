/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.model.bo;

import com.cronoteSys.model.dao.LoginDAO;
import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.UserVO;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author bruno
 */
public class LoginBO {

    public boolean save(LoginVO login) {
        return new LoginDAO().save(login);
    }

    public void update(LoginVO login) {
        new LoginDAO().update(login);
    }

    public void delete(LoginVO login) {
        new LoginDAO().delete(login);
    }

    public List<LoginVO> listAll() {
        return new LoginDAO().listAll();
    }

    public UserVO login(LoginVO login) {
        UserVO user = new LoginDAO().verifiedUser(login.getEmail(), login.getPasswd());
        return (user!=null && user.getStats() ==1) ? user : null;
    }

    public LoginVO loginExists(String sEmail){
        return new LoginDAO().loginExists(sEmail);
    }
    
	public boolean validatePassword(String passwd) {
		boolean bHaveNumber = false;
		boolean bHaveSpecialChar = false;
		for (char c : passwd.toCharArray()) {
			if ("0123456789".contains(c + "")) {
				bHaveNumber = true;
				break;
			}
		}
		if (!passwd.matches(".*[a-z]+.*"))
			return false; // n tem letra
		if (!passwd.matches(".*[A-Z]+.*"))
			return false; // n tem LETRA
		for (char c : passwd.toCharArray()) {
			if ("#@/\\\\%$&*!?<>.-_)(".contains(c + "")) {
				bHaveSpecialChar = true;
			}
		}
		if (!(bHaveNumber && bHaveSpecialChar))
			return false;
		return true;
	}
}
