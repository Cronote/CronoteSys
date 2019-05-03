package com.cronoteSys.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.cronoteSys.model.bo.CategoryBO;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.SessionUtil;
import com.google.inject.Inject;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DialogCategoryManagerController implements Initializable {

	@FXML
	private TextField txtSearch;
	@FXML
	private Button btnSearch;
	@FXML
	private ListView<CategoryVO> categoryList;
	@FXML
	private Button btnConfirm;
	@FXML
	private Button btnCancel;
	@Inject
	private CategoryDAO catDao;
	private ObservableList<CategoryVO> lstCategories = FXCollections.emptyObservableList();
	private UserVO loggedUser = (UserVO) SessionUtil.getSESSION().get("loggedUser");

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		lstCategories = FXCollections.observableList(catDao.getList(loggedUser));
		categoryList.setItems(lstCategories);
		categoryList.setCellFactory(new CategoryCellFactory());
		btnConfirm.setDisable(true);
		btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Stage) btnCancel.getScene().getWindow()).close();
			}
		});
		btnCancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Stage) btnCancel.getScene().getWindow()).close();
				categoryList.getSelectionModel().clearSelection();
			}
		});
		btnSearch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String search = txtSearch.getText().trim();
				if (!search.isEmpty()) {
					lstCategories = FXCollections.observableList(catDao.listByDescriptionAndUser(search, loggedUser));
				}else {
					lstCategories = FXCollections.observableList(catDao.getList(loggedUser));
				}
				categoryList.setItems(lstCategories);
			}
		});

		categoryList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CategoryVO>() {
			@Override
			public void changed(ObservableValue<? extends CategoryVO> observable, CategoryVO oldValue,
					CategoryVO newValue) {
				if (newValue != null)
					btnConfirm.setDisable(false);
				else
					btnConfirm.setDisable(true);
			}
		});
	}

	public ListView<CategoryVO> getCategoryList() {
		return categoryList;
	}

	private class CategoryCellFactory implements Callback<ListView<CategoryVO>, ListCell<CategoryVO>> {
		@Override
		public ListCell<CategoryVO> call(ListView<CategoryVO> listview) {
			return new CategoryCell();
		}
	}

	private class CategoryCell extends ListCell<CategoryVO> {
		BooleanProperty isEditing = new SimpleBooleanProperty(false);
		@FXML
		private TextField txtCategoryName;
		@FXML
		private Label lblCategoryName;
		@FXML
		private Button btnEditSave;
		@FXML
		private Button btnDelete;
		@FXML
		private AnchorPane cell;
		private CategoryVO category;

		@Override
		public void updateSelected(boolean selected) {
			super.updateSelected(selected);
			btnEditSave.setVisible(selected);
			btnDelete.setVisible(selected && new CategoryBO().canBeDeleted(category));
			if (isEditing.get() && !selected) {
				txtCategoryName.setVisible(false);
				lblCategoryName.setVisible(true);
				isEditing.set(false);
				loadIcon();
			}

		}

		@Override
		public void updateItem(CategoryVO item, boolean empty) {
			super.updateItem(item, empty);
			if (item != null || !empty) {
				FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
				try {
					loader.setLocation(
							new File(getClass().getResource("/fxml/Templates/cell/categoryCell.fxml").getPath()).toURI()
									.toURL());
					loader.setController(this);
					loader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
				btnEditSave.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if (isEditing.get()) {
							item.setDescription(txtCategoryName.getText());
							lblCategoryName.setVisible(isEditing.get());
							txtCategoryName.setVisible(!isEditing.get());
							isEditing.set(!isEditing.get());
						} else {
							lblCategoryName.setVisible(isEditing.get());
							txtCategoryName.setVisible(!isEditing.get());
							isEditing.set(!isEditing.get());
						}
						lblCategoryName.setText(item.getDescription());
						txtCategoryName.setText(item.getDescription());
						new CategoryDAO().saveOrUpdate(item);
						loadIcon();
					}
				});
				btnDelete.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						new CategoryDAO().delete(item.getId());
						categoryList.getItems().remove(item);
					}
				});
				lblCategoryName.setText(item.getDescription());
				txtCategoryName.setText(item.getDescription());
				category = item;
				setGraphic(cell);
			} else {
				setText(null);
				setGraphic(null);
			}
		}

		private void loadIcon() {
			GlyphIcon<?> icon = null;
			if (isEditing.get()) {
				icon = new FontAwesomeIconView(FontAwesomeIcon.SAVE);
			} else {
				icon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
			}
			icon.setSize("2em");
			btnEditSave.setGraphic(icon);
		}
	}
}
