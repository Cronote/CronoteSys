package com.CronoteSys;

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
		cat.set_description("Programação");
		cat.set_userVO(userDao.find(7));
		catBO.save(cat);

	}

	@Test
	public void testInsertActivity() {
//		Adding an activity
		ActivityBO acBo = new ActivityBO();
		CategoryDAO catDao = new CategoryDAO();

		ActivityVO ac = new ActivityVO();
		CategoryVO cat = catDao.find(2);
		ac.set_title("Atividade 2");
		ac.set_categoryVO(cat);
		ac.set_estimated_Time("30 minutos");
		ac.set_priority(0);
		ac.set_userVO(cat.get_userVO());

		acBo.save(ac);
	}

	@Test
	public void testStartingActivity() {
		ActivityDAO acDao = new ActivityDAO();
		ActivityBO acBo = new ActivityBO();
		ExecutionTimeBO executionTimeBO = new ExecutionTimeBO();

		ActivityVO activityVO = acDao.find(2);

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

		ActivityVO activityVO = acDao.find(2);

		executionTimeBO.finishExecution(activityVO);
		// TODO: futuramente o status tem que ser calculado, tem 2 status para paused
		acBo.switchStatus(activityVO, StatusEnum.NORMAL_PAUSED);
	}
}
