package com.cronoteSys.model.bo;

import com.cronoteSys.model.dao.ExecutionTimeDAO;
import com.cronoteSys.model.vo.ExecutionTimeVO;

import java.util.List;

public class ExecutionTimeBO {
	public ExecutionTimeBO() {
		
	}
	
	public boolean save(ExecutionTimeVO executionTimeVO) {
		return new ExecutionTimeDAO().save(executionTimeVO);
	}
	
	public void update(ExecutionTimeVO executionTimeVO) {
		new ExecutionTimeDAO().update(executionTimeVO);
	}
	
	public void delete(ExecutionTimeVO executionTimeVO) {
		new ExecutionTimeDAO().delete(executionTimeVO.get_id_Execution_Time());
	}
	
	public List<ExecutionTimeVO> listAll(){
		return new ExecutionTimeDAO().getList();
	}
}
