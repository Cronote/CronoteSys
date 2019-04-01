package com.cronoteSys.model.dao;

import javax.persistence.NoResultException;

import com.cronoteSys.model.vo.CategoryVO;

public class CategoryDAO extends GenericsDAO<CategoryVO, Integer> {

	public CategoryDAO() {
		super(CategoryVO.class);
	}

	public CategoryVO findByDescription(String descript) {
		try {
			return (CategoryVO) entityManager.createQuery("From c where c._description = :desc")
					.setParameter(":desc", descript).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
