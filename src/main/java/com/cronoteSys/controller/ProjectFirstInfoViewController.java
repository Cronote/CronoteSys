package com.cronoteSys.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ResourceBundle;

import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.binding.StringFormatter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXCheckBox;

public class ProjectFirstInfoViewController implements Initializable {
	@FXML
	private AnchorPane firstInfoRoot;

	ProjectManagerController projectManagerController;

	@FXML
	Label lblTitle;

	@FXML
	Label lblDescription;

	@FXML
	Label lblStartDate;

	@FXML
	Label lblEndDate;

	@FXML
	Label lblTotalTime;

	@FXML
	Label lblPassedTime;

	@FXML
	Label lblRealDoneTodo;

	@FXML
	JFXProgressBar pgbRealProgress;

	@FXML
	Label lblEstimatedDoneTodo;

	@FXML
	JFXProgressBar pgbEstimatedProgress;

	@FXML
	JFXCheckBox chxHoliday;

	@FXML
	JFXCheckBox chxSaturday;

	@FXML
	JFXCheckBox chxSunday;

	@FXML
	JFXButton btnDelete;

	@FXML
	JFXButton btnEdit;

	private ProjectVO viewingProject;

	public void setRealDoneTodo(String realDoneTodo) {
		this.lblRealDoneTodo.setText(realDoneTodo);

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
			LocalDateTime tempDateTime =viewingProject.getStartDate();

			long years = tempDateTime.until( LocalDateTime.now(), ChronoUnit.YEARS);
			tempDateTime = tempDateTime.plusYears( years );
			long months = tempDateTime.until( LocalDateTime.now(), ChronoUnit.MONTHS);
			tempDateTime = tempDateTime.plusMonths(months);
			long days = tempDateTime.until( LocalDateTime.now(), ChronoUnit.DAYS);

			lblPassedTime.setText(String.format("%d Ano(s), %d mes(es) e %d dia(s)"
					,years,months,days));
			
			tempDateTime =viewingProject.getStartDate();
			 years = tempDateTime.until( viewingProject.getFinishDate(), ChronoUnit.YEARS);
			tempDateTime = tempDateTime.plusYears( years );
			 months = tempDateTime.until( viewingProject.getFinishDate(), ChronoUnit.MONTHS);
			tempDateTime = tempDateTime.plusMonths(months);
			 days = tempDateTime.until( viewingProject.getFinishDate(), ChronoUnit.DAYS);

			lblTotalTime.setText(String.format("%d Ano(s), %d mes(es) e %d dia(s)"
					,years,months,days));

//			System.out.println(viewingProject.getStartDate().until(LocalDateTime.now(), ChronoUnit.YEARS));
		}
	}
}
