package com.cronoteSys.controller;

import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.controlsfx.control.Rating;

import com.cronoteSys.controller.components.dialogs.CategoryManagerDialog;
import com.cronoteSys.controller.components.dialogs.DialogDependencyManager;
import com.cronoteSys.converter.CategoryConverter;
import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.CategoryBO;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.observer.ShowEditViewActivityObservableI;
import com.cronoteSys.observer.ShowEditViewActivityObserverI;
import com.cronoteSys.util.ActivityMonitor;
import com.cronoteSys.util.ActivityMonitor.OnMonitorTick;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Skin;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ActivityDetailsInsertingController implements Initializable, ShowEditViewActivityObservableI {
	@FXML
	private Label lblPaneTitle;
	// Edição
	@FXML
	private JFXTextField txtTitle;
	@FXML
	private AnchorPane detailsRoot;
	@FXML
	private JFXComboBox<CategoryVO> cboCategory;
	@FXML
	private TextField txtCategory;
	@FXML
	private JFXTextArea txtDescription;
	@FXML
	private Label lblDescriptionLimit;
	@FXML
	private Spinner<Integer> spnEstimatedTimeHour;
	@FXML
	private Spinner<Integer> spnEstimatedTimeMinute;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnAddCategory;
	@FXML
	private Button btnConfirmAdd;
	@FXML
	private Button btnCancelAdd;
	@FXML
	private Button btnManageCategory;
	@FXML
	private Rating ratePriority;
	@FXML
	private JFXButton btnDependencies;

	private String mode;
	HashMap<String, Object> hmp = new HashMap<String, Object>();
	private ActivityVO activity;
	private UserVO loggedUser;
	ObservableList<CategoryVO> obsLstCategory = FXCollections.emptyObservableList();

	private void initActivity() {
		activity = new ActivityVO();
		loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");
		activity.setUserVO(loggedUser);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initActivity();
		initForm();
		initEvents();
	}

	private void initForm() {
		defaultData();

	}

	public void loadActivity(ActivityVO act) {
		activity = act;
		if (activity.getId() != null) {
			txtTitle.setText(activity.getTitle());
			cboCategory.getSelectionModel().select(activity.getCategoryVO());
			txtDescription.setText(activity.getDescription());
			ratePriority.setRating(activity.getPriority());
			if (!activity.getStats().equals(StatusEnum.NOT_STARTED)) {
				blockEdition();
			}
			long horas = activity.getEstimatedTime().toHours();
			Duration minutos = activity.getEstimatedTime().minus(horas, ChronoUnit.HOURS);
			String hora = String.format("%02d", horas);
			String minuto = String.format("%02d", minutos.toMinutes());
			spnEstimatedTimeHour.getValueFactory().setValue(Integer.parseInt(hora));
			spnEstimatedTimeMinute.getValueFactory().setValue(Integer.parseInt(minuto));
			spnEstimatedTimeHour.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
			spnEstimatedTimeMinute.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
		}
	}

	public void setProject(ProjectVO proj) {
		activity.setProjectVO(proj);
		btnDependencies.setVisible(proj != null);
	}

	private void blockEdition() {
		txtTitle.setDisable(true);
		cboCategory.setDisable(true);
		spnEstimatedTimeHour.setDisable(true);
		spnEstimatedTimeMinute.setDisable(true);
		btnManageCategory.setDisable(true);
		btnAddCategory.setDisable(true);
		ratePriority.setDisable(true);
	}

	private void defaultData() {

		btnDependencies.setVisible(false);
		obsLstCategory = FXCollections.observableList(new CategoryBO().listByUser(loggedUser));
		cboCategory.setConverter(new CategoryConverter(loggedUser));
		cboCategory.setItems(obsLstCategory);
		spnEstimatedTimeHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 0, 1));
		spnEstimatedTimeMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 1, 1));

	}

	private void initEvents() {
		spnEstimatedTimeMinute.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				if (newValue == 60) {
					newValue = 0;
					spnEstimatedTimeMinute.getValueFactory().setValue(newValue);
					spnEstimatedTimeHour.increment(1);
				}
				if (newValue == 0 && spnEstimatedTimeHour.getValue() == 0) {
					newValue = 1;
					spnEstimatedTimeMinute.getValueFactory().setValue(newValue);
				}

			}
		});
		spnEstimatedTimeHour.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				if (newValue != null && newValue == 0 && spnEstimatedTimeMinute.getValue() == 0) {
					spnEstimatedTimeMinute.increment(1);
				}

			}
		});
		btnAddCategory.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				switchCategoryMode();
			}
		});
		btnManageCategory.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				CategoryManagerDialog categoryManagerDialog = new CategoryManagerDialog(cboCategory.getItems());

				categoryManagerDialog.showCategoryManagerDialog();
				CategoryVO selectedCategory = categoryManagerDialog.getSelectedCategory();
				obsLstCategory = FXCollections.observableList(new CategoryBO().listByUser(loggedUser));
				cboCategory.setItems(obsLstCategory);
				cboCategory.getSelectionModel().select(selectedCategory);

			}
		});
		btnConfirmAdd.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				CategoryVO cat = new CategoryVO();
				CategoryBO catBO = new CategoryBO();
				if (txtCategory.getText().trim().length() > 0) {
					cat.setDescription(txtCategory.getText());
					cat.setUserVO(loggedUser);
					cat = catBO.save(cat);
					if (cat.getId() != null) {
						cboCategory.getItems().add(cat);
						cboCategory.getSelectionModel().select(cat);
						switchCategoryMode();
						switchCategoryErrorLabel(false);
					}
				} else {
					switchCategoryErrorLabel(true);
				}
			}
		});
		btnCancelAdd.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				switchCategoryMode();
				switchCategoryErrorLabel(false);
			}
		});
		btnSave.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				btnSaveClicked(event);
			}
		});
		txtDescription.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				final int LIMIT = 255;

				int i = LIMIT - (newValue != null ? newValue.length() : 0);
				if (i > 5) {
					lblDescriptionLimit.setStyle("-fx-text-fill:black;");
				} else {
					lblDescriptionLimit.setStyle("-fx-text-fill:red;");

				}
				if (i <= 0) {
					String s = txtDescription.getText().substring(0, LIMIT);
					txtDescription.setText(s);
					lblDescriptionLimit.setText(String.valueOf(0));
					return;
				}
				lblDescriptionLimit.setText(String.valueOf(i));
			}
		});
		txtDescription.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				final int LIMIT = 255;
				int i = LIMIT - (((TextArea) event.getSource()).getText().length() + event.getCharacter().length());
				if (i < 0) {
					if (event.getCode() != KeyCode.BACK_SPACE || event.isControlDown())
						event.consume();
				}
			}
		});

		btnDependencies.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				activity.setTitle(txtTitle.getText());
				DialogDependencyManager categoryManagerDialog = new DialogDependencyManager(activity);

				categoryManagerDialog.showDependencyManagerDialog();
			}
		});

	}

	private void btnSaveClicked(ActionEvent event) {
		if (new ScreenUtil().isFilledFields((Stage) btnSave.getScene().getWindow(), detailsRoot, false)) {
			activity.setTitle(txtTitle.getText());
			activity.setCategoryVO(cboCategory.getValue());
			activity.setDescription(txtDescription.getText());
			Duration d = Duration.ZERO;
			d = d.plusMinutes(spnEstimatedTimeMinute.getValue());
			d = d.plusHours(spnEstimatedTimeHour.getValue());
			activity.setEstimatedTime(d);
			activity.setPriority((int) ratePriority.getRating());
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

	private void switchCategoryMode() {
		btnConfirmAdd.setVisible(btnAddCategory.isVisible());
		btnCancelAdd.setVisible(btnAddCategory.isVisible());
		txtCategory.setVisible(btnAddCategory.isVisible());
		cboCategory.setVisible(!btnAddCategory.isVisible());
		btnManageCategory.setVisible(!btnAddCategory.isVisible());
		btnAddCategory.setVisible(!btnAddCategory.isVisible());
	}

	private void switchCategoryErrorLabel(boolean show) {
		Label lbl = (Label) txtCategory.getScene().lookup("#lblCategoryValidation");
		if (show) {
			lbl.getStyleClass().removeAll("hide");
			txtCategory.getStyleClass().add("error");
		} else {
			txtCategory.getStyleClass().removeAll("error");
			lbl.getStyleClass().add("hide");
		}
	}

	@Override
	public void notifyAllListeners(HashMap<String, Object> hmp) {
		for (ShowEditViewActivityObserverI l : listeners) {
			l.showEditViewActivity(hmp);
		}

	}
}