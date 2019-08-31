package com.cronoteSys.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.cronoteSys.controller.components.cellfactory.ProjectCellFactory;
import com.cronoteSys.model.bo.ProjectBO;
import com.cronoteSys.model.vo.ProjectVO;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ProjectListController implements Initializable {
	private ListProperty<ProjectVO> projectLst = new SimpleListProperty<ProjectVO>();

	private UserVO loggedUser;

	@FXML
	private Button btnAddProject;
	@FXML
	private ListView<ProjectVO> cardsList;
	@FXML
	private AnchorPane title;
	@FXML
	private TextField txtSearch;
	@FXML
	private Button btnSearch;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initEvents();
		initListeners();
		loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");
		ProjectBO projectBO = new ProjectBO();
		List<ProjectVO> lst = projectBO.listAll(loggedUser);
		projectLst.set(FXCollections.observableArrayList(lst));
		cardsList.itemsProperty().bind(projectLst);
		cardsList.setCellFactory(new ProjectCellFactory());
		GlyphIcon<?> icon = null;
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

	private void initListeners() {
		cardsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProjectVO>() {
			@Override
			public void changed(ObservableValue<? extends ProjectVO> observable, ProjectVO oldValue,
					ProjectVO newValue) {
				notifyAllProjectSelectedListeners(newValue);
			}
		});
	}

	private void initEvents() {
		btnAddProject.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				cardsList.getSelectionModel().clearSelection();
				notifyAllBtnProjectClickedListeners();
			}
		});

		ProjectBO.addOnProjectAddedIListener(proj -> {
			System.out.println("ProjectListController.projectadded()");
			projectLst.add(0, proj);
			cardsList.refresh();
			cardsList.getSelectionModel().select(proj);
		});
		ProjectBO.addOnProjectDeletedListener(proj -> {
			projectLst.remove(proj);
			cardsList.refresh();
		});
	}

	private static ArrayList<BtnProjectClickedI> projectAddedListeners = new ArrayList<BtnProjectClickedI>();

	public interface BtnProjectClickedI {
		void onBtnProjectClicked();
	}

	public static void addOnBtnProjectClickedListener(BtnProjectClickedI newListener) {
		projectAddedListeners.add(newListener);
	}

	private void notifyAllBtnProjectClickedListeners() {
		for (BtnProjectClickedI l : projectAddedListeners) {
			l.onBtnProjectClicked();
		}
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
