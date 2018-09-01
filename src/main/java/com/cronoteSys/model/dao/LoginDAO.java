package com.cronoteSys.model.dao;


import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.HibernateUtil;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LoginDAO extends GenericsDAO<LoginVO, Integer> {

	private Session session;
	
	public LoginDAO() {
		super(LoginVO.class);
	}

	/*public boolean save(LoginVO login) {
		session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(login);
			tx.commit();// faz a transacao
			session.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			tx.rollback();
		} finally {
			session.close();
		}

		return false;
	}

	public boolean update(LoginVO login) {
		session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(login);
			tx.commit();// faz a transacao
			session.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			tx.rollback();
		} finally {
			session.close();
		}

		return false;
	}

	public boolean delete(LoginVO login) {
		session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(login);
			tx.commit();// faz a transacao
			session.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			tx.rollback();
		} finally {
			session.close();
		}

		return false;
	}*/

	public List<LoginVO> listAll() {
		try {
			List<LoginVO> login;
			session = HibernateUtil.getSessionFactory().openSession();// obtem
																		// uma
																		// sessao
			login = session.createNativeQuery("SELECT * FROM tb_login", LoginVO.class).list();
			if (login.size() > 0) {
				return login;
			}

		} catch (HibernateException e) {
			System.out.println("Problem on list " + e.getMessage());
		}
		return null;
	}

	public UserVO verifiedUser(String email, String pass) {
		session = HibernateUtil.getSessionFactory().openSession();
		try {
			List<LoginVO> login = session
					.createNativeQuery("SELECT * FROM tb_login WHERE email  = '" + email + "';", LoginVO.class).list();
			if (login.size()>0) {
				if (login.get(0).getPasswd().equals(pass)) {
					UserVO user = login.get(0).getTbUser();
					Hibernate.initialize(user);
					session.close();
					return user;
				}
			}

		} catch (Exception e) {
			System.out.println("aaaaaaaaaa" + e.getMessage());
		} finally {
			session.close();
		}
		return null;
	}

	public LoginVO loginExists(String sEmail) {
		session = HibernateUtil.getSessionFactory().openSession();
		try {
			List<LoginVO> login = session
					.createNativeQuery("SELECT * FROM tb_login WHERE email = '" + sEmail + "';", LoginVO.class).list();
			if (login.size() > 0) {
				return login.get(0);
			}
			
		} catch (Exception e) {
			System.out.println("Erro na verificação de email: " + e.getMessage());
		} finally {
			session.close();
		}
		return null;
	}
}
