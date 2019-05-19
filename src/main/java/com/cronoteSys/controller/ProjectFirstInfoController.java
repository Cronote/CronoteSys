package com.cronoteSys.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class ProjectFirstInfoController implements Initializable {
	@FXML
	private AnchorPane firstInfoRoot;
	@FXML
	private JFXTextField txtTitle;
	@FXML
	private JFXTextArea txtDescription;
	@FXML
	private TabPane tabDeadline;
	@FXML
	private JFXDatePicker dtpByDateStart;
	@FXML
	private JFXDatePicker dtpByDateEnd;
	@FXML
	private JFXDatePicker dtpByTimeStart;
	@FXML
	private Spinner<Integer> spnTotalTimeHour;
	@FXML
	private Spinner<Integer> spnTotalTimeMinute;
	@FXML
	private Spinner<Integer> spnHourADay;
	@FXML
	private Spinner<Integer> spnMinuteADay;
	@FXML
	private CheckBox chbConsiderHolidays;
	@FXML
	private JFXButton btnSave;

	ProjectManagerController projectManagerController;

	public ProjectFirstInfoController(ProjectManagerController control) {
		projectManagerController = control;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initDefaults();
		initEvents();

	}

	private void initDefaults() {
		spnTotalTimeHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 0, 1));
		spnTotalTimeMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 1, 1));
		spnHourADay.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 0, 1));
		spnMinuteADay.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 1, 1));
		dtpByDateStart.setValue(LocalDate.now());
		dtpByTimeStart.setValue(LocalDate.now());
	}

	private void initEvents() {
		btnSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				getProject();
			}
		});
	}

	protected boolean fielsFilleds() {
		if (txtTitle.getText().trim().isEmpty()) {
			txtTitle.requestFocus();
			return false;
		}
		if (tabDeadline.getSelectionModel().getSelectedIndex() == 0) {
			if (dtpByDateStart.getValue() == null) {
				dtpByDateStart.requestFocus();
				return false;
			}
			boolean dateEndOk = true;
			if (dtpByDateEnd.getValue() == null) {
				dateEndOk = false;
			}
			if (dateEndOk && dtpByDateEnd.getValue().isBefore(dtpByDateStart.getValue())) {
				dateEndOk = false;
			}
			if (!dateEndOk) {
				dtpByDateEnd.requestFocus();
				return false;
			}
		} else if (tabDeadline.getSelectionModel().getSelectedIndex() == 1) {
			// TODO: fazer o metodo de coleta de prazo por tempo
		}
		return true;
	}

	protected ProjectVO getProject() {
		ProjectVO proj = projectManagerController.getSelectedProject();
		proj.setTitle(txtTitle.getText());
		proj.setDescription(txtDescription.getText());
		if (tabDeadline.getSelectionModel().getSelectedIndex() == 0) {
			proj.setStartDate(LocalDateTime.of(dtpByDateStart.getValue(), LocalTime.MIN));
			proj.setFinishDate(LocalDateTime.of(dtpByDateEnd.getValue(), LocalTime.MAX));
		}
		proj.setUserVO((UserVO) SessionUtil.getSession().get("loggedUser"));
		projectManagerController.saveProject(proj);
		return proj;
	}

	protected void setProject(ProjectVO project) {

		if (project != null) {
			txtTitle.setText(project.getTitle());
			txtDescription.setText(project.getDescription());
			dtpByDateStart.setValue(project.getStartDate().toLocalDate());
			dtpByDateEnd.setValue(project.getFinishDate().toLocalDate());
		}
	}
}
