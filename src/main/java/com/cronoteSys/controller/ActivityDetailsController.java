package com.cronoteSys.controller;

import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.cronoteSys.converter.CategoryConverter;
import com.cronoteSys.converter.UnityTimeEnumConverter;
import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.UnityTimeEnum;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.ScreenUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ActivityDetailsController implements Initializable {

	private ActivityVO activity;
	private UserVO user;
	@FXML
	private TextField txtTitle;
	@FXML
	private AnchorPane detailsRoot;
	@FXML
	private ComboBox<CategoryVO> cboCategory;
	@FXML
	private TextArea txtDescription;
	@FXML
	private ToggleGroup tggPriority;
	@FXML
	private Spinner<Double> spnEstimatedTimeNumber;
	@FXML
	private ComboBox<UnityTimeEnum> cboEstimatedTimeUnity;
	@FXML
	private Button btnSave;
	@FXML
	private Label lblTitle;
	@FXML
	private Label lblCategory;
	@FXML
	private Label lblDescription;
	@FXML
	private Label lblEstimatedTime;
	@FXML
	private Label lblRealTime;
	@FXML
	private ProgressIndicator pgiProgress;
	@FXML
	private Label lblStatus;
	@FXML
	private Label lblLastModified;

	private String mode;

	public ActivityDetailsController(UserVO user) {
		this.user = user;
		mode = "edit";
	}

	public ActivityDetailsController(ActivityVO act, String mode) {
		this.activity = act;
		this.user = act.getUserVO();
		this.mode = mode;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initEvents();
		initForm();
	}

	private void initForm() {
		if (mode.equals("edit")) {
			List<CategoryVO> lstCategory = new CategoryDAO().getList(user);
			ObservableList<CategoryVO> obsLstCategory = FXCollections.observableList(lstCategory);
			cboCategory.setConverter(new CategoryConverter(user));
			cboCategory.setItems(obsLstCategory);
			ObservableList<UnityTimeEnum> obsLstEstimatedTimeUnity = FXCollections
					.observableArrayList(UnityTimeEnum.values());
			cboEstimatedTimeUnity.setItems(obsLstEstimatedTimeUnity);
			cboEstimatedTimeUnity.setConverter(new UnityTimeEnumConverter());
			for (int i = 0; i < tggPriority.getToggles().size(); i++) {
				tggPriority.getToggles().get(i).setUserData(i);
			}
			if (activity == null) {
				activity = new ActivityVO();
				activity.setUserVO(user);
			} else {
				txtTitle.setText(activity.getTitle());
				cboCategory.getSelectionModel().select(activity.getCategoryVO());

			}
		} else {
			lblTitle.setText(activity.getTitle());
			lblCategory.setText(activity.getCategoryVO().getDescription());
			lblDescription.setText(activity.getDescription());
			lblLastModified.setText(activity.getLastModification().toString());
			if (activity.getEstimatedTimeUnit().equals(UnityTimeEnum.HOURS)) {
				lblEstimatedTime.setText(String.format("%02d " + UnityTimeEnum.HOURS.getDescription(),
						activity.getEstimatedTime().toHours()));
			} else {
				lblEstimatedTime.setText(String.format("%02d " + UnityTimeEnum.MINUTES.getDescription(),
						activity.getEstimatedTime().toMinutes()));
			}
			lblStatus.setText(activity.getStats().getDescription().toUpperCase());
			lblLastModified.setText(activity.getLastModification()
					.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm", new Locale("pt", "BR"))));
		}
	}

	private void initEvents() {
		if (cboEstimatedTimeUnity != null) {
			cboEstimatedTimeUnity.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					cboEstimatedTimeUnitySelectionChanged(event);

				}
			});
		}
		btnSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				btnSaveClicked(event);
			}
		});
	}

	private void cboEstimatedTimeUnitySelectionChanged(ActionEvent event) {
		UnityTimeEnum value = cboEstimatedTimeUnity.getValue();
		if (value == null)
			return;
		if (value.equals(UnityTimeEnum.MINUTES)) {
			spnEstimatedTimeNumber.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 6000, 1, 1));
		} else {
			spnEstimatedTimeNumber.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 100, 0.5, 0.5));
		}
	}

	private void btnSaveClicked(ActionEvent event) {
		activity.setTitle(txtTitle.getText());
		activity.setCategoryVO(cboCategory.getValue());
		activity.setDescription(txtDescription.getText());
		Duration d = Duration.ZERO;
		if (cboEstimatedTimeUnity.getValue().equals(UnityTimeEnum.MINUTES)) {
			d = Duration.ofMinutes(spnEstimatedTimeNumber.getValue().longValue());

		} else {
			d = Duration.ofHours(spnEstimatedTimeNumber.getValue().longValue());
		}
		activity.setEstimatedTime(d);
		activity.setEstimatedTimeUnit(cboEstimatedTimeUnity.getValue());
		activity.setPriority(Integer.parseInt(tggPriority.getSelectedToggle().getUserData().toString()));
		activity = new ActivityBO().save(activity);
		if (activity != null) {
			new ScreenUtil().clearFields((Stage) btnSave.getScene().getWindow(), detailsRoot);
			tggPriority.getToggles().get(2).setSelected(true);

		}
	}

}
