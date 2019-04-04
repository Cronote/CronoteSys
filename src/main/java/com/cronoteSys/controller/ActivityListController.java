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

public class ActivityListController implements Initializable, Observer {

	@FXML
	private Button btnAddActivity;
	@FXML
	private FlowPane cardsList;
	private List<ActivityVO> activityList = new ArrayList<ActivityVO>();

	private UserVO loggedUser;

	public ActivityListController(UserVO loggedUser) {
		this.loggedUser = loggedUser;
		System.out.println("adoadoado");
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
					cardsList.getChildren().add(0, (Node) card.load());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void btnAddActivityClicked(ActionEvent e) {
		System.out.println("hueuehue falando");
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
		FXMLLoader card = loadTemplate("ActivitCard");
		ActivityCardController acController = new ActivityCardController(act);
		card.setController(acController);
		acController.addObserver(this);
		return card;
	}

	private FXMLLoader loadTemplate(String template) {
		FXMLLoader root = null;

		System.out.println("template>>>> " + template);
		root = new FXMLLoader(getClass().getResource("/fxml/Templates/" + template + ".fxml"));
		return root;
	}

	private void initEvents() {
		btnAddActivity.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("ue");
				btnAddActivityClicked(event);

			}
		});
	}

	private static ArrayList<btnAddActivityClickedI> listeners = new ArrayList<btnAddActivityClickedI>();

	public interface btnAddActivityClickedI {
		void btnAddActivityClicked(HashMap<String, Object> hmp);
	}

	public static void addBtnAddActivityClickedListener(btnAddActivityClickedI newListener) {
		listeners.add(newListener);
	}

	private void notifyAllListeners(HashMap<String, Object> hmp) {
		for (btnAddActivityClickedI l : listeners) {
			l.btnAddActivityClicked(hmp);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Card clickado");
		if (arg instanceof HashMap<?, ?>) {
			String action = ((HashMap<?, ?>) arg).get("action").toString();
			ActivityVO act = (ActivityVO) ((HashMap<?, ?>) arg).get("activity");
			if (action.equalsIgnoreCase("remove")) {
				activityList.remove(act);
				renderList();
			} else {
				notifyAllListeners((HashMap<String, Object>) arg);

			}
		}
	}

}
