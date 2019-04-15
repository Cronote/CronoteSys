package com.cronoteSys.controller;

import java.awt.event.ActionListener;
import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Timer;

import com.cronoteSys.converter.CategoryConverter;
import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.CategoryBO;
import com.cronoteSys.model.dao.CategoryDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.CategoryVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.ScreenUtil;
import com.sun.mail.imap.protocol.Status;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Skin;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ActivityDetailsController extends ShowEditViewActivityObservable implements Initializable {
	// Edição
	@FXML
	private TextField txtTitle;
	@FXML
	private AnchorPane detailsRoot;
	@FXML
	private ComboBox<CategoryVO> cboCategory;
	@FXML
	private TextField txtCategory;
	@FXML
	private TextArea txtDescription;
	@FXML
	private ToggleGroup tggPriority;
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
	// Visualização
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
	private Label lblDescriptionLimit;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnDelete;

	private String mode;
	HashMap<String, Object> hmp = new HashMap<String, Object>();
	private ActivityVO activity;
	private UserVO user;
	Timer timer = new Timer(60000, new ActionListener() {

		public void actionPerformed(java.awt.event.ActionEvent e) {
			Platform.runLater(new Runnable() {
				public void run() {
					ActivityBO actBo = new ActivityBO();
					activity = actBo.updateRealTime(activity);
					loadActivity();
				}
			});

		}
	});

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
		initForm();
		initEvents();
		timer.setInitialDelay(0);
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
		} else {
			loadActivity();
			if (activity.getStats().equals(StatusEnum.BROKEN_IN_PROGRESS)
					|| activity.getStats().equals(StatusEnum.NORMAL_IN_PROGRESS))
				timer.start();
		}
	}

	private void loadActivity() {
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
		double progress = realtime / estimatedTime;
		progress = progress > 1 ? 1 : progress;
		lblStatus.setText(activity.getStats().getDescription().toUpperCase());
		pgiProgress.setProgress(progress);
		lblLastModified.setText(activity.getLastModification()
				.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm", new Locale("pt", "BR"))));
	}

	private void blockEdition() {
		txtTitle.setDisable(true);
		cboCategory.setDisable(true);
		spnEstimatedTimeHour.setDisable(true);
		spnEstimatedTimeMinute.setDisable(true);
		for (int i = 0; i < tggPriority.getToggles().size(); i++) {
			((ToggleButton) tggPriority.getToggles().get(i)).setDisable(true);
		}
	}

	private void defaultData() {
		List<CategoryVO> lstCategory = new CategoryDAO().getList(user);
		ObservableList<CategoryVO> obsLstCategory = FXCollections.observableList(lstCategory);
		cboCategory.setConverter(new CategoryConverter(user));
		cboCategory.setItems(obsLstCategory);
		spnEstimatedTimeHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 0, 1));
		spnEstimatedTimeMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 1, 1));

		for (int i = 0; i < tggPriority.getToggles().size(); i++) {
			tggPriority.getToggles().get(i).setUserData(i);
		}
	}

	private void initEvents() {
		if (spnEstimatedTimeMinute != null) {
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
		}
		if (spnEstimatedTimeHour != null && spnEstimatedTimeMinute != null) {
			spnEstimatedTimeHour.valueProperty().addListener(new ChangeListener<Integer>() {

				@Override
				public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
					if (newValue != null && newValue == 0 && spnEstimatedTimeMinute.getValue() == 0) {
						spnEstimatedTimeMinute.increment(1);
					}

				}
			});
		}
		if (btnAddCategory != null) {
			btnAddCategory.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					switchCategoryMode();
				}
			});
		}
		if (btnConfirmAdd != null) {
			btnConfirmAdd.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					CategoryVO cat = new CategoryVO();
					CategoryBO catBO = new CategoryBO();
					if (txtCategory.getText().trim().length() > 0) {
						cat.setDescription(txtCategory.getText());
						cat.setUserVO(user);
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

		if (txtDescription != null) {
			txtDescription.textProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					final int LIMIT = 255;
					int i = LIMIT - newValue.length();
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
		}

		if (pgiProgress != null) {
			pgiProgress.skinProperty().addListener(new ChangeListener<Skin>() {

				@Override
				public void changed(ObservableValue<? extends Skin> observable, Skin oldValue, Skin newValue) {
					pgiProgress.lookup(".progress")
							.setStyle("-fx-background-color:" + activity.getStats().getHexColor());
					Text t = (Text) pgiProgress.lookup(".percentage");
					String progressStr = String.format("%.2f", (pgiProgress.getProgress() * 100));
					t.setText(progressStr + "%");
				}
			});
			pgiProgress.progressProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					pgiProgress.lookup(".progress")
							.setStyle("-fx-background-color:" + activity.getStats().getHexColor());
					Text t = (Text) pgiProgress.lookup(".percentage");
					String progressStr = String.format("%.2f", (pgiProgress.getProgress() * 100));
					t.setText(progressStr + "%");
				}
			});
		}

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

	private void switchCategoryMode() {
		btnConfirmAdd.setVisible(btnAddCategory.isVisible());
		btnCancelAdd.setVisible(btnAddCategory.isVisible());
		txtCategory.setVisible(btnAddCategory.isVisible());
		cboCategory.setVisible(!btnAddCategory.isVisible());
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
}