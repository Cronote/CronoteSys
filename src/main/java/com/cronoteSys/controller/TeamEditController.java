package com.cronoteSys.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.cronoteSys.controller.components.cellfactory.SimplifiedAccountCellFactory;
import com.cronoteSys.controller.components.cellfactory.TeamMemberCellFactory;
import com.cronoteSys.model.bo.TeamBO;
import com.cronoteSys.model.bo.UserBO;
import com.cronoteSys.model.interfaces.ThreatingUser;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.model.vo.relation.side.TeamMember;
import com.cronoteSys.model.vo.view.SimpleUser;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.SessionUtil;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.scene.layout.AnchorPane;

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
	private ListView<ThreatingUser> lstUsers;

	@FXML
	private JFXButton btnUnselect;
	@FXML
	private JFXButton btnSelect;

	@FXML
	private JFXTextField txtSearchMembers;
	@FXML
	private JFXButton btnSearchMembers;
	@FXML
	private ListView<ThreatingUser> lstMembers;
	@FXML
	private JFXColorPicker cpTeamColor;
	@FXML
	private JFXButton btnSave;
	@Inject
	private UserBO userBO;

	private ListProperty<ThreatingUser> lstMProperty = new SimpleListProperty<ThreatingUser>();
	private ListProperty<ThreatingUser> lstUProperty = new SimpleListProperty<ThreatingUser>();
	private UserVO loggedUser;
	private TeamVO editingTeam;
	@FXML
	private AnchorPane rootPaneEditTeam;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");
		txtName.getValidators().add(new RequiredFieldValidator("Campo obrigatório!"));
		lstUsers.setCellFactory(new TeamMemberCellFactory());
		lstMembers.setCellFactory(new TeamMemberCellFactory());
		initEvents();

		lstUsers.itemsProperty().bind(lstUProperty);
		lstMembers.itemsProperty().bind(lstMProperty);
	}

	public void setEditingTeam(TeamVO editingTeam) {
		this.editingTeam = editingTeam;
		if (editingTeam != null) {
			txtName.setText(editingTeam.getName());
			txtDesc.setText(editingTeam.getDesc());
			cpTeamColor.setValue(ScreenUtil.stringToColor(editingTeam.getTeamColor()));
			lstMProperty.set(FXCollections.observableArrayList(editingTeam.getMembers()));
			refreshLists();
		}

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
		if (editingTeam == null)
			editingTeam = new TeamVO();
		String membersString = loggedUser.getIdUser().toString();
		if (lstMembers.getItems() != null) {
			for (int i = 0; i < lstMembers.getItems().size(); i++) {
				TeamMember tm = (TeamMember) lstMembers.getItems().get(i);
				membersString += "," + tm.getUser().getIdUser();
			}
		}
		List<UserVO> users = userBO.findByNameOrEmail(search, membersString);
		lstUProperty.set(FXCollections.observableArrayList(users));
		refreshLists();
	}

	private void refreshLists() {
		lstUsers.refresh();
		lstMembers.refresh();
	}

	private void btnSearchUsersClicked(ActionEvent e) {
		if (!txtSearchUsers.getText().trim().isEmpty()) {
			usersFill(txtSearchUsers.getText().trim());
		}
	}

	private void btnSaveClicked(ActionEvent e) {
		if (editingTeam == null)
			editingTeam = new TeamVO();
		if (!txtName.validate())
			return;
		editingTeam.setName(txtName.getText());
		editingTeam.setName(txtName.getText());
		editingTeam.setDesc(txtDesc.getText());
		editingTeam.setTeamColor(cpTeamColor.getValue().toString());
		List<TeamMember> members = new ArrayList<TeamMember>();
		if (lstMembers.getItems() != null) {
			for (ThreatingUser tu : lstMembers.getItems()) {
				members.add((TeamMember) tu);
			}
		}
		editingTeam.setMembers(members);

		TeamBO teambo = new TeamBO();
		if (editingTeam.getId() == null) {
			editingTeam.setOwner(loggedUser);
			teambo.save(editingTeam);
		} else
			teambo.update(editingTeam);
	}

	private void btnSelectClicked(ActionEvent e) {
		UserVO selected = (UserVO) lstUsers.getSelectionModel().getSelectedItem();
		List<TeamMember> lstTmp = new ArrayList<TeamMember>();
		for (ThreatingUser aux : lstMProperty) {
			lstTmp.add((TeamMember) aux);
		}
		if (selected != null) {
			lstUProperty.remove(selected);
			lstTmp.add(new TeamMember(selected, false));
			lstMProperty.set(FXCollections.observableArrayList(lstTmp));
			refreshLists();
		}
	}

	private void btnUnselectClicked(ActionEvent e) {
		TeamMember selected = (TeamMember) lstMembers.getSelectionModel().getSelectedItem();
		List<UserVO> lstTmp = new ArrayList<UserVO>();
		for (ThreatingUser aux : lstUProperty) {
			lstTmp.add((UserVO) aux);
		}
		if (selected != null) {
			lstMProperty.remove(selected);
			lstTmp.add(selected.getUser());
			lstUProperty.set(FXCollections.observableArrayList(lstTmp));
			refreshLists();

		}
	}
}