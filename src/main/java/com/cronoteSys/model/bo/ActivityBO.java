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
		notifyAllActivityAddedListeners(activityVO);
		return activityVO;
	}

	public ActivityVO update(ActivityVO activityVO) {
		return acDAO.saveOrUpdate(activityVO);
	}

	public void delete(ActivityVO activityVO) {
		acDAO.delete(activityVO.getId());
		notifyAllactivityDeletedListeners(activityVO);
	}

	public void switchStatus(ActivityVO ac, StatusEnum stats) {
		ac.setStats(stats);
		acDAO.saveOrUpdate(ac);
	}

	public List<ActivityVO> listAllByUser(UserVO user) {
		return acDAO.getList(user);
	}

	private static ArrayList<OnActivityAddedI> activityAddedListeners = new ArrayList<OnActivityAddedI>();

	public interface OnActivityAddedI {
		void onActivityAddedI(ActivityVO act);
	}

	public static void addOnActivityAddedIListener(OnActivityAddedI newListener) {
		activityAddedListeners.add(newListener);
	}

	private void notifyAllActivityAddedListeners(ActivityVO act) {
		for (OnActivityAddedI l : activityAddedListeners) {
			l.onActivityAddedI(act);
		}
	}

	private static ArrayList<OnActivityDeletedI> activityDeletedListeners = new ArrayList<OnActivityDeletedI>();

	public interface OnActivityDeletedI {
		void onActivityDeleted(ActivityVO act);
	}

	public static void addOnActivityDeletedListener(OnActivityDeletedI newListener) {
		activityDeletedListeners.add(newListener);
	}

	private void notifyAllactivityDeletedListeners(ActivityVO act) {
		for (OnActivityDeletedI l : activityDeletedListeners) {
			l.onActivityDeleted(act);
		}
	}
}
