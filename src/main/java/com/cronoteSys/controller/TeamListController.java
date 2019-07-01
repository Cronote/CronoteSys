package com.cronoteSys.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.cronoteSys.model.bo.TeamBO;
import com.cronoteSys.model.dao.TeamDAO;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.sun.xml.internal.bind.v2.runtime.output.ForkXmlOutput;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class TeamListController implements Initializable {

	@FXML
	private JFXButton btnPrevious;
	@FXML
	private JFXListView<TeamVO> lstTeams;
	@FXML
	private JFXButton btnNext;

	private UserVO loggedUser;
	private TeamBO teambo = new TeamBO();
	private List<TeamVO> teamLst = new ArrayList<TeamVO>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");

		initEvents();
		fillList();
	}

	private void fillList() {
		teamLst = teambo.listByUserOwnerOrMember(loggedUser.getIdUser());
		lstTeams.setItems(FXCollections.observableList(teamLst));
	}

	private void initEvents() {
		btnNext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int selected = lstTeams.getSelectionModel().getSelectedIndex() + 1;
				if (selected < lstTeams.getItems().size()) {
					lstTeams.getSelectionModel().select(selected);
				}
			}
		});
		btnPrevious.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int selected = lstTeams.getSelectionModel().getSelectedIndex() - 1;
				if (selected > -1) {
					lstTeams.getSelectionModel().select(selected);
				}
			}
		});
		lstTeams.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				lstTeams.scrollTo(newValue.intValue());
			}
		});
	}

}