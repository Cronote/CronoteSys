package com.cronoteSys.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.cronoteSys.converter.CategoryConverter;
import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.model.vo.UserVO;

import javafx.fxml.Initializable;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.util.StringConverter;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class ActivityDetailsController implements Initializable {

	private ActivityVO activity;
	@FXML
	TextField txtTitle;
	@FXML
	ComboBox<CategoryVO> cboCategory;
	@FXML
	TextArea txtDescription;
	@FXML
	ToggleGroup tggPriority;
	@FXML
	Spinner spnEstimatedTimeNumber;
	@FXML
	ComboBox<String> cboEstimatedTimeUnity;

	@FXML
	Button btnSave;

	public ActivityDetailsController(UserVO user) {
		this.activity = new ActivityVO();
		activity.set_userVO(user);
	}

	public ActivityDetailsController(ActivityVO act) {
		this.activity = act;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<CategoryVO> lstCategory = new CategoryDAO().getList();
		ObservableList<CategoryVO> obsLstCategory = FXCollections.observableList(lstCategory);
		cboCategory.setItems(FXCollections.observableList(obsLstCategory));
		cboCategory.setConverter(new CategoryConverter());
		ObservableList<String> obsLstEstimatedTimeUnity = FXCollections.observableArrayList("Horas", "Minutos");
		cboEstimatedTimeUnity.setItems(obsLstEstimatedTimeUnity);
		for (int i = 0; i < tggPriority.getToggles().size(); i++) {
			tggPriority.getToggles().get(i).setUserData(i);
		}
		spnEstimatedTimeNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 6000, 1, 1));
		cboEstimatedTimeUnity.getSelectionModel().clearAndSelect(1);

		cboEstimatedTimeUnity.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				cboEstimatedTimeUnitySelectionChanged(event);

			}
		});
		btnSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				btnSaveClicked(event);
			}
		});
	}

	private void cboEstimatedTimeUnitySelectionChanged(ActionEvent event) {
		String value = cboEstimatedTimeUnity.getValue();
		if (value.equalsIgnoreCase("Minutos")) {
			spnEstimatedTimeNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 6000, 1, 1));
		} else {
			spnEstimatedTimeNumber.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 100, 0.5, 0.5));
		}
	}

	private void btnSaveClicked(ActionEvent event) {
		activity.set_title(txtTitle.getText());
		activity.set_categoryVO(cboCategory.getValue());
		// TODO: activity.set_Description (tem q criar esse atributo) e depois setar
		// aqui
		activity.set_estimated_Time(spnEstimatedTimeNumber.getValue().toString() + cboEstimatedTimeUnity.getValue());
		activity.set_priority(Integer.parseInt(tggPriority.getSelectedToggle().getUserData().toString()));
		System.out.println(activity.get_priority());
		activity.set_stats(StatusEnum.NOT_STARTED);
		activity.set_last_Modification(LocalDate.now());
		new ActivityBO().save(activity);
	}

}
