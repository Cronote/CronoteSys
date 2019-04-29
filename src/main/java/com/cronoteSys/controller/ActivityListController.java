package com.cronoteSys.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.cronoteSys.controller.ActivityCardController.OnProgressChangedI;
import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.ExecutionTimeBO;
import com.cronoteSys.model.bo.ActivityBO.OnActivityAddedI;
import com.cronoteSys.model.bo.ActivityBO.OnActivityDeletedI;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.ActivityMonitor;
import com.cronoteSys.util.SessionUtil;
import com.cronoteSys.util.ActivityMonitor.OnMonitorTick;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Skin;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ActivityListController implements Initializable, ShowEditViewActivityObservableI {

	@FXML
	private Button btnAddActivity;
	@FXML
	private ListView<ActivityVO> cardsList;
	private List<ActivityVO> activityList = new ArrayList<ActivityVO>();
	@FXML
	TitledPane title;
	ActivityBO actBO = new ActivityBO();
	private UserVO loggedUser;
	private ProjectVO selectedProject;

	public void listByProject(ProjectVO project) {
		activityList = actBO.listAllByUserAndProject(loggedUser, project);
		selectedProject = project;
		cardsList.setItems(FXCollections.observableArrayList(activityList));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loggedUser = (UserVO) SessionUtil.getSESSION().get("loggedUser");
		activityList = actBO.listAllByUserAndProject(loggedUser, null);
		cardsList.setItems(FXCollections.observableArrayList(activityList));
		cardsList.setCellFactory(new ActivityCellFactory());
		initEvents();
		initObservers();
		GlyphIcon icon = null;
		if (activityList.size() > 0) {
			icon = new MaterialDesignIconView(MaterialDesignIcon.PLAYLIST_PLUS);
			icon.getStyleClass().addAll("normal-white");
		} else {
			icon = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
			icon.getStyleClass().addAll("outline-white", "rainforce");
		}
		icon.setSize("3em");
		btnAddActivity.setGraphic(icon);
	}

	private void initObservers() {
		ActivityBO.addOnActivityAddedIListener(new OnActivityAddedI() {
			@Override
			public void onActivityAddedI(ActivityVO act) {
				activityList.add(0, act);
				cardsList.setItems(FXCollections.observableArrayList(activityList));
			}
		});
		ActivityBO.addOnActivityDeletedListener(new OnActivityDeletedI() {
			@Override
			public void onActivityDeleted(ActivityVO act) {
				activityList.remove(act);
				cardsList.setItems(FXCollections.observableArrayList(activityList));
			}
		});
	
	}

	private void btnAddActivityClicked(ActionEvent e) {
		HashMap<String, Object> hmp = new HashMap<String, Object>();
		
		hmp.put("project", selectedProject);
		hmp.put("action", "cadastro");

		notifyAllListeners(hmp);

	}

	private void initEvents() {
		btnAddActivity.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				cardsList.getSelectionModel().clearSelection();
				btnAddActivityClicked(event);

			}
		});
	}

	@Override
	public void notifyAllListeners(HashMap<String, Object> hmp) {
		for (ShowEditViewActivityObserverI l : listeners) {
			l.showEditViewActivity(hmp);
		}

	}

//	@Override
//	public void update(Observable o, Object arg) {
//		if (arg instanceof HashMap<?, ?>) {
//			String action = ((HashMap<?, ?>) arg).get("action").toString();
//			ActivityVO act = (ActivityVO) ((HashMap<?, ?>) arg).get("activity");
//			if (action.equalsIgnoreCase("remove")) {
//				activityList.remove(act);
//				cardsList.setItems(FXCollections.observableArrayList(activityList));
//				
//			} else {
//				notifyAllListeners((HashMap<String, Object>) arg);
//
//			}
//		}
//	}

}

class ActivityCellFactory implements Callback<ListView<ActivityVO>, ListCell<ActivityVO>> {
	@Override
	public ListCell<ActivityVO> call(ListView<ActivityVO> listview) {
		return new CardCell();
	}

}

