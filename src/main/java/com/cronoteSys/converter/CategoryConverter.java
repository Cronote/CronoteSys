package com.cronoteSys.converter;

import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.CategoryVO;

import javafx.util.StringConverter;

public class CategoryConverter extends StringConverter<CategoryVO>  {
	CategoryDAO catDAO;

	public CategoryConverter() {
		catDAO = new CategoryDAO();
	}

	@Override
	public String toString(CategoryVO object) {

		return object != null ? object.get_description() : null;
	}

	@Override
	public CategoryVO fromString(String string) {
		CategoryVO cat = catDAO.findByDescription(string);
		return cat;
	}

}
