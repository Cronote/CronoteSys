package com.cronoteSys.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;

import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

public class TeamController implements Initializable {

	@FXML private JFXButton btnPrevious;
	@FXML private JFXListView<TeamVO> lstTeams;
	@FXML private JFXButton btnNext;
	
	@FXML private JFXTextField txtName;
	@FXML private JFXTextArea txtDesc;
	
	@FXML private JFXTextField txtSearchUsers;
	@FXML private JFXButton btnSearchUsers;
	@FXML private JFXListView<UserVO> lstUsers;
	
	
	@FXML private JFXButton btnUnselect;
	@FXML private JFXButton btnSelect;
	
	@FXML private JFXTextField txtSearchMembers;
	@FXML private JFXButton btnSearchMembers;
	@FXML private JFXListView<UserVO> lstMembers;
	
	@FXML private JFXButton btnSave;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}