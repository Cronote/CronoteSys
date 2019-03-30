package com.CronoteSys;

import java.time.LocalDate;

import org.junit.Test;

import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.dao.ExecutionTimeDAO;
import com.cronoteSys.model.dao.UserDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.ExecutionTimeVO;
import com.cronoteSys.model.vo.StatusEnum;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	@Test
	public void testInsertCategory() {
		UserDAO userDao = new UserDAO();
		CategoryDAO catDao = new CategoryDAO();

		CategoryVO cat = new CategoryVO();
		cat.set_description("Programção");
		cat.set_userVO(userDao.find(7));
		catDao.save(cat);

	}

	@Test
	public void testInsertActivity() {
//		Adding an activity
		ActivityDAO acDao = new ActivityDAO();
		CategoryDAO catDao = new CategoryDAO();

		ActivityVO ac = new ActivityVO();
		CategoryVO cat = catDao.find(1);
		ac.set_categoryVO(cat);
		ac.set_estimated_Time("zero");
		ac.set_priority(0);
		ac.set_stats(StatusEnum.NOT_STARTED);
		ac.set_title("Atividade 2");
		ac.set_userVO(cat.get_userVO());

		acDao.save(ac);
	}
	public void testStartingActivity() {
		
//		Starting Activity
		ActivityDAO acDao = new ActivityDAO();
		ExecutionTimeDAO executionTimeDAO = new ExecutionTimeDAO();
		
		ActivityVO activityVO = acDao.find(2);
		ExecutionTimeVO executionTimeVO = new ExecutionTimeVO();
		
		executionTimeVO.set_ActivityVO(activityVO);
				
		executionTimeDAO.save(executionTimeVO);
		activityVO.set_stats(StatusEnum.NORMAL_IN_PROGRESS);
		acDao.update(activityVO);
		
	}
	
	public void testPausingActivity() {
//		Pausing Activity
		ActivityDAO acDao = new ActivityDAO();
		ExecutionTimeDAO executionTimeDAO = new ExecutionTimeDAO();
		
		ActivityVO activityVO = acDao.find(2);
		ExecutionTimeVO executionTimeVO = executionTimeDAO.executionInProgress(activityVO);
		
		executionTimeVO.set_finish_Date(LocalDate.now());
		executionTimeDAO.update(executionTimeVO);
		activityVO.set_stats(StatusEnum.NORMAL_PAUSED);
		acDao.update(activityVO);
	}
}
