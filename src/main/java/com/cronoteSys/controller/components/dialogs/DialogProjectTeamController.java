package com.cronoteSys.controller.components.dialogs;

import java.net.URL;
import java.util.ResourceBundle;

import com.cronoteSys.controller.components.listcell.SimplifiedTeamCellController;
import com.cronoteSys.model.bo.TeamBO;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DialogProjectTeamController implements Initializable {

	@FXML
	private Button btnSearch;
	@FXML
	private JFXTextField txtSearch;
	@FXML
	private ListView<TeamVO> teamList;
	@FXML
	private Button btnConfirm;
	@FXML
	private Button btnCancel;

	private TeamBO teamBO = new TeamBO();
	private ObservableList<TeamVO> lstTeams = FXCollections.emptyObservableList();
	private UserVO loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lstTeams = FXCollections.observableList(teamBO.listByUserOwnerOrMember(loggedUser.getIdUser()));
		teamList.setItems((lstTeams));

		teamList.setCellFactory(new Callback<ListView<TeamVO>, ListCell<TeamVO>>() {

			@Override
			public ListCell<TeamVO> call(ListView<TeamVO> param) {
				return new SimplifiedTeamCellController();
			}
		});

		btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Stage) btnCancel.getScene().getWindow()).close();
			}
		});
		btnCancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Stage) btnCancel.getScene().getWindow()).close();
				teamList.getSelectionModel().clearSelection();
			}
		});
		
		btnSearch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String search = txtSearch.getText().trim();
				if (!search.isEmpty()) {
					lstTeams = FXCollections.observableList(teamBO.searchByUserOwnerOrMember(search, loggedUser));
				} else {
					lstTeams = FXCollections.observableList(teamBO.listByUserOwnerOrMember(loggedUser.getIdUser()));
				}
				teamList.setItems(lstTeams);
			}
		});

	}

	public TeamVO getSelectedTeam() {
		return teamList.getSelectionModel().getSelectedItem();
	}

}
