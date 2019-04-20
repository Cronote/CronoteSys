package com.cronoteSys.controller;

import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.ResourceBundle;

import javax.swing.Timer;
import javax.swing.text.html.CSS;

import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.ExecutionTimeBO;
import com.cronoteSys.model.bo.ActivityBO.OnActivityAddedI;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.util.ActivityMonitor;
import com.cronoteSys.util.ActivityMonitor.OnMonitorTick;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ActivityCardController extends Observable implements Initializable {

	@FXML
	private AnchorPane cardRoot;

	@FXML
	private Label lblTitle;

	@FXML
	private Label lblStatus;

	@FXML
	private ProgressBar pgbProgress;

	@FXML
	private Label lblProgress;
	@FXML
	private Label lblIndex;

	@FXML
	private Button btnDelete;
	@FXML
	private Button btnPlayPause;
	@FXML
	private Button btnFinalize;

	private ActivityVO activity;

	public ActivityCardController() {

	}

	public ActivityCardController(ActivityVO activity) {
		this.activity = activity;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadActivity();
		initEvents();
	}

	private void loadActivity() {
		lblTitle.setText(activity.getTitle());

		loadProgressAndRealtime();
		GlyphIcon icon = null;
		String btnText = "";
		System.out.println(activity.getStats());
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
	}

	private void initEvents() {
		HashMap<String, Object> hmp = new HashMap<String, Object>();
		btnDelete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				cardRoot.getStyleClass().remove("activityCardSelected");
				new ActivityBO().delete(activity);
				hmp.put("action", "remove");
				hmp.put("activity", activity);
				setChanged();
				notifyObservers(hmp);

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
		cardRoot.setOnMouseEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if (activity.getStats().equals(StatusEnum.NOT_STARTED)) {
					btnDelete.getStyleClass().add("show");
				}

			}
		});

		cardRoot.setOnMouseExited(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				btnDelete.getStyleClass().removeAll("show");

			}
		});

		cardRoot.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				cardRoot.getStyleClass().remove("activityCardSelected");
				cardRoot.getStyleClass().add("activityCardSelected");
				hmp.put("action", "view");
				hmp.put("activity", activity);
				hmp.put("card", cardRoot);
				setChanged();
				notifyObservers(hmp);
			}
		});

		pgbProgress.skinProperty().addListener(new ChangeListener<Skin>() {

			@Override
			public void changed(ObservableValue<? extends Skin> observable, Skin oldValue, Skin newValue) {
				pgbProgress.lookup(".bar").setStyle("-fx-background-color:" + activity.getStats().getHexColor());
			}
		});
		pgbProgress.progressProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				pgbProgress.lookup(".bar").setStyle("-fx-background-color:" + activity.getStats().getHexColor());

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

}
