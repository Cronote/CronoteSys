package com.cronoteSys.model.bo;

import java.time.LocalDateTime;
import java.util.List;

import com.cronoteSys.model.dao.ExecutionTimeDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ExecutionTimeVO;

public class ExecutionTimeBO {
	ExecutionTimeDAO execDAO;

	public ExecutionTimeBO() {
		execDAO = new ExecutionTimeDAO();
	}

	public void delete(ExecutionTimeVO executionTimeVO) {
		execDAO.delete(executionTimeVO.getId());
	}

	public void startExecution(ActivityVO ac) {
		if (execDAO.executionInProgressByUser(ac.getUserVO()) == 0) {
			ExecutionTimeVO exec = new ExecutionTimeVO();
			exec.setActivityVO(ac);
			exec.setStartDate(LocalDateTime.now());
			execDAO.saveOrUpdate(exec);
		}else {
			System.out.println("Atividades simultâneas não permitido");
			//TODO: devolver mecanismos para avisar que o usuario não pode executar atividades simultâneas
		}
	}

	public void finishExecution(ActivityVO ac) {
		ExecutionTimeVO executionTimeVO = execDAO.executionInProgress(ac);
		executionTimeVO.setFinishDate(LocalDateTime.now());
		execDAO.saveOrUpdate(executionTimeVO);
	}

	public List<ExecutionTimeVO> listAll() {
		return execDAO.getList();
	}
}
