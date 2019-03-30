package com.cronoteSys.model.dao;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ExecutionTimeVO;

public class ExecutionTimeDAO extends GenericsDAO<ExecutionTimeVO, Integer> {

	public ExecutionTimeDAO() {
		super(ExecutionTimeVO.class);
	}
	
	public ExecutionTimeVO executionInProgress(ActivityVO activityVO) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExecutionTimeVO> query = builder.createQuery(ExecutionTimeVO.class);
        Query q = entityManager.createQuery("SELECT p FROM "
                + ExecutionTimeVO.class.getSimpleName()
                + " p WHERE p._ActivityVO = :activity and p._finish_Date = null");
        q.setParameter("activity",activityVO);
        try {
        	return (ExecutionTimeVO) q.getSingleResult();
        }catch(NoResultException e){
        	e.printStackTrace();
        	return null;
        }
		 
	}
	
}
