package com.cronoteSys.model.bo;

import java.util.List;

import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.vo.ActivityVO;

public class ActivityBO {

	public ActivityBO() {
		
	}
	
	public boolean save(ActivityVO activityVO) {
		return new ActivityDAO().save(activityVO);
	}
	
	public void update(ActivityVO activityVO) {
		new ActivityDAO().update(activityVO);
	}
	
	public void delete(ActivityVO activityVO) {
		new ActivityDAO().delete(activityVO.get_id_Activity());
	}
	
	public List<ActivityVO> listAll(){
		return new ActivityDAO().getList();
	}
}
