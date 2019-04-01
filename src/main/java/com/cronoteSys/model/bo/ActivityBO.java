package com.cronoteSys.model.bo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;

public class ActivityBO {
	ActivityDAO acDAO;

	public ActivityBO() {
		acDAO = new ActivityDAO();
	}

	public ActivityVO save(ActivityVO activityVO) {
		if (activityVO.get_id_Activity() == null) {
			activityVO.set_stats(StatusEnum.NOT_STARTED);
		}
		activityVO.set_last_Modification(LocalDate.now());
		activityVO = acDAO.saveOrUpdate(activityVO);
		notifyAllListeners(activityVO);
		return activityVO;
	}

	public void update(ActivityVO activityVO) {
		acDAO.saveOrUpdate(activityVO);
	}

	public void delete(ActivityVO activityVO) {
		acDAO.delete(activityVO.get_id_Activity());
	}

	public void switchStatus(ActivityVO ac, StatusEnum stats) {
		ac.set_stats(stats);
		acDAO.saveOrUpdate(ac);
	}

	public List<ActivityVO> listAll() {
		return acDAO.getList();
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
