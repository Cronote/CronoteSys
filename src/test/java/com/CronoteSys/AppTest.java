package com.CronoteSys;

import java.time.Duration;

import org.junit.Test;

import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.CategoryBO;
import com.cronoteSys.model.bo.ExecutionTimeBO;
import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.dao.UserDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.StatusEnum;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	@Test
	public void testInsertCategory() {
		UserDAO userDao = new UserDAO();
		CategoryBO catBO = new CategoryBO();

		CategoryVO cat = new CategoryVO();
		cat.setDescription("Planejamento");
		cat.setUserVO(userDao.find(1));
		catBO.save(cat);

	}

	@Test
	public void testInsertActivity() {
//		Adding an activity
		ActivityBO acBo = new ActivityBO();
		CategoryDAO catDao = new CategoryDAO();

		ActivityVO ac = new ActivityVO();
		CategoryVO cat = catDao.find(2);
		ac.setTitle("Atividade 2");
		ac.setCategoryVO(cat);
		Duration d = Duration.ofMinutes(4);
		ac.setEstimatedTime(d);
		ac.setPriority(0);
		ac.setUserVO(cat.getUserVO());

		acBo.save(ac);
	}

	@Test
	public void testStartingActivity() {
		ActivityDAO acDao = new ActivityDAO();
		ActivityBO acBo = new ActivityBO();
		ExecutionTimeBO executionTimeBO = new ExecutionTimeBO();

		ActivityVO activityVO = acDao.find(24);

		executionTimeBO.startExecution(activityVO);
		// TODO: futuramente o status tem que ser calculado, tem 2 status para em
		// progress
		acBo.switchStatus(activityVO, StatusEnum.NORMAL_IN_PROGRESS);

	}

	@Test
	public void testPausingActivity() {
		ActivityBO acBo = new ActivityBO();
		ExecutionTimeBO executionTimeBO = new ExecutionTimeBO();
		ActivityDAO acDao = new ActivityDAO();

		ActivityVO activityVO = acDao.find(24);

		executionTimeBO.finishExecution(activityVO);
		// TODO: futuramente o status tem que ser calculado, tem 2 status para paused
		acBo.switchStatus(activityVO, StatusEnum.NORMAL_PAUSED);
		
	}
}
