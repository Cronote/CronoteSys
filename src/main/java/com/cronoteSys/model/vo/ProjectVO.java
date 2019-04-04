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
@Table(name = "tb_project")
public class ProjectVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String title;
	private String description;
	private LocalDateTime lastModification;
	private LocalDateTime startDate;
	private LocalDateTime finishDate;
	private Integer stats;
	private UserVO userVO;

	public ProjectVO() {

	}

	public ProjectVO(int idProject, String title, String description, LocalDateTime lastModification,
			LocalDateTime startDate, LocalDateTime finishDate, int stats, UserVO userVO) {
		this.id = idProject;
		this.title = title;
		this.description = description;
		this.lastModification = lastModification;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.stats = stats;
		this.userVO = userVO;
	}

	@Id
	@Column(name = "id_project")
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

	@Column(name = "last_modification", nullable = false)
	public LocalDateTime getLastModification() {
		return lastModification;
	}

	public void setLastModification(LocalDateTime lastModification) {
		this.lastModification = lastModification;
	}

	@Column(name = "start_date", nullable = false)
	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	@Column(name = "finish_date", nullable = false)
	public LocalDateTime getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(LocalDateTime finishDate) {
		this.finishDate = finishDate;
	}

	@Column(name = "stats", nullable = false)
	public Integer getStats() {
		return stats;
	}

	public void setStats(Integer stats) {
		this.stats = stats;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_user", referencedColumnName = "id_user")
	public UserVO getUserVO() {
		return userVO;
	}

	public void setUserVO(UserVO userVO) {
		this.userVO = userVO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((finishDate == null) ? 0 : finishDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastModification == null) ? 0 : lastModification.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
		ProjectVO other = (ProjectVO) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (finishDate == null) {
			if (other.finishDate != null)
				return false;
		} else if (!finishDate.equals(other.finishDate))
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
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (stats == null) {
			if (other.stats != null)
				return false;
		} else if (!stats.equals(other.stats))
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
