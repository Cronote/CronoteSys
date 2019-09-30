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
import com.cronoteSys.util.SessionUtil;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class ActivityListController implements Initializable {

	@FXML
	private Button btnAddActivity;
	@FXML
	private ListView<ActivityVO> cardsList;
	@FXML
	private AnchorPane pane;
	@FXML
	private Label titleLabel;
	@FXML
	private HBox SearchGroup;

	private UserVO loggedUser;
	private ProjectVO selectedProject;
	private ActivityFilter filter;
	private ListProperty<ActivityVO> activityList = new SimpleListProperty<ActivityVO>();
	private ActivityBO actBO = new ActivityBO();

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
		filter = new ActivityFilter(project != null ? project.getId() : null,
				project != null ? null : loggedUser.getIdUser());
		List<ActivityVO> lst = actBO.listAll(filter);
		activityList.set(FXCollections.observableArrayList(lst));
		cardsList.itemsProperty().bind(activityList);
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
					cardsList.refresh();
					cardsList.getSelectionModel().select(0);
				} else {
					int selected = cardsList.getSelectionModel().getSelectedIndex();
					if (selected > -1) {
						activityList.remove(selected);
						activityList.add(selected, act);
						cardsList.refresh();
						cardsList.getSelectionModel().select(selected);
					}
				}
			}
		});
		ActivityBO.addOnActivityDeletedListener(new OnActivityDeletedI() {
			@Override
			public void onActivityDeleted(ActivityVO act) {
				activityList.remove(act);
				cardsList.refresh();
			}
		});
	}

	private void btnAddActivityClicked(ActionEvent e) {
		HashMap<String, Object> hmp = new HashMap<String, Object>();
		hmp.put("project", selectedProject);
		hmp.put("action", "cadastro");
		notifyAllActivitySelectedListeners(hmp);
	}

	private void initEvents() {
		btnAddActivity.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				cardsList.getSelectionModel().clearSelection();
				btnAddActivityClicked(event);
			}
		});
		cardsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ActivityVO>() {

			@Override
			public void changed(ObservableValue<? extends ActivityVO> observable, ActivityVO oldValue,
					ActivityVO newValue) {
				HashMap<String, Object> hmp = new HashMap<String, Object>();
				if (newValue != null) {
					hmp.put("activity", newValue);
					hmp.put("project", newValue.getProjectVO());
					hmp.put("action", "view");
					notifyAllActivitySelectedListeners(hmp);
				}
			}
		});
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

	public List<ActivityVO> getList() {
		return activityList;
	}

	private static ArrayList<ActivitySelectedI> activitySelectedListeners = new ArrayList<ActivitySelectedI>();

	public interface ActivitySelectedI {
		void onActivitySelected(HashMap<String, Object> hmp);
	}

	public static void addOnActivitySelectedListener(ActivitySelectedI newListener) {
		activitySelectedListeners.clear();
		activitySelectedListeners.add(newListener);
	}

	public static void notifyAllActivitySelectedListeners(HashMap<String, Object> hmp) {
		for (ActivitySelectedI l : activitySelectedListeners) {
			l.onActivitySelected(hmp);
		}
	}
}
