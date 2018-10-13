package com.cronoteSys.model.dao;

import com.cronoteSys.model.vo.ActivityVO;

public class ActivityDAO extends GenericsDAO<ActivityVO, Integer> {
	
	public ActivityDAO() {
		super(ActivityVO.class);
	}
		
}