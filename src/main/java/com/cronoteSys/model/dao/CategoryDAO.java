package com.cronoteSys.model.dao;

import com.cronoteSys.model.vo.CategoryVO;

public class CategoryDAO extends GenericsDAO<CategoryVO, Integer> {

	public CategoryDAO() {
		super(CategoryVO.class);
	}
}
