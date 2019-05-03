package com.cronoteSys.converter;

import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.UserVO;
import com.google.inject.Inject;

import javafx.util.StringConverter;

public class CategoryConverter extends StringConverter<CategoryVO>  {
	@Inject CategoryDAO catDAO;
	UserVO userVO ;
	public CategoryConverter(UserVO user ) {
		this.userVO = user;
	}

	@Override
	public String toString(CategoryVO object) {
		return object != null ? object.getDescription() : null;
	}

	@Override
	public CategoryVO fromString(String string) {
		CategoryVO cat = catDAO.findByDescriptionAndUser(string,userVO);
		return cat;
	}

}
