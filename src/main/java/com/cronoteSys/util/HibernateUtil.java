/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.util;

import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.SessionFactory;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author bruno
 */
public class HibernateUtil {

	public static EntityManagerFactory factory = null;

	static {
		init();
	}

	private static void init() {
		try {
			if (factory == null) {
				factory = Persistence.createEntityManagerFactory("jpahibernate");

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static EntityManager getEntityManager() {
		return factory.createEntityManager(); // Prove a parte de persistência
	}
}
