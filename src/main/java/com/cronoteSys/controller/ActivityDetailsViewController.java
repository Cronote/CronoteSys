package com.cronoteSys.controller;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.controlsfx.control.Rating;

import com.cronoteSys.controller.ActivityListController.ActivitySelectedI;
import com.cronoteSys.controller.components.cellfactory.SimpleActivityCellFactory;
import com.cronoteSys.interfaces.LoadActivityInterface;
import com.cronoteSys.interfaces.LoadProjectInterface;
import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.SimpleActivity;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.observer.ShowEditViewActivityObservableI;
import com.cronoteSys.observer.ShowEditViewActivityObserverI;
import com.cronoteSys.util.ActivityMonitor;
import com.cronoteSys.util.ActivityMonitor.OnMonitorTick;
import com.cronoteSys.util.SessionUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Skin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.TitledPane;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXButton;

public class ActivityDetailsViewController implements Initializable, LoadActivityInterface, LoadProjectInterface {
	@FXML
	private Label lblPaneTitle;
	// Visualização
	@FXML
	private Label lblTitle;
	@FXML
	private Label lblCategory;
	@FXML
	private Label lblDescription;
	@FXML
	private Label lblEstimatedTime;
	@FXML
	private Label lblRealTime;
	@FXML
	private ProgressIndicator pgiProgress;
	@FXML
	private Label lblStatus;
	@FXML
	private Label lblLastModified;
	@FXML
	private Label lblDescriptionLimit;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnDelete;
	@FXML
	private Rating ratePriority;
	@FXML
	private AnchorPane detailsRoot;

	HashMap<String, Object> hmp = new HashMap<String, Object>();
	private ActivityVO activity = new ActivityVO();
	private UserVO loggedUser;
	@FXML
	VBox pnlDependenciesBox;
	@FXML
	JFXListView<SimpleActivity> lstDependencies;
	@FXML
	JFXButton btnBackward;
	@FXML
	JFXButton btnForward;

	private void initActivity() {
		loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");
		activity.setUserVO(loggedUser);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initEvents();
		initActivity();

	}

	public void loadActivity(ActivityVO act) {
		activity = act;
		lblTitle.setText(activity.getTitle());
		lblCategory.setText(activity.getCategoryVO().getDescription());
		lblDescription.setText(activity.getDescription());
		ratePriority.setRating(activity.getPriority());
		lblEstimatedTime.setText(activity.getEstimatedTimeAsString());
		loadProgressAndRealtime();
		lblLastModified.setText(activity.getLastModification()
				.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm", new Locale("pt", "BR"))));
		if (activity.getStats() == StatusEnum.NOT_STARTED) {
			btnEdit.getStyleClass().add("show");
			btnDelete.getStyleClass().add("show");
		} else if (!StatusEnum.itsFinalized(activity.getStats())) {
			btnEdit.getStyleClass().add("show");
		}
		ratePriority.setDisable(true);
		if (activity.getProjectVO() != null) {
			pnlDependenciesBox.setVisible(true);
			lstDependencies.setCellFactory(new SimpleActivityCellFactory());
			lstDependencies
					.setItems(FXCollections.observableArrayList(SimpleActivity.fromList(activity.getDependencies())));
			lstDependencies.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					lstDependencies.scrollTo(newValue.intValue());

				}
			});
			btnBackward.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					int selectedIndex = lstDependencies.getSelectionModel().getSelectedIndex();
					lstDependencies.getSelectionModel().select(--selectedIndex);
				}
			});
			btnForward.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					int selectedIndex = lstDependencies.getSelectionModel().getSelectedIndex();
					lstDependencies.getSelectionModel().select(++selectedIndex);
				}
			});
		}
		if(activity.itsDependency()) {
			btnDelete.getStyleClass().removeAll("show");
		}
	}

	private void loadProgressAndRealtime() {
		lblRealTime.setText(activity.getRealtimeAsString());
		double estimatedTime = activity.getEstimatedTime().toMillis();
		double realtime = activity.getRealtime().toMillis();
		double progress = realtime / estimatedTime;
		progress = progress > 1 ? 1 : progress;
		lblStatus.setText(activity.getStats().getDescription().toUpperCase());
		if (StatusEnum.itsFinalized(activity.getStats()))
			pgiProgress.setProgress(1);
		else
			pgiProgress.setProgress(progress);
		StackPane stackProgress = (StackPane) pgiProgress.lookup(".progress");
		Text textPercentage = (Text) pgiProgress.lookup(".percentage");
		String progressStr = String.format("%.2f", (pgiProgress.getProgress() * 100));
		if (textPercentage != null)
			textPercentage.setText(progressStr + "%");
		if (stackProgress != null)
			stackProgress.setStyle("-fx-background-color:" + activity.getStats().getHexColor());
	}

	private void initEvents() {
		if (btnEdit != null) {
			btnEdit.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					hmp.put("activity", activity);
					hmp.put("project", activity.getProjectVO());
					hmp.put("action", "cadastro");
					ActivityListController.notifyAllActivitySelectedListeners(hmp);

				}
			});
		}
		if (btnDelete != null) {
			btnDelete.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					new ActivityBO().delete(activity);
				}
			});
		}
		if (pgiProgress != null) {
			pgiProgress.skinProperty().addListener((ChangeListener<Skin<?>>) (observable, oldValue, newValue) -> {
				if (activity.getStats() != null) {
					StackPane stackProgress = (StackPane) pgiProgress.lookup(".progress");
					Text textPercentage = (Text) pgiProgress.lookup(".percentage");
					String progressStr = String.format("%.2f", (pgiProgress.getProgress() * 100));
					if (textPercentage != null)
						textPercentage.setText(progressStr + "%");
					if (stackProgress != null)
						stackProgress.setStyle("-fx-background-color:" + activity.getStats().getHexColor());
				}
			});
			ActivityMonitor.addOnMonitorTickListener(new OnMonitorTick() {
				@Override
				public void onMonitorTicked(ActivityVO act) {
					if (act.getId() == activity.getId())
						activity = act;
					loadProgressAndRealtime();
				}
			});
		}
	}

	

	@Override
	public void loadProject(ProjectVO proj) {
		//Not necessary
	}
}