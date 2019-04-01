package com.cronoteSys.model.dao;

import javax.persistence.NoResultException;

import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.UserVO;

public class CategoryDAO extends GenericsDAO<CategoryVO, Integer> {

	public CategoryDAO() {
		super(CategoryVO.class);
	}

	public CategoryVO findByDescriptionAndUser(String descript,UserVO user) {
		try {
			return (CategoryVO) entityManager.createQuery("From c where c._description = :desc and c_userVO= :user")
					.setParameter(":desc", descript)
					.setParameter("user", user).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
