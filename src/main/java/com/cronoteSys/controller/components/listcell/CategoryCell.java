package com.cronoteSys.controller.components.listcell;

import java.io.File;
import java.io.IOException;

import com.cronoteSys.model.bo.CategoryBO;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

public class CategoryCell extends ListCell<CategoryVO> {
	BooleanProperty isEditing = new SimpleBooleanProperty(false);
	@FXML
	private JFXTextField txtCategoryName;
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
				loader.setLocation(new File(getClass().getResource("/fxml/Templates/cell/categoryCell.fxml").getPath())
						.toURI().toURL());
				loader.setController(this);
				loader.load();
				txtCategoryName.getValidators().add(new RequiredFieldValidator("CAMPO OBRIGATÓRIO!"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			btnEditSave.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (isEditing.get()) {
						if (txtCategoryName.validate()) {
							item.setDescription(txtCategoryName.getText());
							lblCategoryName.setVisible(isEditing.get());
							txtCategoryName.setVisible(!isEditing.get());
							isEditing.set(!isEditing.get());
							category = new CategoryBO().save(item);
						} else {
							return;
						}
					} else {
						lblCategoryName.setVisible(isEditing.get());
						txtCategoryName.setVisible(!isEditing.get());
						isEditing.set(!isEditing.get());
					}
					lblCategoryName.setText(item.getDescription());
					txtCategoryName.setText(item.getDescription());
					loadIcon();
				}
			});
			btnDelete.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					new CategoryDAO().delete(item.getId());
					getListView().getItems().remove(item);
				}
			});
			lblCategoryName.setText(item.getDescription());
			txtCategoryName.setText(item.getDescription());
			category = item;

			setGraphic(cell);
			getStyleClass().addAll("themed-list-cell");

		} else {
			setText(null);
			setGraphic(null);
		}
		setStyle("-fx-background-color:transparent;");
	}

	private void loadIcon() {
		GlyphIcon<?> icon = null;
		if (isEditing.get()) {
			icon = new FontAwesomeIconView(FontAwesomeIcon.SAVE);

		} else {
			icon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
		}
		icon.getStyleClass().add("letters_box_icons");
		icon.setSize("2em");
		btnEditSave.setGraphic(icon);
	}

}