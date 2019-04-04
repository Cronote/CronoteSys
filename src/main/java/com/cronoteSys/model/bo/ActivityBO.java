package com.cronoteSys.model.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.model.vo.UserVO;

public class ActivityBO {
	ActivityDAO acDAO;

	public ActivityBO() {
		acDAO = new ActivityDAO();
	}

	public ActivityVO save(ActivityVO activityVO) {
		if (activityVO.getId() == null) {
			activityVO.setStats(StatusEnum.NOT_STARTED);
		}
		activityVO.setLastModification(LocalDateTime.now());
		activityVO = acDAO.saveOrUpdate(activityVO);
		notifyAllListeners(activityVO);
		return activityVO;
	}

	public void update(ActivityVO activityVO) {
		acDAO.saveOrUpdate(activityVO);
	}

	public void delete(ActivityVO activityVO) {
		acDAO.delete(activityVO.getId());
	}

	public void switchStatus(ActivityVO ac, StatusEnum stats) {
		ac.setStats(stats);
		acDAO.saveOrUpdate(ac);
	}

	public List<ActivityVO> listAllByUser(UserVO user) {
		return acDAO.getList(user);
	}

	private static ArrayList<OnActivityAddedI> listeners = new ArrayList<OnActivityAddedI>();

	public interface OnActivityAddedI {
		void onActivityAddedI(ActivityVO act);
	}

	public static void addOnActivityAddedIListener(OnActivityAddedI newListener) {
		listeners.add(newListener);
	}

	private void notifyAllListeners(ActivityVO act) {
		for (OnActivityAddedI l : listeners) {
			l.onActivityAddedI(act);
		}
	}
}
