package com.cronoteSys.converter;

import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.UserVO;

import javafx.util.StringConverter;

public class CategoryConverter extends StringConverter<CategoryVO>  {
	CategoryDAO catDAO;
	UserVO userVO ;
	public CategoryConverter(UserVO user ) {
		catDAO = new CategoryDAO();
		this.userVO = user;
	}

	@Override
	public String toString(CategoryVO object) {

		return object != null ? object.get_description() : null;
	}

	@Override
	public CategoryVO fromString(String string) {
		CategoryVO cat = catDAO.findByDescriptionAndUser(string,userVO);
		return cat;
	}

}