class CardCell extends ListCell<ActivityVO> implements ShowEditViewActivityObservableI {
	@FXML
	private AnchorPane activityCardRoot;
	@FXML
	private Label lblTitle;
	@FXML
	private Label lblStatus;
	@FXML
	private ProgressBar pgbProgress;
	@FXML
	private Label lblProgress;
	@FXML
	private Button btnDelete;
	@FXML
	private Label lblIndex;
	@FXML
	private Button btnFinalize;
	@FXML
	private Button btnPlayPause;
	private ActivityVO activity;

	{

		FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
		try {
			loader.setLocation(new File(getClass().getResource("/fxml/Templates/cell/ActivityCell.fxml").getPath())
					.toURI().toURL());
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void updateSelected(boolean selected) {
		super.updateSelected(selected);
		HashMap<String, Object> hmp = new HashMap<String, Object>();
		if (selected) {
			activityCardRoot.getStyleClass().add("activityCardSelected");
			hmp.put("action", "view");
			hmp.put("activity", activity);
			hmp.put("project", activity.getProjectVO());
			notifyAllListeners(hmp);
		} else
			activityCardRoot.getStyleClass().removeAll("activityCardSelected");

	}

	@Override
	protected void updateItem(ActivityVO item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null || !empty) {
			initEvents();
			activity = item;
			loadActivity();
			setGraphic(activityCardRoot);
			
			
		} else {
			setGraphic(null);
			setText(null);
		}
		setStyle("-fx-background-color:transparent;");
	}

	private void loadActivity() {
		lblTitle.setText(activity.getTitle());

		loadProgressAndRealtime();
		GlyphIcon icon = null;
		String btnText = "";
		if (activity.getStats() != StatusEnum.NORMAL_FINALIZED && activity.getStats() != StatusEnum.BROKEN_FINALIZED) {
			if (activity.getStats() == StatusEnum.NOT_STARTED || activity.getStats() == StatusEnum.NORMAL_PAUSED
					|| activity.getStats() == StatusEnum.BROKEN_PAUSED) {

				icon = new FontAwesomeIconView(FontAwesomeIcon.PLAY_CIRCLE_ALT);
				btnText = "play";
				btnFinalize.getStyleClass().removeAll("show");
				icon.setSize("2em");
			} else if (activity.getStats().equals(StatusEnum.BROKEN_IN_PROGRESS)
					|| activity.getStats().equals(StatusEnum.NORMAL_IN_PROGRESS)) {

				icon = new FontAwesomeIconView(FontAwesomeIcon.PAUSE_CIRCLE_ALT);
				btnText = "pause";
				btnFinalize.getStyleClass().add("show");
				icon.setSize("2em");
			}
			btnPlayPause.setGraphic(icon);
			btnPlayPause.setText(btnText);
			btnPlayPause.getStyleClass().add("show");
		}
		StackPane sp = (StackPane) pgbProgress.lookup(".bar");
		if (sp != null)
			sp.setStyle("-fx-background-color:" + activity.getStats().getHexColor());
	}

	private void loadProgressAndRealtime() {
		lblStatus.setText(activity.getStats().getDescription());
		Node bar = pgbProgress.lookup(".bar");
		if (bar != null)
			bar.setStyle("-fx-background-color:" + activity.getStats().getHexColor());
		double estimatedTime = activity.getEstimatedTime().toMillis();
		double realtime = activity.getRealtime().toMillis();
		double progress = realtime / estimatedTime;
		double difference = progress - 1;
		if (difference != 0) {
			FontAwesomeIconView icon = new FontAwesomeIconView();
			Tooltip tp = new Tooltip();
			if (difference > 0) {
				if (difference <= 0.75) {
					icon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOWN);
				} else {
					icon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOUBLE_DOWN);
					icon.setSize("1.5em");
				}
				icon.setFill(Color.RED);
				tp.setText("Percentual de tempo excedido");
				lblIndex.setStyle("-fx-text-fill:red");
			} else {
				if (difference >= -0.35) {
					icon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_UP);
				} else {
					icon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOUBLE_UP);
					icon.setSize("1.5em");
				}
				icon.setFill(Color.GREEN);
				tp.setText("Percentual de tempo desnecessário");
				lblIndex.setStyle("-fx-text-fill:green");

			}
			lblIndex.getStyleClass().removeAll("hide");
			lblIndex.setText(String.format("%.2f", Math.abs((difference * 100))) + "%");
			lblIndex.setGraphic(icon);
			Tooltip.install(lblIndex, tp);

		}
		progress = progress > 1 ? 1 : progress;
		if (activity.getStats() == StatusEnum.BROKEN_FINALIZED || activity.getStats() == StatusEnum.NORMAL_FINALIZED)
			pgbProgress.setProgress(1);
		else
			pgbProgress.setProgress(progress);
		String progressStr = String.format("%.2f", Math.abs((pgbProgress.getProgress() * 100)));
		lblProgress.setText(progressStr + "%");
		notifyAllOnProgressChangedListeners(activity);
	}

	private void initEvents() {

		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				new ActivityBO().delete(activity);

			}
		});

		btnPlayPause.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ExecutionTimeBO execBo = new ExecutionTimeBO();
				ActivityBO actBo = new ActivityBO();
				if (btnPlayPause.getText().equalsIgnoreCase("play")) {
					if (execBo.startExecution(activity) != null) {
						activity = actBo.switchStatus(activity, StatusEnum.NORMAL_IN_PROGRESS);
						if (btnDelete.isVisible()) {
							btnDelete.getStyleClass().remove("show");
						}
						ActivityMonitor.addActivity(activity);
					}
				} else {
					if (execBo.finishExecution(activity) != null) {
						activity = actBo.switchStatus(activity, StatusEnum.NORMAL_PAUSED);
						ActivityMonitor.removeActivity(activity);
					}
				}
				loadActivity();

			}
		});
		btnFinalize.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ExecutionTimeBO execBo = new ExecutionTimeBO();
				ActivityBO actBo = new ActivityBO();

				if (execBo.finishExecution(activity) != null) {
					activity = actBo.switchStatus(activity, StatusEnum.NORMAL_FINALIZED);
					btnPlayPause.getStyleClass().removeAll("show");
					btnFinalize.getStyleClass().removeAll("show");
					loadActivity();

					ActivityMonitor.removeActivity(activity);
				}

			}
		});
		setOnMouseEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if (activity.getStats().equals(StatusEnum.NOT_STARTED)) {
					btnDelete.getStyleClass().add("show");
				}

			}
		});
		setOnMouseExited(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				btnDelete.getStyleClass().removeAll("show");

			}
		});

		pgbProgress.skinProperty().addListener(new ChangeListener<Skin>() {

			@Override
			public void changed(ObservableValue<? extends Skin> observable, Skin oldValue, Skin newValue) {
				paintBar(pgbProgress.lookup(".bar"));

			}
		});
		pgbProgress.progressProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				paintBar(pgbProgress.lookup(".bar"));

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

	private void paintBar(Node node) {
		if (node != null)
			node.setStyle("-fx-background-color:" + activity.getStats().getHexColor());
	}

	private static ArrayList<OnProgressChangedI> activityAddedListeners = new ArrayList<OnProgressChangedI>();

	public interface OnProgressChangedI {
		void onProgressChangedI(ActivityVO act);
	}

	public static void addOnActivityAddedIListener(OnProgressChangedI newListener) {
		activityAddedListeners.add(newListener);
	}

	private void notifyAllOnProgressChangedListeners(ActivityVO act) {
		for (OnProgressChangedI l : activityAddedListeners) {
			l.onProgressChangedI(act);
		}
	}

	@Override
	public void notifyAllListeners(HashMap<String, Object> hmp) {
		for (ShowEditViewActivityObserverI l : listeners) {
			l.showEditViewActivity(hmp);
		}

	}

}