package com.cronoteSys.model.vo;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_execution_time")
public class ExecutionTimeVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private LocalDateTime startDate;
	private LocalDateTime finishDate;
	private ActivityVO activityVO;
	
	public ExecutionTimeVO() {
		
	}
	
	public ExecutionTimeVO(int idExecutionTime, LocalDateTime startDate, LocalDateTime finishDate, ActivityVO activityVO) {
		this.setId(idExecutionTime);
		this.setStartDate(startDate);
		this.finishDate = finishDate;
		this.activityVO = activityVO;
	}
	
	@Id
	@Column(name = "id_execution_time")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "start_date")
	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	
	@Column(name = "finish_date")
	public LocalDateTime getFinishDate() {
		return finishDate;
	}
	
	public void setFinishDate(LocalDateTime finishDate) {
		this.finishDate = finishDate;
	}
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "id_activity", referencedColumnName = "id_activity")
	public ActivityVO getActivityVO() {
		return activityVO;
	}
	
	public void setActivityVO(ActivityVO activityVO) {
		this.activityVO = activityVO;
	}

	
	
	
}
