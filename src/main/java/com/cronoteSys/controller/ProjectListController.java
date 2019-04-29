package com.cronoteSys.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.cronoteSys.controller.ProjectCell.ProjectSelectedI;
import com.cronoteSys.model.bo.ProjectBO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.SessionUtil;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class ProjectListController implements Initializable {

	@FXML
	private Button btnAddProject;
	@FXML
	private ListView<ProjectVO> cardsList;
	private List<ProjectVO> projectLst = new ArrayList<ProjectVO>();

	private UserVO loggedUser;
	@FXML
	TitledPane title;
	@FXML
	TextField txtSearch;
	@FXML
	Button btnSearch;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initEvents();
		initObservers();
		loggedUser = (UserVO) SessionUtil.getSESSION().get("loggedUser");
		ProjectBO projectBO = new ProjectBO();
		projectLst = projectBO.listAll(loggedUser);
		cardsList.setItems(FXCollections.observableArrayList(projectLst));
		cardsList.setCellFactory(new ProjectCellFactory());
		GlyphIcon icon = null;
		if (projectLst.size() > 0) {
			icon = new MaterialDesignIconView(MaterialDesignIcon.PLAYLIST_PLUS);
			icon.getStyleClass().addAll("normal-white");
		} else {
			icon = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
			icon.getStyleClass().addAll("outline-white", "rainforce");
		}
		icon.setSize("3em");
		btnAddProject.setGraphic(icon);
	}

	private void initObservers() {
		ProjectCell.addOnProjectSelectedListener(new ProjectSelectedI() {
			
			@Override
			public void onProjectSelect(ProjectVO project) {
				
			}
		});
	}

	private void initEvents() {
		btnAddProject.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				cardsList.getSelectionModel().clearSelection();
				notifyAllProjectAddedListeners();

			}
		});

	}

	private static ArrayList<BtnProjectClickedI> projectAddedListeners = new ArrayList<BtnProjectClickedI>();

	public interface BtnProjectClickedI {
		void onBtnProjectClicked();
	}

	public static void addOnBtnProjectClickedListener(BtnProjectClickedI newListener) {
		projectAddedListeners.add(newListener);
	}

	private void notifyAllProjectAddedListeners() {
		for (BtnProjectClickedI l : projectAddedListeners) {
			l.onBtnProjectClicked();
		}
	}

}

class ProjectCellFactory implements Callback<ListView<ProjectVO>, ListCell<ProjectVO>> {
	@Override
	public ListCell<ProjectVO> call(ListView<ProjectVO> listview) {
		return new ProjectCell();
	}
}

class ProjectCell extends ListCell<ProjectVO> {
	@FXML
	private Label lblTitle;
	@FXML
	private Label lblProgress;
	@FXML
	private ProgressBar pgbProgress;
	@FXML
	private AnchorPane projectCardRoot;
	private ProjectVO project;

	@Override
	public void updateSelected(boolean selected) {
		super.updateSelected(selected);
		if (selected) {
			projectCardRoot.getStyleClass().add("projectCardSelected");
			notifyAllProjectSelectedListeners(project);

		} else
			projectCardRoot.getStyleClass().removeAll("projectCardSelected");

	}


	@Override
	public void updateItem(ProjectVO item, boolean empty) {
		super.updateItem(item, empty);

		if (item != null || !empty) {

			FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
			try {
				loader.setLocation(new File(getClass().getResource("/fxml/Templates/cell/ProjectCard.fxml").getPath())
						.toURI().toURL());
				loader.setController(this);
				loader.load();

			} catch (IOException e) {
				e.printStackTrace();
			}
			lblTitle.setText(item.getTitle());
			setGraphic(projectCardRoot);
			project = item;
		} else {
			setText(null);
			setGraphic(null);
		}
		setStyle("-fx-background-color:transparent;");
	}

	private static ArrayList<ProjectSelectedI> projectSelectedListeners = new ArrayList<ProjectSelectedI>();

	public interface ProjectSelectedI {
		void onProjectSelect(ProjectVO project);
	}

	public static void addOnProjectSelectedListener(ProjectSelectedI newListener) {
		projectSelectedListeners.add(newListener);
	}

	private void notifyAllProjectSelectedListeners(ProjectVO project) {
		for (ProjectSelectedI l : projectSelectedListeners) {
			l.onProjectSelect(project);
		}
	}
}
