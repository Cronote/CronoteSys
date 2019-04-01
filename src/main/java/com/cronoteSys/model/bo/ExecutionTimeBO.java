package com.cronoteSys.model.bo;

import com.cronoteSys.model.dao.ExecutionTimeDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ExecutionTimeVO;

import java.time.LocalDate;
import java.util.List;

public class ExecutionTimeBO {
	ExecutionTimeDAO execDAO;

	public ExecutionTimeBO() {
		execDAO = new ExecutionTimeDAO();
	}

	public void delete(ExecutionTimeVO executionTimeVO) {
		execDAO.delete(executionTimeVO.get_id_Execution_Time());
	}

	public void startExecution(ActivityVO ac) {
		if (execDAO.executionInProgressByUser(ac.get_userVO()) == 0) {
			ExecutionTimeVO exec = new ExecutionTimeVO();
			exec.set_ActivityVO(ac);
			exec.set_start_Date(LocalDate.now());
			execDAO.saveOrUpdate(exec);
		}else {
			System.out.println("Atividades simultâneas não permitido");
			//TODO: devolver mensagem para avisar que o usuario não pode executar atividades simultâneas
		}
	}

	public void finishExecution(ActivityVO ac) {
		ExecutionTimeVO executionTimeVO = execDAO.executionInProgress(ac);
		executionTimeVO.set_finish_Date(LocalDate.now());
		execDAO.saveOrUpdate(executionTimeVO);
	}

	public List<ExecutionTimeVO> listAll() {
		return execDAO.getList();
	}
}
