package com.cronoteSys.controller.components.dialogs;

import java.io.File;
import java.io.IOException;

import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXAlert;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.StageStyle;

public class CategoryManagerDialog {

	private ProjectVO project;

	public CategoryManagerDialog(ProjectVO project) {
		this.project = project;
	}

	public void showCategoryManagerDialog() {
		try {
			FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
			loader.setLocation(
					new File(getClass().getResource("/fxml/Templates/dialogs/CategoryManager.fxml").getPath()).toURI()
							.toURL());
			Parent root = loader.load();
			DialogCategoryManagerController controller = loader.getController();
			controller.setProject(project);
			JFXAlert<CategoryVO> alert = new JFXAlert<CategoryVO>();
			alert.setContent(root);
			alert.initStyle(StageStyle.UNDECORATED);
			alert.showAndWait();
			selectedCategory = controller.getCategoryList().getSelectionModel().getSelectedItem();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private CategoryVO selectedCategory;

	public CategoryVO getSelectedCategory() {
		return selectedCategory;
	}
}
