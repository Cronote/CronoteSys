package com.cronoteSys.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.cronoteSys.controller.components.cellfactory.ActivityCellFactory;
import com.cronoteSys.filter.ActivityFilter;
import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.ActivityBO.OnActivityAddedI;
import com.cronoteSys.model.bo.ActivityBO.OnActivityDeletedI;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.observer.ShowEditViewActivityObservableI;
import com.cronoteSys.observer.ShowEditViewActivityObserverI;
import com.cronoteSys.util.SessionUtil;
import com.sun.org.apache.bcel.internal.generic.LSTORE;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class ActivityListController implements Initializable, ShowEditViewActivityObservableI {

	ActivityBO actBO = new ActivityBO();
	@FXML
	private Button btnAddActivity;
	@FXML
	private ListView<ActivityVO> cardsList;
	private List<ActivityVO> activityList = new ArrayList<ActivityVO>();
	@FXML
	AnchorPane pane;
	private UserVO loggedUser;
	private ProjectVO selectedProject;
	@FXML
	Label titleLabel;
	@FXML
	HBox SearchGroup;
	ActivityFilter filter;

	public void listByProject(ProjectVO project) {
		selectedProject = project;
		setList(project);

		if (project != null) {
			titleLabel.setVisible(false);
			AnchorPane.setTopAnchor(SearchGroup, 15.0);
			AnchorPane.setTopAnchor(cardsList, 61.0);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");

		cardsList.setCellFactory(new ActivityCellFactory());
		initEvents();
		initObservers();

	}

	public void setList(ProjectVO project) {
		filter = new ActivityFilter(project != null ? project.getId() : null, loggedUser.getIdUser());
		activityList = actBO.listAll(filter);
		cardsList.setItems(FXCollections.observableArrayList(activityList));
		setBtnAddIcon();
	}

	private void setBtnAddIcon() {
		GlyphIcon<?> icon = null;
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
			public void onActivityAddedI(ActivityVO act, String action) {
				if (action.equalsIgnoreCase("save")) {
					activityList.add(0, act);
					cardsList.setItems(FXCollections.observableArrayList(activityList));
				} else {
					int selected = cardsList.getSelectionModel().getSelectedIndex();
					activityList.remove(selected);
					cardsList.setItems(FXCollections.observableArrayList(activityList));
					activityList.add(selected,act);
					cardsList.setItems(FXCollections.observableArrayList(activityList));
					cardsList.getSelectionModel().select(selected);
				}
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

	public int getActivityTotal() {
		return activityList.size();
	}

	public int getDoneActivitiesTotal() {
		int doneCount = 0;
		for (ActivityVO act : activityList) {
			if (StatusEnum.itsFinalized(act.getStats()))
				doneCount++;
		}
		return doneCount;
	}
}
