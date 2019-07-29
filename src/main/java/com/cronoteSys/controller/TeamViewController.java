package com.cronoteSys.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.cronoteSys.controller.components.cellfactory.SimplifiedAccountCellFactory;
import com.cronoteSys.model.bo.UserBO;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.model.vo.view.SimpleUser;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.SessionUtil;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class TeamViewController implements Initializable {

	@FXML
	private AnchorPane rootPaneViewTeam;
	@FXML
	private StackPane stkColor;
	@FXML
	private Label lblName;
	@FXML
	private Text lblDescription;
	@FXML
	private JFXListView<SimpleUser> membersLst;
	@FXML
	private JFXButton btnEdit;
	@FXML
	private JFXButton btnNew;
	@FXML
	private JFXButton btnLeaveTeam;

	@Inject
	private UserBO userBO;
	private UserVO loggedUser;
	private TeamVO viewingTeam;

	public void setViewingTeam(TeamVO viewingTeam) {
		this.viewingTeam = viewingTeam;
		if (viewingTeam != null) {
			lblName.setText(viewingTeam.getName());
			lblDescription.setText(viewingTeam.getDesc());
			String rgba = viewingTeam.getTeamColor() != null ? ScreenUtil.colorToRGBString(viewingTeam.getTeamColor())
					: "1,1,1,1";
			stkColor.setStyle(stkColor.getStyle().concat("-fx-background-color:rgba(" + rgba + ");"));
			btnEdit.setVisible(loggedUser.getIdUser() == viewingTeam.getOwner().getIdUser());
			btnLeaveTeam.setVisible(true);
		}
		switchVisibility(viewingTeam != null);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");
		membersLst.setCellFactory(new SimplifiedAccountCellFactory());

		initEvents();
	}

	private void initEvents() {
		btnEdit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				btnEditClicked(event);
			}
		});
		btnNew.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				btnNewClicked(event);
			}
		});
		btnLeaveTeam.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				btnLeaveTeamClicked(event);
			}
		});
	}

	private void btnEditClicked(ActionEvent event) {
		notifyAllBtnNewTeamListeners(viewingTeam, "edit");
	}

	private void btnNewClicked(ActionEvent event) {
		notifyAllBtnNewTeamListeners(null, "new");
	}

	private void btnLeaveTeamClicked(ActionEvent event) {
//		List<TeamUser> lst = new ArrayList<TeamUser>();
//		lst.addAll(viewingTeam.getTeamUser());
//		if (viewingTeam.getMembers().contains(loggedUser)) {
//			viewingTeam.getMembers().remove(loggedUser);
//			new TeamBO().update(viewingTeam, "leaving");
//		}
//		if (loggedUser.getIdUser().equals(viewingTeam.getOwner().getIdUser())) {
//			if (viewingTeam.getMembers().size() > 0) {
//				UserVO newOnwer = viewingTeam.getMembers().remove(0);
//				viewingTeam.setOwner(newOnwer);
//				new TeamBO().update(viewingTeam, "leaving");
//			} else {
//				new TeamBO().delete(viewingTeam);
//			}
//		}
	}

	private void switchVisibility(Boolean b) {
		lblName.setVisible(b);
		lblDescription.getParent().setVisible(b);
		membersLst.getParent().setVisible(b);
		stkColor.setVisible(b);

	}

	private static ArrayList<BtnNewTeamClickedI> btnNewTeamListeners = new ArrayList<BtnNewTeamClickedI>();

	public interface BtnNewTeamClickedI {
		void onBtnNewTeamClicked(TeamVO team, String mode);
	}

	public static void addOnBtnNewTeamClickedListener(BtnNewTeamClickedI newListener) {
		btnNewTeamListeners.add(newListener);
	}

	private void notifyAllBtnNewTeamListeners(TeamVO team, String mode) {
		for (BtnNewTeamClickedI l : btnNewTeamListeners) {
			l.onBtnNewTeamClicked(team, mode);
		}
	}
}