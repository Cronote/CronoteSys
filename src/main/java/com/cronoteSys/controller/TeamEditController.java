package com.cronoteSys.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.cronoteSys.controller.components.cellfactory.SimplifiedAccountCellFactory;
import com.cronoteSys.model.bo.TeamBO;
import com.cronoteSys.model.bo.UserBO;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;
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
import javafx.scene.paint.Color;

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
	private JFXColorPicker cpTeamColor;
	@FXML
	private JFXButton btnSave;
	@Inject
	private UserBO userBO;

	private UserVO loggedUser;
	private TeamVO editingTeam;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");
		txtName.getValidators().add(new RequiredFieldValidator("Campo obrigatório!"));
		lstMembers.setCellFactory(new SimplifiedAccountCellFactory());
		lstUsers.setCellFactory(new SimplifiedAccountCellFactory());
		initEvents();
	}

	public void setEditingTeam(TeamVO editingTeam) {
		this.editingTeam = editingTeam;
		if (editingTeam != null) {
			txtName.setText(editingTeam.getName());
			txtDesc.setText(editingTeam.getDesc());
			cpTeamColor.setValue(ScreenUtil.stringToColor(editingTeam.getTeamColor()));
			editingTeam.getMembersSimpleUser().removeIf(new Predicate<SimpleUser>() {
				@Override
				public boolean test(SimpleUser t) {
					return t.getIdUser().equals(editingTeam.getOwner().getIdUser());
				}
			});
			lstMembers.setItems(FXCollections.observableArrayList(editingTeam.getMembersSimpleUser()));
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
		String members = loggedUser.getIdUser().toString();
		for (SimpleUser su : lstMembers.getItems()) {
			members += ("," + su.getIdUser().toString());
		}
		List<SimpleUser> users = userBO.findByNameOrEmail(search, members);
		lstUsers.setItems(FXCollections.observableList(users));
	}

	private void btnSearchUsersClicked(ActionEvent e) {
		if (!txtSearchUsers.getText().trim().isEmpty()) {
			usersFill(txtSearchUsers.getText().trim());
		}
	}

	private void btnSaveClicked(ActionEvent e) {
		if (editingTeam == null) {
			editingTeam = new TeamVO();
			editingTeam.setOwner(loggedUser);
		}
		if (!txtName.validate())
			return;
		editingTeam.setName(txtName.getText());
		editingTeam.setName(txtName.getText());
		editingTeam.setDesc(txtDesc.getText());
		editingTeam.setTeamColor(cpTeamColor.getValue().toString());
		lstMembers.getItems().removeIf(new Predicate<SimpleUser>() {

			@Override
			public boolean test(SimpleUser t) {
				boolean b = t.getIdUser().longValue() == editingTeam.getOwner().getIdUser().longValue();
				return b;
			}
		});
		List<UserVO> members = new ArrayList<UserVO>();
		if (lstMembers.getItems().size() > 0) {
			for (SimpleUser su : lstMembers.getItems()) {
				UserVO u = new UserVO(su);
				u.setStats(Byte.valueOf("1"));
				members.add(u);
			}
		}
		editingTeam.setMembers(members);
		TeamBO teambo = new TeamBO();
		if (editingTeam.getId() == null)
			teambo.save(editingTeam);
		else
			teambo.update(editingTeam);
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