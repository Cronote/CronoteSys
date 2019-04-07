package com.cronoteSys.model.vo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_activity")
public class ActivityVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String title;
	private String description;
	private Duration estimatedTime;
	private UnityTimeEnum estimatedTimeUnit;
	private StatusEnum stats;
	private Duration realtime;
	private Integer priority;
	private LocalDateTime lastModification;
	private UserVO userVO;
	private ProjectVO projectVO;
	private CategoryVO categoryVO;

	public ActivityVO() {

	}

	public ActivityVO(int idActivity, String title, Duration estimatedTime, StatusEnum stats, Duration realtime,
			Integer priority, LocalDateTime lastModification, UserVO userVO, ProjectVO projectVO,
			CategoryVO categoryVO) {
		this.id = idActivity;
		this.title = title;
		this.estimatedTime = estimatedTime;
		this.stats = stats;
		this.realtime = realtime;
		this.priority = priority;
		this.lastModification = lastModification;
		this.userVO = userVO;
		this.projectVO = projectVO;
		this.categoryVO = categoryVO;
	}

	@Id
	@Column(name = "id_activity", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "title", nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "description", nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "estimated_time", nullable = false)
	public Duration getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(Duration estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	@Column(name = "estimated_time_unity", nullable = false)
	public UnityTimeEnum getEstimatedTimeUnit() {
		return estimatedTimeUnit;
	}

	public void setEstimatedTimeUnit(UnityTimeEnum estimatedTimeUnit) {
		this.estimatedTimeUnit = estimatedTimeUnit;
	}

	@Column(name = "stats", nullable = false)
	public StatusEnum getStats() {
		return stats;
	}

	public void setStats(StatusEnum stats) {
		this.stats = stats;
	}

	@Column(name = "real_time", nullable = true)
	public Duration getRealtime() {
		return realtime;
	}

	public void setRealtime(Duration realtime) {
		this.realtime = realtime;
	}

	@Column(name = "priority", nullable = false)
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Column(name = "last_modification")
	public LocalDateTime getLastModification() {
		return lastModification;
	}

	public void setLastModification(LocalDateTime lastModification) {
		this.lastModification = lastModification;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_user", referencedColumnName = "id_user")
	public UserVO getUserVO() {
		return userVO;
	}

	public void setUserVO(UserVO userVO) {
		this.userVO = userVO;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "id_project", referencedColumnName = "id_project")
	public ProjectVO getProjectVO() {
		return projectVO;
	}

	public void setProjectVO(ProjectVO projectVO) {
		this.projectVO = projectVO;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_category", referencedColumnName = "id_category")
	public CategoryVO getCategoryVO() {
		return categoryVO;
	}

	public void setCategoryVO(CategoryVO categoryVO) {
		this.categoryVO = categoryVO;
	}

	@Transient
	public String getEstimatedTimeAsString() {
		long horas = getEstimatedTime().toHours();
		Duration minutos = getEstimatedTime().minus(horas, ChronoUnit.HOURS);
		return String.format("%02d:%02d ", horas, minutos.toMinutes());
	}

	@Transient
	public String getRealtimeAsString() {
		long horas = getRealtime().toHours();
		Duration minutos = getRealtime().minus(horas, ChronoUnit.HOURS);
		return String.format("%02d:%02d ", horas, minutos.toMinutes());
	}

	@Override
	public String toString() {
		return "ActivityVO [_id_Activity=" + id + ", _title=" + title + ", _description=" + description
				+ ", _estimated_Time=" + estimatedTime + ", _stats=" + stats + ", _realtime=" + realtime
				+ ", _priority=" + priority + ", _last_Modification=" + lastModification + ", _userVO=" + userVO
				+ ", _projectVO=" + projectVO + ", _categoryVO=" + categoryVO + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryVO == null) ? 0 : categoryVO.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((estimatedTime == null) ? 0 : estimatedTime.hashCode());
		result = prime * result + ((estimatedTimeUnit == null) ? 0 : estimatedTimeUnit.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastModification == null) ? 0 : lastModification.hashCode());
		result = prime * result + ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + ((projectVO == null) ? 0 : projectVO.hashCode());
		result = prime * result + ((realtime == null) ? 0 : realtime.hashCode());
		result = prime * result + ((stats == null) ? 0 : stats.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((userVO == null) ? 0 : userVO.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivityVO other = (ActivityVO) obj;
		if (categoryVO == null) {
			if (other.categoryVO != null)
				return false;
		} else if (!categoryVO.equals(other.categoryVO))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (estimatedTime == null) {
			if (other.estimatedTime != null)
				return false;
		} else if (!estimatedTime.equals(other.estimatedTime))
			return false;
		if (estimatedTimeUnit != other.estimatedTimeUnit)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastModification == null) {
			if (other.lastModification != null)
				return false;
		} else if (!lastModification.equals(other.lastModification))
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		if (projectVO == null) {
			if (other.projectVO != null)
				return false;
		} else if (!projectVO.equals(other.projectVO))
			return false;
		if (realtime == null) {
			if (other.realtime != null)
				return false;
		} else if (!realtime.equals(other.realtime))
			return false;
		if (stats != other.stats)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (userVO == null) {
			if (other.userVO != null)
				return false;
		} else if (!userVO.equals(other.userVO))
			return false;
		return true;
	}

}
