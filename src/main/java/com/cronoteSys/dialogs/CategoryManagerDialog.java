package com.cronoteSys.dialogs;

import java.io.File;
import java.io.IOException;

import com.cronoteSys.controller.DialogCategoryManagerController;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.util.SessionUtil;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CategoryManagerDialog {

	public CategoryManagerDialog(ObservableList<CategoryVO> items) {
		lstCategories = items;
	}

	public void showCategoryManagerDialog() {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
			loader.setLocation(
					new File(getClass().getResource("/fxml/Templates/dialogs/CategoryManager.fxml").getPath()).toURI()
							.toURL());
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);

			DialogCategoryManagerController controller = loader.getController();
			stage.showAndWait();
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
