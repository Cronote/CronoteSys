package com.cronoteSys.model.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.HibernateUtil;

public abstract class GenericsDAO<T, I extends Serializable> {

	// Fonte preciosa https://developer.jboss.org/wiki/GenericDataAccessObjects
	protected EntityManager entityManager = HibernateUtil.getEntityManager();

	private Class<T> persistedClass;

	protected GenericsDAO() {
	}

	protected GenericsDAO(Class<T> persistedClass) {
		this();
		this.persistedClass = persistedClass;
	}

	public T saveOrUpdate(T entity) {
		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		entity = entityManager.merge(entity);
		entityManager.flush();
		t.commit();
		return entity;
	}

	public void delete(I id) {
		T entity = find(id);
		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		T mergedEntity = entityManager.merge(entity);
		entityManager.remove(mergedEntity);
		entityManager.flush();
		t.commit();
	}

	public List<T> getList() {
		return entityManager.createQuery("select c from " + persistedClass.getSimpleName() + " c").getResultList();
	}

	public List<T> getList(UserVO user) {
		Query q = entityManager
				.createQuery("SELECT p FROM " + persistedClass.getSimpleName() + " p WHERE p._userVO = :user");
		q.setParameter("user", user);
		return q.getResultList();
	}

	public List<T> getList(String col, String search) {
		Query q = entityManager
				.createQuery("SELECT p FROM " + persistedClass.getSimpleName() + " p WHERE p." + col + " LIKE :search");
		q.setParameter("search", search + "%");
		return q.getResultList();
	}

	public T find(I id) {
		return entityManager.find(persistedClass, id);
	}
}
