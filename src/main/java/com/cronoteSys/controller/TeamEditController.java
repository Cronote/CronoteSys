package com.cronoteSys.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.cronoteSys.controller.components.cellfactory.TeamMemberCellFactory;
import com.cronoteSys.model.bo.TeamBO;
import com.cronoteSys.model.bo.UserBO;
import com.cronoteSys.model.interfaces.ThreatingUser;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.model.vo.relation.side.TeamMember;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.SessionUtil;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
	private JFXTextField txtSearchMembers;
	@FXML
	private JFXButton btnSearchMembers;
	@FXML
	private ListView<ThreatingUser> lstMembers;
	@FXML
	private JFXColorPicker cpTeamColor;
	@FXML
	private JFXButton btnSave;
	@FXML
	private AnchorPane rootPaneEditTeam;
	@Inject
	private UserBO userBO;

	private ListProperty<ThreatingUser> lstMProperty = new SimpleListProperty<ThreatingUser>();
	private ListProperty<ThreatingUser> lstUProperty = new SimpleListProperty<ThreatingUser>();
	private UserVO loggedUser;
	private TeamVO editingTeam;
	private static final DataFormat MEMBER_LIST = new DataFormat("memberList");

	private List<TeamMember> membersBackup = new ArrayList<TeamMember>();

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
			membersBackup.clear();
			membersBackup.addAll(editingTeam.getMembers());
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

		lstUsers.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				dragDetected(event, lstUsers);
			}
		});

		lstUsers.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				dragOver(event, lstUsers);
			}
		});

		lstUsers.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				dragDropped(event, lstUsers);
			}
		});

		// Add mouse event handlers for the target
		lstMembers.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				dragDetected(event, lstMembers);
			}
		});

		lstMembers.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				dragOver(event, lstMembers);
			}
		});

		lstMembers.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				dragDropped(event, lstMembers);
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
		editingTeam.setDesc(txtDesc.getText());
		editingTeam.setTeamColor(cpTeamColor.getValue().toString());
		if (lstMembers.getItems() != null) {
			for (ThreatingUser tu : lstMembers.getItems()) {
				if (!backupContains((TeamMember) tu)) {
					TeamMember member = (TeamMember) tu;
					member.setExpiresAt(LocalDateTime.now().plusMinutes(24));
					membersBackup.add(member);
				}
			}

			membersBackup.removeIf(new Predicate<TeamMember>() {

				@Override
				public boolean test(TeamMember t) {

					return !membersContains(t);
				}
			});

		}
		for (TeamMember teamMember : membersBackup) {
			if (!teamMember.isInviteAccepted() && teamMember.getExpiresAt() == null) {
				teamMember.setExpiresAt(LocalDateTime.now().plusMinutes(24));

			}
		}
		editingTeam.setMembers(membersBackup);

		TeamBO teambo = new TeamBO();
		if (editingTeam.getId() == null) {
			editingTeam.setOwner(loggedUser);
			teambo.save(editingTeam);
		} else
			teambo.update(editingTeam);
	}

	private boolean backupContains(TeamMember tm) {
		for (TeamMember m : membersBackup) {
			if (tm.getUser().getIdUser() == m.getUser().getIdUser()) {
				return true;
			}
		}

		return false;

	}

	private boolean membersContains(TeamMember tm) {
		for (ThreatingUser tu : lstMembers.getItems()) {
			if (tm.getUser().getIdUser() == ((TeamMember) tu).getUser().getIdUser()) {
				return true;
			}
		}

		return false;

	}

	private void manipulateLists(ThreatingUser selected, String type) {
		List<Object> lstTmp = new ArrayList<Object>();
		if (type.equals("user")) {
			lstTmp.clear();
			for (ThreatingUser aux : lstUProperty) {
				lstTmp.add((UserVO) aux);
			}
		} else {
			lstTmp.clear();
			for (ThreatingUser aux : lstMProperty) {
				lstTmp.add((TeamMember) aux);
			}
		}
		if (selected != null) {
			if (type.equals("user")) {
				lstMProperty.remove(selected);
				List<UserVO> usersTmp = new ArrayList<UserVO>();
				for (Object obj : lstTmp)
					usersTmp.add((UserVO) obj);
				usersTmp.add(((TeamMember) selected).getUser());
				lstUProperty.set(FXCollections.observableArrayList(usersTmp));
			} else {
				lstUProperty.remove(selected);
				List<TeamMember> membersTmp = new ArrayList<TeamMember>();
				for (Object obj : lstTmp)
					membersTmp.add((TeamMember) obj);
				membersTmp.add(new TeamMember((UserVO) selected, false, LocalDateTime.now()));
				lstMProperty.set(FXCollections.observableArrayList(membersTmp));
			}
			refreshLists();

		}
	}

	private void dragDetected(MouseEvent event, ListView<ThreatingUser> listView) {
		// Make sure at least one item is selected
		int selectedCount = listView.getSelectionModel().getSelectedIndices().size();

		if (selectedCount == 0) {
			event.consume();
			return;
		}

		// Initiate a drag-and-drop gesture
		Dragboard dragboard = listView.startDragAndDrop(TransferMode.MOVE);

		// Put the the selected items to the dragboard
		ThreatingUser selected = listView.getSelectionModel().getSelectedItem();

		ClipboardContent content = new ClipboardContent();
		content.put(MEMBER_LIST, selected);

		dragboard.setContent(content);
		event.consume();
	}

	private void dragOver(DragEvent event, ListView<ThreatingUser> listView) {
		// If drag board has an ITEM_LIST and it is not being dragged
		// over itself, we accept the MOVE transfer mode
		Dragboard dragboard = event.getDragboard();

		if (event.getGestureSource() != listView && dragboard.hasContent(MEMBER_LIST)) {
			event.acceptTransferModes(TransferMode.MOVE);
		}

		event.consume();
	}

	private void dragDropped(DragEvent event, ListView<ThreatingUser> listView) {
		boolean dragCompleted = false;

		// Transfer the data to the target
		Dragboard dragboard = event.getDragboard();

		if (dragboard.hasContent(MEMBER_LIST)) {
			ThreatingUser moving = (ThreatingUser) dragboard.getContent(MEMBER_LIST);
			String movingTo = moving instanceof UserVO ? "member" : "user";
			manipulateLists(moving, movingTo);
			// Data transfer is successful
			dragCompleted = true;
		}

		// Data transfer is not successful
		event.setDropCompleted(dragCompleted);
		event.consume();
	}

}