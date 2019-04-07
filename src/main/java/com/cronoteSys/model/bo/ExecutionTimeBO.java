package com.cronoteSys.model.bo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.cronoteSys.model.dao.ActivityDAO;
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
		} else {
			System.out.println("Atividades simultâneas não permitido");
			// TODO: devolver mecanismos para avisar que o usuario não pode executar
			// atividades simultâneas
		}
	}

	public void finishExecution(ActivityVO ac) {
		ExecutionTimeVO executionTimeVO = execDAO.executionInProgress(ac);
		executionTimeVO.setFinishDate(LocalDateTime.now());
		execDAO.saveOrUpdate(executionTimeVO);
	}

	public Duration getRealTime(ActivityVO act) {
		List<ExecutionTimeVO> lst = execDAO.listByActivity(act);
		Duration sum = Duration.ZERO;
		if (lst.size() == 0)
			return sum;
		ExecutionTimeVO execInProgress = null;
		for (ExecutionTimeVO execution : lst) {
			if (execution.getFinishDate() == null) {
				execInProgress = execution;
				continue;
			}
			Duration d = Duration.between(execution.getStartDate(), execution.getFinishDate());
			sum = sum.plus(d);
		}
		if (execInProgress != null) {
			sum = sum.plus(Duration.between(execInProgress.getStartDate(), LocalDateTime.now()));
		}
		return sum;
	}

	public List<ExecutionTimeVO> listAll() {
		return execDAO.getList();
	}
}
