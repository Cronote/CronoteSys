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

import de.jensd.fx.glyphs.GlyphIcon;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ProjectManagerController implements Initializable {

	@FXML
	private Button btnNext;
	@FXML
	private Button btnBack;
	@FXML
	private BorderPane borderPane;
	StackPane pnlContent;
	HBox hboxContent = new HBox();
	ProjectManagerState state;
	ProjectFirstInfoController firstInfoController;
	ProjectBO projectBO = new ProjectBO();
	ProjectVO selectedProject;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (state == ProjectManagerState.FIRST_INFO)
			initFirstInfo();
		else if (state == ProjectManagerState.ACTIVITIES)
			initActivities();
		initEvents();
		AnimatedArrow animation = new AnimatedArrow();
		btnNext.setOnMouseEntered(animation);
		btnNext.setOnMouseExited(animation);
		btnBack.setOnMouseEntered(animation);
		btnBack.setOnMouseExited(animation);

	}

	private void initEvents() {
		btnNext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (state.equals(ProjectManagerState.FIRST_INFO)) {
					if (selectedProject == null) {
						if (firstInfoController.fielsFilleds()) {
							System.out.println("funfou");
							selectedProject = projectBO.save(firstInfoController.getProject());
						}
					} else {
						initActivities();
					}
				}
			}
		});
	}

	private void initFirstInfo() {
		pnlContent = new StackPane();
		pnlContent.getChildren().clear();
		FXMLLoader projectFirstInfoFXML = ScreenUtil.loadTemplate("ProjectFirstInfo");
		firstInfoController = new ProjectFirstInfoController();
		projectFirstInfoFXML.setController(firstInfoController);
		btnBack.setVisible(false);
		try {
			AnchorPane pnlFirstInfo = (AnchorPane) projectFirstInfoFXML.load();
			pnlContent.getChildren().add(pnlFirstInfo);
			borderPane.setCenter(pnlContent);
			borderPane.setPadding(new Insets(0));
		} catch (IOException e) {
			e.printStackTrace();
		}
		firstInfoController.setProject(selectedProject);
	}

	private void initActivities() {
		pnlContent = new StackPane();
		pnlContent.getChildren().clear();
		FXMLLoader activityListFXML = ScreenUtil.loadTemplate("ActivityList");
		btnBack.setVisible(true);
		try {
			TitledPane pnlActivities = (TitledPane) activityListFXML.load();
			System.out.println(selectedProject.getId());
			if (selectedProject != null)
				((ActivityListController) activityListFXML.getController()).listByProject(selectedProject);

			double height = pnlActivities.getPrefHeight() - 100;
			pnlActivities.setPrefHeight(height);
			hboxContent = new HBox(pnlActivities);
			pnlContent.getChildren().add(hboxContent);
			borderPane.setCenter(pnlContent);
			borderPane.setPadding(new Insets(0));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ShowEditViewActivityObservableI.addShowEditViewActivityListener(new ShowEditViewActivityObserverI() {

			private void removeEditViewPane() {
				while (hboxContent.getChildren().size() > 1) {
					hboxContent.getChildren().remove(1);
				}
			}

			@Override
			public void showEditViewActivity(HashMap<String, Object> hmap) {
				System.out.println("asd");
				removeEditViewPane();
				try {
					FXMLLoader detailsFxml = ScreenUtil.loadTemplate("DetailsInserting");
					String action = (String) hmap.get("action");
					if (action.equalsIgnoreCase("close")) {
						return;
					}
					if (action.equalsIgnoreCase("cadastro")) {
						ProjectVO project = (ProjectVO) hmap.get("project");
						if (project != null) {
							detailsFxml.setController(new ActivityDetailsController(project));
						} else
							detailsFxml.setController(new ActivityDetailsController(null));
					} else {
						ActivityVO activity = (ActivityVO) hmap.get("activity");
						if (action.equalsIgnoreCase("view")) {
							detailsFxml = ScreenUtil.loadTemplate("ActivityDetailsView");
						}
						detailsFxml.setController(new ActivityDetailsController(activity, action));
					}
					TitledPane pnlDetails = new TitledPane("DETALHES DA ATIVIDADE", detailsFxml.load());

					TitledPane p = ((TitledPane) hboxContent.getChildren().get(0));
					pnlDetails.setPrefHeight(p.getPrefHeight());
					pnlDetails.setPrefWidth(borderPane.getPrefWidth() - p.getPrefWidth());
					pnlDetails.setCollapsible(false);
					pnlDetails.setAlignment(Pos.CENTER);
					pnlDetails.getStyleClass().add("activityDetails");
					pnlDetails.setMaxHeight(Double.POSITIVE_INFINITY);
					hboxContent.getChildren().add(pnlDetails);
					HBox.setHgrow(pnlDetails, Priority.ALWAYS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		ActivityBO.addOnActivityDeletedListener(new OnActivityDeletedI() {
			@Override
			public void onActivityDeleted(ActivityVO act) {
				hboxContent.getChildren().remove(1);
			}
		});
		btnBack.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				initFirstInfo();

			}
		});
	}

	public void setState(ProjectManagerState state) {
		this.state = state;
	}

	public void setSelectedProject(ProjectVO selectedProject) {
		this.selectedProject = selectedProject;
	}

	class AnimatedArrow implements EventHandler<Event> {
		FadeTransition fade = new FadeTransition();

		@Override
		public void handle(Event event) {
			Button btn = (Button) event.getTarget();
			GlyphIcon<?> icon = (GlyphIcon<?>) btn.getGraphic();

			fade.setDuration(Duration.seconds(1));
			fade.setFromValue(5);
			fade.setToValue(0.1);
			fade.setCycleCount(Timeline.INDEFINITE);
			// the transition will set to be auto reversed by setting this to true
			fade.setAutoReverse(true);
			fade.setNode(icon);
			System.out.println(event.getEventType());
			if (event.getEventType().equals(MouseEvent.MOUSE_ENTERED))
				fade.playFromStart();
			else if (event.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
				fade.stop();
				icon.setOpacity(1);
			}

		}

	}
}

class ProjectFirstInfoController implements Initializable {
	@FXML
	private AnchorPane firstInfoRoot;
	@FXML
	private TextField txtTitle;
	@FXML
	private TextArea txtDescription;
	@FXML
	private TabPane tabDeadline;
	@FXML
	private DatePicker dtpByDateStart;
	@FXML
	private DatePicker dtpByDateEnd;
	@FXML
	private DatePicker dtpByTimeStart;
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initDefaults();
	}

	private void initDefaults() {
		spnTotalTimeHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 0, 1));
		spnTotalTimeMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 1, 1));
		spnHourADay.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 0, 1));
		spnMinuteADay.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 1, 1));
		dtpByDateStart.setValue(LocalDate.now());
		dtpByTimeStart.setValue(LocalDate.now());
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
		ProjectVO proj = new ProjectVO();
		proj.setTitle(txtTitle.getText());
		proj.setDescription(txtDescription.getText());
		if (tabDeadline.getSelectionModel().getSelectedIndex() == 0) {
			proj.setStartDate(LocalDateTime.of(dtpByDateStart.getValue(), LocalTime.MIN));
			proj.setFinishDate(LocalDateTime.of(dtpByDateEnd.getValue(), LocalTime.MAX));
		}
		proj.setUserVO((UserVO) SessionUtil.getSESSION().get("loggedUser"));

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
