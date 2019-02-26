package com.cronoteSys.model.bo;

import java.util.List;

import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.CategoryVO;

public class CategoryBO {
	
	public CategoryBO() {
		
	}
	
	public boolean save(CategoryVO activityVO) {
		return new CategoryDAO().save(activityVO);
	}
	
	public void update(CategoryVO activityVO) {
		new CategoryDAO().update(activityVO);
	}
	
	public void delete(CategoryVO activityVO) {
		new CategoryDAO().delete(activityVO.get_id_Category());
	}
	
	public List<CategoryVO> listAll(){
		return new CategoryDAO().getList();
	}
}
