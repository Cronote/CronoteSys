package com.cronoteSys.model.bo;

import java.util.List;

import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.CategoryVO;

public class CategoryBO {
	
	public CategoryBO() {
		
	}
	
	public CategoryVO save(CategoryVO activityVO) {
		return new CategoryDAO().saveOrUpdate(activityVO);
	}
	
	public void update(CategoryVO activityVO) {
		new CategoryDAO().saveOrUpdate(activityVO);
	}
	
	public void delete(CategoryVO activityVO) {
		new CategoryDAO().delete(activityVO.get_id_Category());
	}
	
	public List<CategoryVO> listAll(){
		return new CategoryDAO().getList();
	}
}
