package com.cronoteSys.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.cronoteSys.controller.components.cellfactory.SimplifiedAccountCellFactory;
import com.cronoteSys.model.bo.TeamBO;
import com.cronoteSys.model.bo.UserBO;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.model.vo.view.SimpleUser;
import com.cronoteSys.util.SessionUtil;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class TeamEditController implements Initializable {

	@FXML
	private JFXTextField txtName;
	@FXML
	private JFXTextArea txtDesc;

	@FXML
	private JFXTextField txtSearchUsers;
	@FXML
	private JFXButton btnSearchUsers;
	@FXML
	private JFXListView<SimpleUser> lstUsers;

	@FXML
	private JFXButton btnUnselect;
	@FXML
	private JFXButton btnSelect;

	@FXML
	private JFXTextField txtSearchMembers;
	@FXML
	private JFXButton btnSearchMembers;
	@FXML
	private JFXListView<SimpleUser> lstMembers;

	@FXML
	private JFXButton btnSave;
	private UserVO loggedUser;
	@Inject
	private UserBO userBO;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");

		lstUsers.setCellFactory(new SimplifiedAccountCellFactory());
		lstMembers.setCellFactory(new SimplifiedAccountCellFactory());

		initEvents();
	}

	private void initEvents() {
		btnSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				btnSaveClicked(event);
			}
		});
		btnSearchUsers.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				btnSearchUsersClicked(event);
			}
		});

		btnSelect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				btnSelectClicked(event);
			}
		});
		btnUnselect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				btnUnselectClicked(event);
			}
		});
	}

	private void usersFill(String search) {
		List<SimpleUser> users = userBO.findByNameOrEmail(search, loggedUser.getIdUser());
		lstUsers.setItems(FXCollections.observableList(users));
	}

	private void btnSearchUsersClicked(ActionEvent e) {
		if (!txtSearchUsers.getText().trim().isEmpty()) {
			usersFill(txtSearchUsers.getText().trim());
		}
	}

	private void btnSaveClicked(ActionEvent e) {
		TeamVO team = new TeamVO();
		team.setOwner(loggedUser);
		if (!txtName.validate())
			return;
		team.setName(txtName.getText());
		team.setDesc(txtDesc.getText());
		List<UserVO> members = new ArrayList<UserVO>();
		if (lstMembers.getItems().size() > 0) {
			for (SimpleUser su : lstMembers.getItems()) {
				members.add(new UserVO(su));
			}
		}
		team.setMembers(members);
		TeamBO teambo = new TeamBO();
		teambo.save(team);
	}

	private void btnSelectClicked(ActionEvent e) {
		SimpleUser selected = lstUsers.getSelectionModel().getSelectedItem();
		if (selected != null) {
			lstUsers.getItems().remove(selected);
			lstMembers.getItems().add(selected);
		}
	}

	private void btnUnselectClicked(ActionEvent e) {
		SimpleUser selected = lstMembers.getSelectionModel().getSelectedItem();
		if (selected != null) {
			lstMembers.getItems().remove(selected);
			lstUsers.getItems().add(selected);
		}
	}
}