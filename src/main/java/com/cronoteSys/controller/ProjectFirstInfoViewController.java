package com.cronoteSys.controller;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.binding.StringFormatter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXCheckBox;

public class ProjectFirstInfoViewController implements Initializable {
	@FXML
	private AnchorPane firstInfoRoot;

	private ProjectManagerController projectManagerController;

	@FXML
	private Label lblTitle;

	@FXML
	private Label lblDescription;

	@FXML
	private Label lblStartDate;

	@FXML
	private Label lblEndDate;

	@FXML
	private Label lblTotalTime;

	@FXML
	private Label lblPassedTime;

	@FXML
	private Label lblRealDoneTodo;

	@FXML
	private JFXProgressBar pgbRealProgress;

	@FXML
	private Label lblReaProgress;

	@FXML
	private Label lblEstimatedDoneTodo;

	@FXML
	private JFXProgressBar pgbEstimatedProgress;

	@FXML
	private Label lblEstimatedProgress;
	
	@FXML
	private HBox pnlInfoTeam;

	@FXML
	private Label lblTeamName;
	
	@FXML
	private StackPane stkTeamColor;
	
	@FXML
	private JFXCheckBox chxHoliday;

	@FXML
	private JFXCheckBox chxSaturday;

	@FXML
	private JFXCheckBox chxSunday;

	@FXML
	private JFXButton btnDelete;

	@FXML
	private JFXButton btnEdit;
	
	@FXML
	private JFXButton btnAddTeam;

	private ProjectVO viewingProject;

	private List<ActivityVO> lstAct = new ArrayList<ActivityVO>();

	public void setActivities(List<ActivityVO> lst) {
		lstAct.clear();
		lstAct.addAll(lst);
		getRealDoneTodo();
	}

	private void getRealDoneTodo() {
		double total = lstAct.size();
		Duration passedDuration = Duration.between(viewingProject.getStartDate(), LocalDateTime.now());
		int estimatedCount = 0;
		int doneCount = 0;
		for (ActivityVO act : lstAct) {
			if (StatusEnum.itsFinalized(act.getStats()))
				doneCount++;
		}
		Object[] activities = new ActivityBO().timeToComplete(lstAct, passedDuration);
		estimatedCount = (int) activities[1];

		this.lblRealDoneTodo.setText(String.format("%d/%.0f", doneCount, total));
		Double realProgress = doneCount / total;
		pgbRealProgress.setProgress(realProgress);

		this.lblEstimatedDoneTodo.setText(String.format("%d/%.0f", estimatedCount, total));
		Double estimatedProgress = estimatedCount / total;
		pgbEstimatedProgress.setProgress(estimatedProgress);
	}

	public ProjectFirstInfoViewController() {
		// TODO Auto-generated constructor stub
	}

	public ProjectFirstInfoViewController(ProjectManagerController control) {
		projectManagerController = control;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initEvents();

	}

	private void initEvents() {
		pgbRealProgress.progressProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				lblReaProgress.setText(String.format("%.2f%%", newValue.doubleValue() * 100));

			}
		});

		pgbEstimatedProgress.progressProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				lblEstimatedProgress.setText(String.format("%.2f%%", newValue.doubleValue() * 100));

			}
		});
		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				projectManagerController.setSelectedProject(null, "cadastro");
				projectManagerController.delete(viewingProject);
			}
		});
		btnEdit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				projectManagerController.setSelectedProject(viewingProject, "cadastro");

			}
		});
	}

	protected void setProject(ProjectVO project) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		if (project != null) {
			viewingProject = project;
			lblTitle.setText(viewingProject.getTitle());
			lblDescription.setText(viewingProject.getDescription());
			lblStartDate.setText(format.format(viewingProject.getStartDate()));
			lblEndDate.setText(format.format(viewingProject.getFinishDate()));
			LocalDateTime tempDateTime = viewingProject.getStartDate();

			long years = tempDateTime.until(LocalDateTime.now(), ChronoUnit.YEARS);
			tempDateTime = tempDateTime.plusYears(years);
			long months = tempDateTime.until(LocalDateTime.now(), ChronoUnit.MONTHS);
			tempDateTime = tempDateTime.plusMonths(months);
			long days = tempDateTime.until(LocalDateTime.now(), ChronoUnit.DAYS);

			lblPassedTime.setText(String.format("%d Ano(s), %d mes(es) e %d dia(s)", years, months, days));

			tempDateTime = viewingProject.getStartDate();
			years = tempDateTime.until(viewingProject.getFinishDate(), ChronoUnit.YEARS);
			tempDateTime = tempDateTime.plusYears(years);
			months = tempDateTime.until(viewingProject.getFinishDate(), ChronoUnit.MONTHS);
			tempDateTime = tempDateTime.plusMonths(months);
			days = tempDateTime.until(viewingProject.getFinishDate(), ChronoUnit.DAYS);

			lblTotalTime.setText(String.format("%d Ano(s), %d mes(es) e %d dia(s)", years, months, days));

			if(project.getTeam()!=null) {
				pnlInfoTeam.setVisible(true);
				lblTeamName.setText(project.getTeam().getName());
				String color = project.getTeam() != null ? ScreenUtil.colorToRGBString(project.getTeam().getTeamColor()) : "1,1,1,1";
				stkTeamColor.setStyle("-fx-background-color:rgba(" + color + ");");
				
			}
			else {
				pnlInfoTeam.setVisible(false);
				
			}
		}else {
			pnlInfoTeam.setVisible(false);
		}
	}
}
