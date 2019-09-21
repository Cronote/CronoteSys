package com.cronoteSys.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.cronoteSys.controller.ActivityListController.ActivitySelectedI;
import com.cronoteSys.interfaces.LoadActivityInterface;
import com.cronoteSys.interfaces.LoadProjectInterface;
import com.cronoteSys.model.bo.ProjectBO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.util.ScreenUtil;
import com.google.inject.Inject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProjectManagerController implements Initializable {

	@Inject
	private ProjectBO projectBO;
	@FXML
	private TabPane tabPane;
	private HBox hboxContent = new HBox();;

	private ProjectFirstInfoController firstInfoController;
	private ProjectFirstInfoViewController firstInfoViewController;
	private ActivityListController activityListController;
	private ProjectVO selectedProject;
	private Tab tbActivities = new Tab("ATIVIDADES", hboxContent);

	public ProjectVO getSelectedProject() {
		return selectedProject != null ? selectedProject : new ProjectVO();
	}

	public void setSelectedProject(ProjectVO project, String mode) {
		this.selectedProject = project;
		initFirstInfo(mode);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		initActivities();
	}

	private void initEvents() {
	}

	public void initFirstInfo(String mode) {
		FXMLLoader firstInfoLoader = null;
		AnchorPane firstInfoPane = null;
		tabPane.getTabs().clear();
		hboxContent.getChildren().clear();
		try {
			if (mode.equals("cadastro")) {
				firstInfoLoader = ScreenUtil.loadTemplate("ProjectFirstInfo");
				firstInfoController = new ProjectFirstInfoController(this);
				firstInfoLoader.setController(firstInfoController);
				firstInfoPane = firstInfoLoader.load();
				firstInfoController.setProject(selectedProject);
			} else {
				firstInfoLoader = ScreenUtil.loadTemplate("ProjectFirstInfoView");
				firstInfoViewController = new ProjectFirstInfoViewController(this);
				firstInfoLoader.setController(firstInfoViewController);
				firstInfoPane = firstInfoLoader.load();
				firstInfoViewController.setProject(selectedProject);
			}
			tabPane.getTabs().add(0, new Tab("INFORMAÇÕES", firstInfoPane));

			initActivities();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initActivitiesObservers() {
		ActivityListController.addOnActivitySelectedListener(new ActivitySelectedI() {
			@Override
			public void onActivitySelected(HashMap<String, Object> hmp) {
				switchHBoxContent(hmp);
			}
		});
	}

	private void initActivities() {
		System.out.println(selectedProject);
		if (selectedProject != null) {
			try {

				FXMLLoader actlivityListLoader = ScreenUtil.loadTemplate("ActivityList");
				hboxContent.getChildren().add(actlivityListLoader.load());
				activityListController = ((ActivityListController) actlivityListLoader.getController());
				tabPane.getTabs().add(tbActivities);
				activityListController.listByProject(selectedProject);
				try {
					firstInfoViewController.setActivities(activityListController.getList());
				} catch (Exception e) {
					e.printStackTrace();
				}
				initActivitiesObservers();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void switchHBoxContent(HashMap<String, Object> hmap) {
		FXMLLoader loader = null;
		while (hboxContent.getChildren().size() > 1) {
			hboxContent.getChildren().remove(1);
		}
		try {
			String mode = (String) hmap.getOrDefault("action", "view");
			ActivityVO activity = (ActivityVO) hmap.get("activity");
			AnchorPane ap = null;
			if (mode.equals("view")) {
				loader = ScreenUtil.loadTemplate("ActivityDetailsView");
				ap = (AnchorPane) loader.load();

			} else {
				loader = ScreenUtil.loadTemplate("ActivityDetailsInserting");
				ap = (AnchorPane) loader.load();
			}
			ap.getStyleClass().add("tone3-background");
			if (activity != null)
				((LoadActivityInterface) loader.getController()).loadActivity(activity);
			((LoadProjectInterface) loader.getController()).loadProject(selectedProject);
			HBox.setHgrow(ap, Priority.ALWAYS);
			hboxContent.getChildren().add(ap);
			VBox.setVgrow(ap, Priority.ALWAYS);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		HBox.setHgrow(hboxContent, Priority.ALWAYS);
	}

	protected void saveProject(ProjectVO project) {
		if (project.getId() != null)
			selectedProject = projectBO.update(project);
		else
			selectedProject = projectBO.save(project);
//		setSelectedProject(selectedProject, "view");
	}

	public void delete(ProjectVO viewingProject) {
		projectBO.delete(viewingProject);

	}

}
