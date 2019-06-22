package com.cronoteSys.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.cronoteSys.model.bo.ProjectBO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.observer.ShowEditViewActivityObservableI;
import com.cronoteSys.observer.ShowEditViewActivityObserverI;
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
		while (tabPane.getTabs().size() > 0)
			tabPane.getTabs().remove(0);
		while (hboxContent.getChildren().size() > 0)
			hboxContent.getChildren().remove(0);
		try {
			if (mode.equals("cadastro")) {
				System.out.println(mode);
				firstInfoLoader = ScreenUtil.loadTemplate("ProjectFirstInfo");
				firstInfoController = new ProjectFirstInfoController(this);
				firstInfoLoader.setController(firstInfoController);
				firstInfoPane = firstInfoLoader.load();
				firstInfoController.setProject(selectedProject);
			} else {
				System.out.println(mode);
				firstInfoLoader = ScreenUtil.loadTemplate("ProjectFirstInfoView");
				firstInfoViewController = new ProjectFirstInfoViewController(this);
				firstInfoLoader.setController(firstInfoViewController);
				firstInfoPane = firstInfoLoader.load();
				firstInfoViewController.setProject(selectedProject);
			}
			tabPane.getTabs().add(0, new Tab("INFORMAÇÕES", firstInfoPane));

			initActivities();

		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initActivities() {
		if (selectedProject != null) {
			try {
				FXMLLoader actlivityListLoader = ScreenUtil.loadTemplate("ActivityList");
				hboxContent.getChildren().add(actlivityListLoader.load());
				activityListController = ((ActivityListController) actlivityListLoader.getController());
				ShowEditViewActivityObservableI.addShowEditViewActivityListener(observer);
				tabPane.getTabs().add(tbActivities);
				activityListController.listByProject(selectedProject);
				try {
					int total = activityListController.getActivityTotal();
					firstInfoViewController.setRealDoneTodo(activityListController.getDoneActivitiesTotal()+"/"+total);
				}catch (Exception e) {
					// TODO: handle exception
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void saveProject(ProjectVO project) {
		if (project.getId() != null)
			selectedProject = projectBO.update(project);
		else
			selectedProject = projectBO.save(project);
		setSelectedProject(selectedProject, "view");
	}

	ShowEditViewActivityObserverI observer = new ShowEditViewActivityObserverI() {
		@Override
		public void showEditViewActivity(HashMap<String, Object> hmap) {
			removeEditViewPane();
			try {
				AnchorPane ap = null;
				FXMLLoader detailsFxml = ScreenUtil.loadTemplate("ActivityDetailsInserting");
				String action = (String) hmap.get("action");
				ActivityVO activity = (ActivityVO) hmap.get("activity");
				if (action.equalsIgnoreCase("cadastro")) {
					ap = detailsFxml.load();
					if (activity != null)
						((ActivityDetailsInsertingController) detailsFxml.getController()).loadActivity(activity);
					if (selectedProject != null)
						((ActivityDetailsInsertingController) detailsFxml.getController()).setProject(selectedProject);
				} else {
					detailsFxml = ScreenUtil.loadTemplate("ActivityDetailsView");
					ap = (AnchorPane) detailsFxml.load();
					((ActivityDetailsViewController) detailsFxml.getController()).loadActivity(activity);
				}

				ap.setMaxWidth(Double.POSITIVE_INFINITY);
				ap.setMaxHeight(Double.POSITIVE_INFINITY);
				hboxContent.getChildren().add(ap);
				HBox.setHgrow(ap, Priority.ALWAYS);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void removeEditViewPane() {
			while (hboxContent.getChildren().size() > 1) {
				hboxContent.getChildren().remove(1);
			}
		}
	};

	public ShowEditViewActivityObserverI getObserver() {
		return observer;
	}

	public void delete(ProjectVO viewingProject) {
		projectBO.delete(viewingProject);

	}

}
