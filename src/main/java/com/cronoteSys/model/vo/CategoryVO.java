package com.cronoteSys.model.vo;

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
@Table(name = "tb_category")
public class CategoryVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer _id_Category;
	private String _description;
	private UserVO _userVO;
	
	public CategoryVO() {
		
	}
	
	public CategoryVO(int idCategory, String description, UserVO userVO) {
		this._id_Category = idCategory;
		this._description = description;
		this._userVO = userVO;
	}
	
	@Id
	@Column(name = "id_category", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer get_id_Category() {
		return _id_Category;
	}
	
	public void set_id_Category(Integer _id_Category) {
		this._id_Category = _id_Category;
	}
	
	@Column(name = "description", nullable = false)
	public String get_description() {
		return _description;
	}
	
	
	public void set_description(String _description) {
		this._description = _description;
	}
	
	
	@ManyToOne(targetEntity = UserVO.class)
	@JoinColumn(name = "id_user")
	@Fetch(FetchMode.SELECT)
	public UserVO get_userVO() {
		return _userVO;
	}
	
	public void set_userVO(UserVO _userVO) {
		this._userVO = _userVO;
	}
	

}
