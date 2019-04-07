package com.cronoteSys.controller;

import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.cronoteSys.converter.CategoryConverter;
import com.cronoteSys.converter.UnityTimeEnumConverter;
import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.StatusEnum;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ActivityDetailsController extends ShowEditViewActivityObservable implements Initializable {

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
	@FXML
	private Button btnSave;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnDelete;
	private String mode;
	HashMap<String, Object> hmp = new HashMap<String, Object>();
	private ActivityVO activity;
	private UserVO user;

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
			defaultData();
			if (activity == null) {
				activity = new ActivityVO();
				activity.setUserVO(user);
			} else {
				txtTitle.setText(activity.getTitle());
				cboCategory.getSelectionModel().select(activity.getCategoryVO());
				txtDescription.setText(activity.getDescription());
				for (int i = 0; i < tggPriority.getToggles().size(); i++) {
					boolean isPriorityIndex = i == activity.getPriority();
					((ToggleButton) tggPriority.getToggles().get(i)).setSelected(isPriorityIndex);
				}
				cboEstimatedTimeUnity.getSelectionModel().select(activity.getEstimatedTimeUnit());
				if (activity.getEstimatedTimeUnit().equals(UnityTimeEnum.HOURS)) {
					spnEstimatedTimeNumber
							.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 100, 0.5, 0.5));
					spnEstimatedTimeNumber.getValueFactory().setValue((double) activity.getEstimatedTime().toHours());

				} else {
					spnEstimatedTimeNumber
							.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 6000, 1, 1));
					spnEstimatedTimeNumber.getValueFactory().setValue((double) activity.getEstimatedTime().toMinutes());
				}
				if (!activity.getStats().equals(StatusEnum.NOT_STARTED)) {
					blockEdition();
				}
			}
		} else {
			lblTitle.setText(activity.getTitle());
			lblCategory.setText(activity.getCategoryVO().getDescription());
			lblDescription.setText(activity.getDescription());
			for (int i = 0; i < tggPriority.getToggles().size(); i++) {
				boolean isPriorityIndex = i == activity.getPriority();
				((ToggleButton) tggPriority.getToggles().get(i)).setSelected(isPriorityIndex);
				((ToggleButton) tggPriority.getToggles().get(i)).setDisable(true);
			}

			lblEstimatedTime.setText(activity.getEstimatedTimeAsString());
			lblRealTime.setText(activity.getRealtimeAsString());
			double estimatedTime = activity.getEstimatedTime().toMillis();
			double realtime = activity.getRealtime().toMillis();
			lblStatus.setText(activity.getStats().getDescription().toUpperCase());
			pgiProgress.setProgress(realtime / estimatedTime);
			lblLastModified.setText(activity.getLastModification()
					.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm", new Locale("pt", "BR"))));
		}
	}

	private void blockEdition() {
		txtTitle.setDisable(true);
		cboCategory.setDisable(true);
		cboEstimatedTimeUnity.setDisable(true);
		spnEstimatedTimeNumber.setDisable(true);
		for (int i = 0; i < tggPriority.getToggles().size(); i++) {
			((ToggleButton) tggPriority.getToggles().get(i)).setDisable(true);
		}
	}

	private void defaultData() {
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
		if (btnSave != null) {
			btnSave.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					btnSaveClicked(event);
				}
			});
		}
		if (btnEdit != null) {
			btnEdit.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					hmp.put("activity", activity);
					hmp.put("action", "edit");
					notifyAllListeners(hmp);
				}
			});
		}
		if (btnDelete != null) {
			btnDelete.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					new ActivityBO().delete(activity);
				}
			});
		}
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
		if (new ScreenUtil().isFilledFields((Stage) btnSave.getScene().getWindow(), detailsRoot,false)) {
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
			if (activity.getId() == null) {
				activity = new ActivityBO().save(activity);

			} else {
				activity = new ActivityBO().update(activity);
			}
			if (activity != null) {
				hmp.put("activity", activity);
				hmp.put("action", "view");
				notifyAllListeners(hmp);
			}
		}
	}
}