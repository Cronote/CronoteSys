package com.cronoteSys.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.cronoteSys.controller.components.cellfactory.TeamCellFactory;
import com.cronoteSys.model.bo.TeamBO;
import com.cronoteSys.model.bo.TeamBO.OnTeamAddedI;
import com.cronoteSys.model.bo.TeamBO.OnTeamDeletedI;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
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
	private ListProperty<TeamVO> teamLst = new SimpleListProperty<TeamVO>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");
		lstTeams.setCellFactory(new TeamCellFactory());
		initEvents();
		initObservers();
		fillList();
	}

	private void fillList() {
		List<TeamVO> lst = teambo.listByUserOwnerOrMember(loggedUser.getIdUser());
		teamLst.set(FXCollections.observableList(lst));
		lstTeams.itemsProperty().bind(teamLst);
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
				notifyAllTeamSelectedListeners(lstTeams.getSelectionModel().getSelectedItem());
			}
		});
	}

	private void initObservers() {
		TeamBO.addOnTeamAddedIListener(new OnTeamAddedI() {
			@Override
			public void onTeamAddedI(TeamVO team, String action) {
				if (action.equalsIgnoreCase("added")) {
					teamLst.add(0, team);
					lstTeams.refresh();
					lstTeams.getSelectionModel().clearAndSelect(0);
				} else if (action.equalsIgnoreCase("updated")) {
					int selected = lstTeams.getSelectionModel().getSelectedIndex();
					if (selected > -1) {
						lstTeams.getSelectionModel().clearSelection();
						teamLst.remove(selected);
						teamLst.add(selected, team);
						lstTeams.refresh();
						lstTeams.getSelectionModel().select(selected);
					}
				}
			}
		});
		TeamBO.addOnTeamDeletedListener(new OnTeamDeletedI() {
			@Override
			public void onTeamDeleted(TeamVO team) {
				lstTeams.getSelectionModel().clearSelection();
				teamLst.remove(team);
				lstTeams.refresh();
			}
		});
	}

	private static ArrayList<TeamSelectedI> teamSelectedListeners = new ArrayList<TeamSelectedI>();

	public interface TeamSelectedI {
		void onTeamSelected(TeamVO team);
	}

	public static void addOnBtnNewTeamClickedListener(TeamSelectedI newListener) {
		teamSelectedListeners.add(newListener);
	}

	private void notifyAllTeamSelectedListeners(TeamVO team) {
		for (TeamSelectedI l : teamSelectedListeners) {
			l.onTeamSelected(team);
		}
	}

}