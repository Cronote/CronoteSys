package com.cronoteSys.model.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ExecutionTimeVO;
import com.cronoteSys.model.vo.UserVO;

public class ExecutionTimeDAO extends GenericsDAO<ExecutionTimeVO, Integer> {

	public ExecutionTimeDAO() {
		super(ExecutionTimeVO.class);
	}

	public List<ExecutionTimeVO> listByActivity(ActivityVO activityVO) {
		Query q = entityManager.createQuery(
				"SELECT p FROM " + ExecutionTimeVO.class.getSimpleName() + " p WHERE p.activityVO = :activity");
		q.setParameter("activity", activityVO);
		return q.getResultList();
	}

	public int executionInProgressByUser(UserVO userVO) {
		Query q = entityManager.createQuery("SELECT p FROM " + ExecutionTimeVO.class.getSimpleName()
				+ " p WHERE p.finishDate = null and p.activityVO.userVO = :user");
		q.setParameter("user", userVO);
		return q.getResultList().size();

	}

	public ExecutionTimeVO executionInProgress(ActivityVO activityVO) {
		Query q = entityManager.createQuery("SELECT p FROM " + ExecutionTimeVO.class.getSimpleName()
				+ " p WHERE p.activityVO = :activity and p.finishDate = null");
		q.setParameter("activity", activityVO);
		try {
			return (ExecutionTimeVO) q.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}

	}

}
