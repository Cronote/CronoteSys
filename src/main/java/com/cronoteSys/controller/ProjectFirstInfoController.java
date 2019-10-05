package com.cronoteSys.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.cronoteSys.controller.components.dialogs.DialogCategoryManagerController;
import com.cronoteSys.controller.components.dialogs.DialogDependencyManager;
import com.cronoteSys.controller.components.dialogs.DialogTeamCategoryController;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

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
	@FXML
	private JFXButton btnAddTeam;
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

		Node[] field = { txtTitle, dtpByDateStart, dtpByDateEnd };
		Boolean[] isNotnull = { true, true, true };
		Boolean[] isEmail = { false, false, false };
		ScreenUtil.addInlineValidation(field, isNotnull, isEmail);
	}

	private void initEvents() {
		btnSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				txtTitle.validate();
				if (tabDeadline.getSelectionModel().getSelectedIndex() == 0) {
					if (!dtpByDateStart.validate() || !dtpByDateEnd.validate())
						return;
					if (!dtpByDateEnd.getValue().isAfter(dtpByDateStart.getValue()))
						return;
				} else {

				}
				getProject();
			}
		});
		
		btnAddTeam.setOnAction(e->{
			DialogTeamCategoryController categoryManagerDialog = new DialogTeamCategoryController();

			categoryManagerDialog.showDependencyManagerDialog();
		});

		tabDeadline.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Node[] fieldsToRemove = { dtpByDateStart, dtpByDateEnd, dtpByTimeStart };
				ScreenUtil.removeInlineValidation(fieldsToRemove);
				if (newValue.intValue() == 0) {
					Node[] field = { dtpByDateStart, dtpByDateEnd };
					Boolean[] isNotnull = { true, true };
					Boolean[] isEmail = { false, false };
					ScreenUtil.addInlineValidation(field, isNotnull, isEmail);
				} else if (newValue.intValue() == 1) {
					// TODO: Adicionar validações para a aba 'por tempo'
				}

			}
		});

		dtpByDateEnd.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				Pane parentPane = (Pane) dtpByDateEnd.getParent();
				StackPane inputLine = (StackPane) dtpByDateEnd.lookup(".jfx-text-field>*.input-line");
				Label errorlabel;
				if ((Label) parentPane.lookup("#endDateErrorLabel") != null)
					errorlabel = (Label) parentPane.lookup("#endDateErrorLabel");
				else
					errorlabel = new Label("A DATA DEVE SER DEPOIS DA DE INICIO!");
				parentPane.getChildren().removeAll(errorlabel);
				if (inputLine != null)
					inputLine.getStyleClass().removeAll("stack-error");
				if (!dtpByDateEnd.validate() || newValue == null)
					return;
				if (!newValue.isAfter(dtpByDateStart.getValue())) {
					errorlabel.setId("endDateErrorLabel");
					errorlabel.setStyle("-fx-text-fill:#FF5757;");
					errorlabel.setWrapText(true);
					errorlabel.setPrefWidth(205);
					errorlabel.setLayoutY(dtpByDateEnd.getLayoutY() + 35);
					parentPane.getChildren().add(errorlabel);
					AnchorPane.setRightAnchor(errorlabel, 24.0);
					if (inputLine != null) {
						inputLine.getStyleClass().add("stack-error");
					}
					return;
				}
			}
		});

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
