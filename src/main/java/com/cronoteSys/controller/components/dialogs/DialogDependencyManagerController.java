package com.cronoteSys.controller.components.dialogs;

import java.net.URL;
import java.util.ResourceBundle;

import com.cronoteSys.controller.components.cellfactory.SimpleActivityCellFactory;
import com.cronoteSys.filter.ActivityFilter;
import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.SimpleActivity;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.SessionUtil;
import com.google.inject.Inject;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
	ListView<SimpleActivity> activityLst;
	@FXML
	ListView<SimpleActivity> selectedActivitiesLst;
	@FXML
	Button btnUnselect;
	@FXML
	Button btnSelect;
	ActivityFilter filter = new ActivityFilter();


	public void setSelectedActivity(ActivityVO selectedActivity) {
		this.selectedActivity = selectedActivity;
		filter.setProject(selectedActivity.getProjectVO().getId());
		activityLst.setItems(FXCollections
				.observableArrayList(actDAO.getSimpleActivitiesView(filter)));
		activityLst.getItems().remove(SimpleActivity.fromActivity(selectedActivity));
		activityLst.getItems().removeAll(SimpleActivity.fromList(selectedActivity.getDependencies()));
		selectedActivitiesLst.setItems(FXCollections.observableArrayList(SimpleActivity.fromList(selectedActivity.getDependencies())));
		lblActivityTitle.setText(selectedActivity.getTitle());
		
		selectedActivitiesLst.getItems().addListener(new ListChangeListener<SimpleActivity>() {
			@Override
			public void onChanged(Change<? extends SimpleActivity> c) {
				btnUnselect.setDisable(c.getList().isEmpty());
				btnConfirm.setDisable(c.getList().isEmpty());
				
			}

		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		activityLst.setCellFactory(new SimpleActivityCellFactory());
		selectedActivitiesLst.setCellFactory(new SimpleActivityCellFactory());
		btnConfirm.setDisable(true);
		btnConfirm.setDisable(true);
		initEvents();

		

	}

	private void initEvents() {
		btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				selectedActivity.setDependenciesFromSimple(selectedActivitiesLst.getItems());
				((Stage) btnCancel.getScene().getWindow()).close();
			}
		});
		btnCancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Stage) btnCancel.getScene().getWindow()).close();
			}
		});
		btnSelect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				SimpleActivity selected = activityLst.getSelectionModel().getSelectedItem();
				if (selected != null) {
					activityLst.getItems().remove(selected);
					activityLst.getSelectionModel().clearSelection();
					selectedActivitiesLst.getItems().add(selected);
				}
			}
		});
		btnUnselect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				SimpleActivity unselecting = selectedActivitiesLst.getSelectionModel().getSelectedItem();
				if (unselecting != null) {
					activityLst.getItems().add(unselecting);
					selectedActivitiesLst.getItems().remove(unselecting);
					selectedActivitiesLst.getSelectionModel().clearSelection();
					
				}
			}
		});
	}

}
