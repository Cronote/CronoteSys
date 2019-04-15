package com.cronoteSys.controller;

import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Observable;
import java.util.ResourceBundle;

import javax.swing.Timer;

import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.ExecutionTimeBO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.StatusEnum;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Skin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

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
	private Button btnDelete;
	@FXML
	private Button btnPlayPause;
	@FXML
	private Button btnFinalize;

	private ActivityVO activity;
	Timer timer = new Timer(60000, new ActionListener() {

		public void actionPerformed(java.awt.event.ActionEvent e) {
			Platform.runLater(new Runnable() {
				public void run() {
					ActivityBO actBo = new ActivityBO();
					activity = actBo.updateRealTime(activity);
					loadActivity();
				}
			});

		}
	});

	public ActivityCardController() {

	}

	public ActivityCardController(ActivityVO activity) {
		this.activity = activity;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadActivity();
		initEvents();
		timer.setInitialDelay(0);
	}

	private void loadActivity() {
		lblTitle.setText(activity.getTitle());
		lblStatus.setText(activity.getStats().getDescription());

		double estimatedTime = activity.getEstimatedTime().toMillis();
		double realtime = activity.getRealtime().toMillis();
		double progress = realtime / estimatedTime;
		progress = progress > 1 ? 1 : progress;
		pgbProgress.setProgress(progress);
		String progressStr = String.format("%.2f", (pgbProgress.getProgress() * 100));
		lblProgress.setText(progressStr + "%");
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
					execBo.startExecution(activity);
					activity = actBo.switchStatus(activity, StatusEnum.NORMAL_IN_PROGRESS);
					if (btnDelete.isVisible()) {
						btnDelete.getStyleClass().add("hide");
					}
					timer.start();
				} else {
					execBo.finishExecution(activity);
					activity = actBo.switchStatus(activity, StatusEnum.NORMAL_PAUSED);
					timer.stop();
				}
				loadActivity();

			}
		});
		btnFinalize.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ExecutionTimeBO execBo = new ExecutionTimeBO();
				ActivityBO actBo = new ActivityBO();

				execBo.finishExecution(activity);
				activity = actBo.switchStatus(activity, StatusEnum.NORMAL_FINALIZED);
				loadActivity();

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

	}

}
