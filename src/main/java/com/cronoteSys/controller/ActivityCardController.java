package com.cronoteSys.controller;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

import com.cronoteSys.model.vo.ActivityVO;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

public class ActivityCardController extends Observable implements Initializable {

	@FXML
	AnchorPane cardRoot;

	@FXML
	Label lblTitle;

	@FXML
	Label lblStatus;

	@FXML
	ProgressBar pgbProgress;

	ActivityVO activity;
	
	

	public ActivityCardController() {
		// TODO Auto-generated constructor stub
	}

	public ActivityCardController(ActivityVO activity) {
		this.activity = activity;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lblTitle.setText(activity.get_title());
		lblStatus.setText(activity.get_stats().toString());
		
		cardRoot.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				setChanged();
				notifyObservers();
			}
		});
	}
	


}
