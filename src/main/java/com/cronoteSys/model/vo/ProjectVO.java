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
@Table(name = "tb_project")
public class ProjectVO implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer _id_Project;
	private String _title;
	private String _description;
	private LocalDate _last_Modification;
	private LocalDate _start_Date;
	private LocalDate _finish_Date;
	private Integer _stats;
	private UserVO _userVO;
	
	public ProjectVO() {
		
	}
	
	public ProjectVO(int idProject, String title, String description, LocalDate lastModification, LocalDate startDate, LocalDate finishDate, int stats, UserVO userVO) {
		this._id_Project = idProject;
		this._title = title;
		this._description = description;
		this._last_Modification = lastModification;
		this._start_Date = startDate;
		this._finish_Date = finishDate;
		this._stats = stats;
		this._userVO = userVO;
	}

	@Id
	@Column(name = "id_project")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer get_id_Project() {
		return _id_Project;
	}

	public void set_id_Project(Integer _id_Project) {
		this._id_Project = _id_Project;
	}

	@Column(name = "title", nullable = false)
	public String get_title() {
		return _title;
	}

	public void set_title(String _title) {
		this._title = _title;
	}

	@Column(name = "description", nullable = false)
	public String get_description() {
		return _description;
	}

	public void set_description(String _description) {
		this._description = _description;
	}

	@Column(name = "last_modification", nullable = false)
	public LocalDate get_last_Modification() {
		return _last_Modification;
	}

	public void set_last_Modification(LocalDate _last_Modification) {
		this._last_Modification = _last_Modification;
	}

	@Column(name = "start_date", nullable = false)
	public LocalDate get_start_Date() {
		return _start_Date;
	}

	public void set_start_Date(LocalDate _start_Date) {
		this._start_Date = _start_Date;
	}

	@Column(name = "finish_date", nullable = false)
	public LocalDate get_finish_Date() {
		return _finish_Date;
	}

	public void set_finish_Date(LocalDate _finish_Date) {
		this._finish_Date = _finish_Date;
	}

	@Column(name = "finish_date", nullable = false)
	public Integer get_stats() {
		return _stats;
	}

	public void set_stats(Integer _stats) {
		this._stats = _stats;
	}
	
	@ManyToOne(targetEntity = UserVO.class)
	@JoinColumn(name = "id_user")
	@Fetch(FetchMode.SELECT)
	public UserVO get_userVO() {
		return _userVO;
	}

	public void set_userVO(UserVO userVO) {
		this._userVO = userVO;
	}

}
