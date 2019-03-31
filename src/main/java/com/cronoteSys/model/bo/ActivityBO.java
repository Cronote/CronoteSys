package com.cronoteSys.model.bo;

import java.time.LocalDate;
import java.util.List;

import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.StatusEnum;

public class ActivityBO {
	ActivityDAO acDAO;

	public ActivityBO() {
		acDAO = new ActivityDAO();
	}

	public boolean save(ActivityVO activityVO) {
		if (activityVO.get_id_Activity() == null) {
			activityVO.set_stats(StatusEnum.NOT_STARTED);
		}
		activityVO.set_last_Modification(LocalDate.now());
		return acDAO.save(activityVO);
	}

	public void update(ActivityVO activityVO) {
		acDAO.update(activityVO);
	}

	public void delete(ActivityVO activityVO) {
		acDAO.delete(activityVO.get_id_Activity());
	}

	public void switchStatus(ActivityVO ac, StatusEnum stats) {
		ac.set_stats(stats);
		acDAO.update(ac);
	}

	public List<ActivityVO> listAll() {
		return acDAO.getList();
	}
}
