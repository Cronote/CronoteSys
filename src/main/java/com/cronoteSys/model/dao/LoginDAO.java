package com.cronoteSys.model.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.HibernateUtil;

import com.cronoteSys.model.dao.GenericsDAO;

public class LoginDAO extends GenericsDAO<LoginVO, Integer> {

	public LoginDAO() {
		super(LoginVO.class);
	}

	public List<LoginVO> listAll() {
		try {
			List<LoginVO> login;
			// uma
			// sessao
			login = entityManager.createNativeQuery("SELECT * FROM tb_login", LoginVO.class).getResultList();
			if (login.size() > 0) {
				return login;
			}

		} catch (HibernateException e) {
			System.out.println("Problem on list " + e.getMessage());
		}
		return null;
	}

	public UserVO verifiedUser(String email, String pass) {
		try {
			List<LoginVO> login = entityManager
					.createNativeQuery("SELECT * FROM tb_login WHERE email  = '" + email + "';", LoginVO.class)
					.getResultList();
			if (login.size() > 0) {
				if (login.get(0).getPasswd().equals(pass)) {
					UserVO user = login.get(0).getTbUser();
					Hibernate.initialize(user);
					return user;
				}
			}

		} catch (Exception e) {
			System.out.println("Erro de verificação de usuario: " + e.getMessage());
		}
		return null;
	}

	public LoginVO loginExists(String sEmail) {
		try {
			List<LoginVO> login = entityManager
					.createNativeQuery("SELECT * FROM tb_login WHERE email = '" + sEmail + "';", LoginVO.class)
					.getResultList();
			if (login.size() > 0) {
				return login.get(0);
			}

		} catch (Exception e) {
			System.out.println("Erro na verificação de email: " + e.getMessage());
		}
		return null;
	}
}
