package com.cronoteSys.controller.components.dialogs;

import java.net.URL;
import java.util.ResourceBundle;

import com.cronoteSys.controller.components.cellfactory.CategoryCellFactory;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.SessionUtil;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class DialogCategoryManagerController implements Initializable {

	@FXML
	private JFXTextField txtSearch;
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
	private UserVO loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		lstCategories = FXCollections.observableList(catDao.getList(loggedUser.getIdUser()));
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
				} else {
					lstCategories = FXCollections.observableList(catDao.getList(loggedUser.getIdUser()));
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


}
