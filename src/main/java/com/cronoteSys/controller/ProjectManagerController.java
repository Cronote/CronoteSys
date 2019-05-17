package com.cronoteSys.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.ActivityBO.OnActivityDeletedI;
import com.cronoteSys.model.bo.ProjectBO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.observer.ShowEditViewActivityObservableI;
import com.cronoteSys.observer.ShowEditViewActivityObserverI;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class ProjectManagerController implements Initializable {

	@FXML
	private TabPane tabPane;
	HBox hboxContent = new HBox();
	ProjectManagerState state;
	ProjectFirstInfoController firstInfoController = new ProjectFirstInfoController(this);
	ActivityListController activityListController;
	ProjectBO projectBO = new ProjectBO();
	ProjectVO selectedProject;
	Tab tbActivities = new Tab("ATIVIDADES", hboxContent);

	public ProjectVO getSelectedProject() {
		return selectedProject != null ? selectedProject : new ProjectVO();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initFirstInfo();
		initActivities();
	}

	private void initEvents() {
	}

	private void initFirstInfo() {
		try {
			FXMLLoader firstInfoLoader = ScreenUtil.loadTemplate("ProjectFirstInfo");
			firstInfoLoader.setController(firstInfoController);
			AnchorPane firstInfoPane = firstInfoLoader.load();
			tabPane.getTabs().add(new Tab("INFORMAÇÕES", firstInfoPane));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initActivities() {
		try {
			FXMLLoader actlivityListLoader = ScreenUtil.loadTemplate("ActivityList");
			hboxContent.getChildren().add(actlivityListLoader.load());
			activityListController = ((ActivityListController) actlivityListLoader.getController());
			ShowEditViewActivityObservableI.addShowEditViewActivityListener(new ShowEditViewActivityObserverI() {
				@Override
				public void showEditViewActivity(HashMap<String, Object> hmap) {
					removeEditViewPane();
					try {
						AnchorPane ap = null;
						FXMLLoader detailsFxml = ScreenUtil.loadTemplate("ActivityDetailsInserting");
						String action = (String) hmap.get("action");
						ActivityVO activity = (ActivityVO) hmap.get("activity");
						if (action.equalsIgnoreCase("cadastro")) {
							ap = detailsFxml.load();
							if(activity!=null)
								((ActivityDetailsInsertingController) detailsFxml.getController()).loadActivity(activity);
							if(selectedProject!=null)
								((ActivityDetailsInsertingController) detailsFxml.getController()).setProject(selectedProject);
						}else {
							detailsFxml = ScreenUtil.loadTemplate("ActivityDetailsView");
							ap = (AnchorPane) detailsFxml.load();
							((ActivityDetailsViewController) detailsFxml.getController()).loadActivity(activity);
						}
						
						ap.setMaxWidth(Double.POSITIVE_INFINITY);
						ap.setMaxHeight(Double.POSITIVE_INFINITY);
						hboxContent.getChildren().add(ap);
						HBox.setHgrow(ap, Priority.ALWAYS);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				private void removeEditViewPane() {
					while (hboxContent.getChildren().size() > 1) {
						hboxContent.getChildren().remove(1);
					}
				}
			});
			tabPane.getTabs().add(tbActivities);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setState(ProjectManagerState state) {
		this.state = state;
	}

	public void setSelectedProject(ProjectVO project) {
		this.selectedProject = project;
		firstInfoController.setProject(selectedProject);
		activityListController.listByProject(selectedProject);

	}

	protected void saveProject(ProjectVO project) {
		if (project.getId() != null)
			selectedProject = projectBO.update(project);
		else
			selectedProject = projectBO.save(project);
	}
}

class ProjectFirstInfoController implements Initializable {
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

enum ProjectManagerState {
	FIRST_INFO, ACTIVITIES, DEPENDENCIES;
}
