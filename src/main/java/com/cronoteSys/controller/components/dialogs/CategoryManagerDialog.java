package com.cronoteSys.controller.components.dialogs;

import java.io.File;
import java.io.IOException;

import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CategoryManagerDialog {

	public CategoryManagerDialog(ObservableList<CategoryVO> items) {
		lstCategories = items;
	}

	public void showCategoryManagerDialog() {
		try {
			FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
			loader.setLocation(
					new File(getClass().getResource("/fxml/Templates/dialogs/CategoryManager.fxml").getPath()).toURI()
							.toURL());
			Parent root = loader.load();
			DialogCategoryManagerController controller = loader.getController();
			JFXAlert<CategoryVO> alert = new JFXAlert<CategoryVO>();
			alert.setContent(root);
			alert.initStyle(StageStyle.UNDECORATED);
			alert.showAndWait();
			lstCategories = controller.getCategoryList().getItems();
			selectedCategory = controller.getCategoryList().getSelectionModel().getSelectedItem();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ObservableList<CategoryVO> lstCategories;

	private CategoryVO selectedCategory;

	public ObservableList<CategoryVO> getLstCategories() {
		return lstCategories;
	}

	public CategoryVO getSelectedCategory() {
		return selectedCategory;
	}
}
