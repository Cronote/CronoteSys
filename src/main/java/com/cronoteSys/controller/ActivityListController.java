package com.cronoteSys.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.ActivityBO.OnActivityAddedI;
import com.cronoteSys.model.bo.ActivityBO.OnActivityDeletedI;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.UserVO;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class ActivityListController extends ShowEditViewActivityObservable implements Initializable, Observer {

	@FXML
	private Button btnAddActivity;
	@FXML
	private FlowPane cardsList;
	private List<ActivityVO> activityList = new ArrayList<ActivityVO>();

	private UserVO loggedUser;

	public ActivityListController(UserVO loggedUser) {
		this.loggedUser = loggedUser;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ActivityBO actBO = new ActivityBO();
		activityList = actBO.listAllByUser(loggedUser);
		renderList();
		initEvents();
		initObservers();

	}

	private void initObservers() {
		ActivityBO.addOnActivityAddedIListener(new OnActivityAddedI() {
			@Override
			public void onActivityAddedI(ActivityVO act) {
				FXMLLoader card = makeCard(act);
				activityList.add(0, act);
				try {
					Node node = (Node) card.load();
					node.getStyleClass().add("activityCardSelected");
					cardsList.getChildren().add(0, node);
					deselectOthersCards(node);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		ActivityBO.addOnActivityDeletedListener(new OnActivityDeletedI() {
			@Override
			public void onActivityDeleted(ActivityVO act) {
				activityList.remove(act);
				renderList();
			}
		});
	}

	private void btnAddActivityClicked(ActionEvent e) {
		notifyAllListeners(null);

	}

	private void renderList() {
		while (cardsList.getChildren().size() > 0)
			cardsList.getChildren().remove(0);
		for (ActivityVO act : activityList) {
			try {
				cardsList.getChildren().add((Node) makeCard(act).load());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private FXMLLoader makeCard(ActivityVO act) {
		FXMLLoader card = loadTemplate("ActivityCard");
		ActivityCardController acController = new ActivityCardController(act);
		card.setController(acController);
		acController.addObserver(this);
		return card;
	}

	private FXMLLoader loadTemplate(String template) {
		FXMLLoader root = null;

		root = new FXMLLoader(getClass().getResource("/fxml/Templates/" + template + ".fxml"));
		return root;
	}

	private void initEvents() {
		btnAddActivity.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				btnAddActivityClicked(event);

			}
		});
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof HashMap<?, ?>) {
			String action = ((HashMap<?, ?>) arg).get("action").toString();
			ActivityVO act = (ActivityVO) ((HashMap<?, ?>) arg).get("activity");
			Node card = (Node) ((HashMap<?, ?>) arg).get("card");
			deselectOthersCards(card);
			if (action.equalsIgnoreCase("remove")) {
				activityList.remove(act);
				renderList();
			} else {
				notifyAllListeners((HashMap<String, Object>) arg);

			}
		}
	}

	public void deselectOthersCards(Node card) {
		for (Node node : cardsList.getChildren()) {
			if (!node.equals(card)) {
				node.getStyleClass().remove("activityCardSelected");
			}
		}

	}
}
