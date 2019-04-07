package com.cronoteSys.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Observable;
import java.util.ResourceBundle;

import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.vo.ActivityVO;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

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
	private Button btnDelete;

	private ActivityVO activity;

	public ActivityCardController() {

	}

	public ActivityCardController(ActivityVO activity) {
		this.activity = activity;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lblTitle.setText(activity.getTitle());
		lblStatus.setText(activity.getStats().getDescription());
		initEvents();
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
	}

}
