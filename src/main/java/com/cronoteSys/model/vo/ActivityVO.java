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
@Table(name = "tb_activity")
public class ActivityVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer _id_Activity;
	private String _title;
	private String _estimated_Time;
	private StatusEnum _stats;
	private LocalDate _realtime;
	private Integer _priority;
	private LocalDate _last_Modification;
	private UserVO _userVO;
	private ProjectVO _projectVO;
	private CategoryVO _categoryVO;
	
	public ActivityVO() {
		
	}
	
	public ActivityVO(int idActivity, String title, String estimatedTime, StatusEnum stats, LocalDate realtime, Integer priority, LocalDate lastModification, UserVO userVO, ProjectVO projectVO, CategoryVO categoryVO) {
		this._id_Activity = idActivity;
		this._title = title;
		this._estimated_Time = estimatedTime;
		this._stats = stats;
		this._realtime = realtime;
		this._priority = priority;
		this._last_Modification = lastModification;
		this._userVO = userVO;
		this._projectVO = projectVO;
		this._categoryVO = categoryVO;
	}
	
	@Id
	@Column(name = "id_activity", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer get_id_Activity() {
		return _id_Activity;
	}
	public void set_id_Activity(Integer _id_Activity) {
		this._id_Activity = _id_Activity;
	}

	@Column(name = "title", nullable = false)
	public String get_title() {
		return _title;
	}

	public void set_title(String _title) {
		this._title = _title;
	}

	@Column(name = "estimated_time", nullable = false)
	public String get_estimated_Time() {
		return _estimated_Time;
	}

	public void set_estimated_Time(String _estimated_Time) {
		this._estimated_Time = _estimated_Time;
	}

	@Column(name = "stats", nullable = false)
	public StatusEnum get_stats() {
		return _stats;
	}

	public void set_stats(StatusEnum _stats) {
		this._stats = _stats;
	}

	@Column(name = "real_time", nullable = false)
	public LocalDate get_realtime() {
		return _realtime;
	}

	public void set_realtime(LocalDate _realtime) {
		this._realtime = _realtime;
	}

	@Column(name = "priority", nullable = false)
	public Integer get_priority() {
		return _priority;
	}

	public void set_priority(Integer _priority) {
		this._priority = _priority;
	}

	@Column(name = "last_modification", nullable = false)
	public LocalDate get_last_Modification() {
		return _last_Modification;
	}

	public void set_last_Modification(LocalDate _last_Modification) {
		this._last_Modification = _last_Modification;
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
	
	@ManyToOne(targetEntity = ProjectVO.class)
	@JoinColumn(name = "id_project")
	@Fetch(FetchMode.SELECT)
	public ProjectVO get_projectVO() {
		return _projectVO;
	}

	public void set_projectVO(ProjectVO projectVO) {
		this._projectVO = projectVO;
	}

	@ManyToOne(targetEntity = CategoryVO.class)
	@JoinColumn(name = "id_category")
	@Fetch(FetchMode.SELECT)
	public CategoryVO get_categoryVO() {
		return _categoryVO;
	}

	public void set_categoryVO(CategoryVO categoryVO) {
		this._categoryVO = categoryVO;
	}
	
	

}
