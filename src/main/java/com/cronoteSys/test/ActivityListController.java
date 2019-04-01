package com.cronoteSys.test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import com.cronoteSys.controller.ActivityCardController;
import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.ActivityBO.OnActivityAddedI;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.util.ScreenUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class ActivityListController implements Initializable, Observer {

	@FXML
	Button btnAddActivity;
	@FXML
	FlowPane cardsList;
	List<ActivityVO> activityList = new ArrayList<ActivityVO>();

	public void btnAddActivityClicked(ActionEvent e) {
		System.out.println("hueuehue falando");
		notifyAllListeners("", null);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ActivityBO actBO = new ActivityBO();
		activityList = actBO.listAll();
		renderList();
		ActivityBO.addOnActivityAddedIListener(new OnActivityAddedI() {
			@Override
			public void onActivityAddedI(ActivityVO act) {
				FXMLLoader card = makeCard(act);
				activityList.add(0, act);
				try {
					cardsList.getChildren().add(0, (Node) card.load());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	private void renderList() {
		for (ActivityVO act : activityList) {
			try {
				cardsList.getChildren().add((Node) makeCard(act).load());
			} catch (IOException e) {
				// TODO Auto-generated catch block
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

	private static ArrayList<btnAddActivityClickedI> listeners = new ArrayList<btnAddActivityClickedI>();

	public interface btnAddActivityClickedI {
		void btnAddActivityClicked(String newScreen, HashMap<String, Object> hmap);
	}

	public static void addBtnAddActivityClickedListener(btnAddActivityClickedI newListener) {
		listeners.add(newListener);
	}

	private void notifyAllListeners(String newScreen, HashMap<String, Object> hmap) {
		for (btnAddActivityClickedI l : listeners) {
			l.btnAddActivityClicked(newScreen, hmap);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		HBox root = ScreenUtil.recoverRoot(btnAddActivity);

	}

}
