package com.cronoteSys.model.vo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "tb_execution_time")
public class ExecutionTimeVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer _id_Execution_Time;
	private LocalDate _start_Date;
	private LocalDate _finish_Date;
	private ActivityVO _ActivityVO;
	
	public ExecutionTimeVO() {
		
	}
	
	public ExecutionTimeVO(int idExecutionTime, LocalDate startDate, LocalDate finishDate, ActivityVO activityVO) {
		this.set_id_Execution_Time(idExecutionTime);
		this.set_start_Date(startDate);
		this._finish_Date = finishDate;
		this._ActivityVO = activityVO;
	}
	
	@Id
	@Column(name = "id_estimated_time")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer get_id_Execution_Time() {
		return _id_Execution_Time;
	}

	public void set_id_Execution_Time(Integer _id_Execution_Time) {
		this._id_Execution_Time = _id_Execution_Time;
	}

	@Column(name = "start_date")
	public LocalDate get_start_Date() {
		return _start_Date;
	}

	public void set_start_Date(LocalDate _start_Date) {
		this._start_Date = _start_Date;
	}
	
	@Column(name = "finish_date")
	public LocalDate get_finish_Date() {
		return _finish_Date;
	}
	
	public void set_finish_Date(LocalDate _finish_Date) {
		this._finish_Date = _finish_Date;
	}
	
	@ManyToOne(targetEntity = ActivityVO.class)
	@JoinColumn(name = "id_Activity")
	@Fetch(FetchMode.SELECT)
	public ActivityVO get_ActivityVO() {
		return _ActivityVO;
	}
	
	public void set_ActivityVO(ActivityVO _ActivityVO) {
		this._ActivityVO = _ActivityVO;
	}

	
	
	
}
