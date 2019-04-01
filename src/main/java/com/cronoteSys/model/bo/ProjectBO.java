package com.cronoteSys.model.bo;

import java.time.LocalDate;
import java.util.List;

import com.cronoteSys.model.dao.ProjectDAO;
import com.cronoteSys.model.vo.ProjectVO;

public class ProjectBO {

	public void save(ProjectVO objProject) {
		new ProjectDAO().saveOrUpdate(objProject);
	}

	public void update(ProjectVO objProject) {
		new ProjectDAO().saveOrUpdate(objProject);
	}

	public void delete(ProjectVO objProject) {
		int projectID = objProject.get_id_Project();
		if(projectID == 0)
			return;
		new ProjectDAO().delete(projectID);
	}

	public List<ProjectVO> listAll() {
		List<ProjectVO> projects = new ProjectDAO().getList();
		if(projects.get(0).get_id_Project() == 0)
			projects.remove(0);
		return projects;
	}
	
	public void lastModificationToNow(ProjectVO objProject) {
		objProject.set_last_Modification(LocalDate.now());
		update(objProject);
	}
	
	public void changeStatus(ProjectVO objProject, int status) {
		objProject.set_stats(status);
		update(objProject);
	}
}
