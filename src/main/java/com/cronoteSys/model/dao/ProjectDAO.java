package com.cronoteSys.model.dao;

import com.cronoteSys.model.vo.ProjectVO;

public class ProjectDAO extends GenericsDAO<ProjectVO, Integer>{

	public ProjectDAO() {
		super(ProjectVO.class);
	}
}
