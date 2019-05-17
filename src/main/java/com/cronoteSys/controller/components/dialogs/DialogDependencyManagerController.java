package com.cronoteSys.controller.components.dialogs;

import java.net.URL;
import java.util.ResourceBundle;

import com.cronoteSys.controller.components.cellfactory.ActivityCellFactory;
import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.SessionUtil;
import com.google.inject.Inject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class DialogDependencyManagerController implements Initializable {

	@Inject
	private ActivityDAO actDAO;
	private UserVO loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");
	private ActivityVO selectedActivity;
	@FXML
	BorderPane dialogroot;
	@FXML
	Button btnCancel;
	@FXML
	Button btnConfirm;
	@FXML
	Label lblActivityTitle;
	@FXML
	ListView<ActivityVO> activityLst;
	@FXML
	ListView<ActivityVO> selectedActivitiesLst;
	@FXML
	Button btnUnselect;
	@FXML
	Button btnSelect;

	public ListView<ActivityVO> getSelectedActivitiesLst() {
		return selectedActivitiesLst;
	}

	public void setSelectedActivity(ActivityVO selectedActivity) {
		this.selectedActivity = selectedActivity;
		activityLst.setItems(FXCollections
				.observableArrayList(actDAO.getList(selectedActivity.getUserVO(), selectedActivity.getProjectVO())));
		activityLst.getItems().remove(selectedActivity);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		activityLst.setCellFactory(new ActivityCellFactory());
		btnConfirm.setDisable(true);
		initEvents();

	}

	private void initEvents() {
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
			}
		});
	}

}
