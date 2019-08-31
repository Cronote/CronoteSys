package com.cronoteSys.controller.components.listcell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.ExecutionTimeBO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.observer.ShowEditViewActivityObservableI;
import com.cronoteSys.observer.ShowEditViewActivityObserverI;
import com.cronoteSys.util.ActivityMonitor;
import com.cronoteSys.util.ActivityMonitor.OnMonitorTick;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.SessionUtil;
import com.jfoenix.controls.JFXProgressBar;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class ActivityCellController extends ListCell<ActivityVO> implements ShowEditViewActivityObservableI {
	private AnchorPane activityCardRoot;
	private Label lblTitle;
	private Label lblCategory;
	private Label lblStatus;
	private Label lblIndex;
	private Label lblProgress;
	private Button btnDelete;
	private Button btnFinalize;
	private Button btnPlayPause;
	private JFXProgressBar pgbProgress;

	private ActivityVO activity;

	private boolean canExecute = true;

	{
		activityCardRoot = new AnchorPane();

		activityCardRoot.setMinWidth(249.0);
		activityCardRoot.getStyleClass().addAll("card", "borders");

		lblTitle = new Label();
		lblTitle.setPrefWidth(153.0);
		lblTitle.setWrapText(true);
		lblTitle.setFont(new Font("System bold", 15));
		lblTitle.setAlignment(Pos.TOP_LEFT);
//		lblTitle.setStyle("-fx-background-color:red");
		AnchorPane.setTopAnchor(lblTitle, 10.0);
		AnchorPane.setLeftAnchor(lblTitle, 14.0);
		AnchorPane.setRightAnchor(lblTitle, 82.0);

		lblCategory = new Label();

		lblStatus = new Label();

		lblIndex = new Label();
		lblProgress = new Label();

		btnDelete = new Button();
		btnFinalize = new Button();
		btnPlayPause = new Button();

		AnchorPane.setTopAnchor(btnPlayPause, 7.0);
		AnchorPane.setRightAnchor(btnPlayPause, 35.0);

		AnchorPane.setTopAnchor(btnDelete, 7.0);
		AnchorPane.setRightAnchor(btnDelete, 2.0);

		AnchorPane.setTopAnchor(btnFinalize, 7.0);
		AnchorPane.setRightAnchor(btnFinalize, 2.0);

		pgbProgress = new JFXProgressBar();

	}

	private void fix() {

		Font infoFont = new Font("System bold", 13);
		Double sumHeights = 10.0;
		FontAwesomeIconView icons[] = { new FontAwesomeIconView(FontAwesomeIcon.TRASH_ALT),
				new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE_ALT) };// 0 - delete, 1 - finalize

		for (FontAwesomeIconView icon : icons) {
			icon.setSize("2em");
			icon.getStyleClass().add("letters_box_icons");
		}

		sumHeights += Double.parseDouble(getHeightOf(lblTitle)[0].toString());

		AnchorPane.setTopAnchor(lblCategory, sumHeights);
		AnchorPane.setLeftAnchor(lblCategory, 14.0);
		sumHeights += Double.parseDouble(getHeightOf(lblCategory)[0].toString());
		AnchorPane.setTopAnchor(lblStatus, sumHeights);
		AnchorPane.setLeftAnchor(lblStatus, 14.0);
		AnchorPane.setTopAnchor(lblIndex, sumHeights);
		AnchorPane.setRightAnchor(lblIndex, 6.0);
		sumHeights += Double.parseDouble(getHeightOf(lblIndex)[0].toString());
		AnchorPane ap = new AnchorPane(pgbProgress, lblProgress);
		{
			AnchorPane.setRightAnchor(lblProgress, 0.0);
			AnchorPane.setLeftAnchor(pgbProgress, 0.0);
			AnchorPane.setRightAnchor(pgbProgress, 63.0);
			AnchorPane.setTopAnchor(pgbProgress, 6.0);
			AnchorPane.setBottomAnchor(pgbProgress, 6.0);
		}
		AnchorPane.setTopAnchor(ap, sumHeights);
		AnchorPane.setLeftAnchor(ap, 12.0);
		AnchorPane.setRightAnchor(ap, 12.0);
		AnchorPane.setBottomAnchor(ap, 3.0);

		activityCardRoot.getChildren().clear();
		activityCardRoot.getChildren().addAll(lblTitle, lblCategory, lblStatus, lblIndex, ap, btnPlayPause, btnDelete,
				btnFinalize);

		for (Node n : activityCardRoot.getChildren()) {
			if (n instanceof Label) {
				((Label) n).setTextFill(Color.WHITE);
				((Label) n).getStyleClass().clear();
				((Label) n).getStyleClass().addAll("info");
				if (n.equals(lblTitle))
					continue;
				((Label) n).setFont(infoFont);
				if (n.equals(lblIndex))
					((Label) n).getStyleClass().add("hide");

			}
			if (n instanceof Button) {
				((Button) n).getStyleClass().addAll("btnTransparent", "hide");
				((Button) n).setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

			}
		}
		lblProgress.setFont(infoFont);
		lblProgress.setTextFill(Color.WHITE);

		btnDelete.setGraphic(icons[0]);
		btnFinalize.setGraphic(icons[1]);
	}

	@Override
	public void updateSelected(boolean selected) {
		super.updateSelected(selected);
		HashMap<String, Object> hmp = new HashMap<String, Object>();
		if (selected) {
			activityCardRoot.getStyleClass().add("cardSelected");
			hmp.put("action", "view");
			hmp.put("activity", activity);
			hmp.put("project", activity.getProjectVO());
			notifyAllListeners(hmp);
		} else
			activityCardRoot.getStyleClass().removeAll("cardSelected");

	}

	@Override
	protected void updateItem(ActivityVO item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null || !empty) {
			initEvents();
			activity = item;
			loadActivity();
			fix();
			setGraphic(activityCardRoot);
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			setAlignment(Pos.CENTER);
		} else {
			setGraphic(null);
			setText(null);
		}
		setStyle("-fx-background-color:transparent");
	}

	public Object[] getHeightOf(Region node) {
		Group root = new Group();
		Scene scene = new Scene(root);

		root.getChildren().add(node);

		root.applyCss();
		root.layout();
		Double height = node.getHeight();
		Object[] result = { height + 5, node };

		return result;

	}

	private void loadActivity() {
		lblTitle.setText(activity.getTitle());
		lblCategory.setText(activity.getCategoryVO().getDescription());
		loadProgressAndRealtime();
		GlyphIcon<?> icon = null;
		String btnText = "";
		if (!StatusEnum.itsFinalized(activity.getStats())) {
			if (activity.getStats() == StatusEnum.NOT_STARTED || StatusEnum.itsPaused(activity.getStats())) {

				icon = new FontAwesomeIconView(FontAwesomeIcon.PLAY_CIRCLE_ALT);
				btnText = "play";
				btnFinalize.getStyleClass().removeAll("show");
				icon.setSize("2em");
			} else if (StatusEnum.inProgress(activity.getStats())) {

				icon = new FontAwesomeIconView(FontAwesomeIcon.PAUSE_CIRCLE_ALT);
				btnText = "pause";
				btnFinalize.getStyleClass().add("show");
				icon.setSize("2em");
			}
			icon.getStyleClass().add("letters_box_icons");
			btnPlayPause.setGraphic(icon);
			btnPlayPause.setText(btnText);
			if (activity.getDependencies().isEmpty())
				canExecute = true;
			else {
				for (ActivityVO dependency : activity.getDependencies()) {
					if (!StatusEnum.itsFinalized(dependency.getStats())) {
						canExecute = false;
						break;
					}
					canExecute = true;

				}
			}

			if (canExecute)
				btnPlayPause.getStyleClass().addAll("show");
			else
				btnPlayPause.getStyleClass().removeAll("show");
		}

		StackPane progressBarPane = (StackPane) pgbProgress.lookup(".bar");
		paintBar(progressBarPane);
	}

	private void loadProgressAndRealtime() {
		lblStatus.setText(activity.getStats().getDescription());
		Node bar = pgbProgress.lookup(".bar");
		if (bar != null)
			bar.setStyle("-fx-background-color:" + activity.getStats().getHexColor());
		double estimatedTime = activity.getEstimatedTime().toMillis();
		double realtime = activity.getRealtime().toMillis();
		double progress = realtime / estimatedTime;
		double difference = progress - 1;
		if (difference != 0) {
			FontAwesomeIconView icon = new FontAwesomeIconView();
			Tooltip tp = new Tooltip();
			if (difference > 0) {
				if (difference <= 0.75) {
					icon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOWN);
				} else {
					icon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOUBLE_DOWN);
					icon.setSize("1.5em");
				}
				icon.setFill(Color.RED);
				tp.setText("Percentual de tempo excedido");
				lblIndex.setStyle("-fx-text-fill:" + StatusEnum.BROKEN_FINALIZED.getHexColor());
			} else {
				if (difference >= -0.35) {
					icon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_UP);
				} else {
					icon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOUBLE_UP);
					icon.setSize("1.5em");
				}
				icon.setFill(Color.GREEN);
				tp.setText("Percentual de tempo desnecessário");
				lblIndex.setStyle("-fx-text-fill:" + StatusEnum.NORMAL_FINALIZED.getHexColor());
			}
			lblIndex.getStyleClass().removeAll("hide");
			lblIndex.setText(String.format("%.2f", Math.abs((difference * 100))) + "%");
			lblIndex.setGraphic(icon);
			Tooltip.install(lblIndex, tp);
		}
		progress = progress > 1 ? 1 : progress;
		if (activity.getStats() == StatusEnum.BROKEN_FINALIZED || activity.getStats() == StatusEnum.NORMAL_FINALIZED)
			pgbProgress.setProgress(1);
		else
			pgbProgress.setProgress(progress);
		String progressStr = String.format("%.2f", Math.abs((pgbProgress.getProgress() * 100)));
		lblProgress.setText(progressStr + "%");
		notifyAllOnProgressChangedListeners(activity);
	}

	private void initEvents() {
		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				new ActivityBO().delete(activity);
			}
		});
		btnPlayPause.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ExecutionTimeBO execBo = new ExecutionTimeBO();
				ActivityBO actBo = new ActivityBO();
				if (btnPlayPause.getText().equalsIgnoreCase("play")) {
					if (execBo.startExecution(activity) != null) {
						activity = actBo.switchStatus(activity, StatusEnum.NORMAL_IN_PROGRESS);
						if (btnDelete.isVisible()) {
							btnDelete.getStyleClass().remove("show");
						}
						ActivityMonitor.addActivity(activity);
					}else {
						
						ScreenUtil.jfxDialogOpener("Aviso!", "Um usuário só pode executar uma atividade por vez!\n"
								+ "Pause ou complete a atividade para começar outra.");
					}
				} else {
					if (execBo.finishExecution(activity) != null) {
						activity = actBo.switchStatus(activity, StatusEnum.NORMAL_PAUSED);
						ActivityMonitor.removeActivity(activity);
					}
				}
				loadActivity();
			}
		});
		btnFinalize.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ExecutionTimeBO execBo = new ExecutionTimeBO();
				ActivityBO actBo = new ActivityBO();
				if (execBo.finishExecution(activity) != null) {
					activity = actBo.switchStatus(activity, StatusEnum.NORMAL_FINALIZED);
					btnPlayPause.getStyleClass().removeAll("show");
					btnFinalize.getStyleClass().removeAll("show");
					loadActivity();

					ActivityMonitor.removeActivity(activity);
				}

			}
		});
		setOnMouseEntered(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				boolean itsDependency = false;
				List<ActivityVO> lst = new ArrayList<ActivityVO>();
				lst.addAll(getListView().getItems());
				System.out.println(lst.size());
				for (ActivityVO a : lst) {
					if (a.getId() == activity.getId())
						continue;
					if (a.getDependencies().contains(activity)) {
						itsDependency = true;
						break;
					}
				}
				
				
				if (activity.getStats().equals(StatusEnum.NOT_STARTED) && !itsDependency) {
					btnDelete.getStyleClass().add("show");
				}
				activity.setItsDependency(itsDependency);
			}
		});
		setOnMouseExited(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				btnDelete.getStyleClass().removeAll("show");

			}
		});
		pgbProgress.skinProperty().addListener(
				(ChangeListener<Skin<?>>) (observable, oldValue, newValue) -> paintBar(pgbProgress.lookup(".bar")));
		pgbProgress.progressProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				paintBar(pgbProgress.lookup(".bar"));

			}
		});
		ActivityMonitor.addOnMonitorTickListener(new OnMonitorTick() {
			@Override
			public void onMonitorTicked(ActivityVO act) {
				if (act.getId() == activity.getId())
					activity = act;
				loadProgressAndRealtime();

			}
		});
	}

	private void paintBar(Node node) {
		if (node != null)
			node.setStyle("-fx-background-color:" + activity.getStats().getHexColor());
	}

	private static ArrayList<OnProgressChangedI> activityAddedListeners = new ArrayList<OnProgressChangedI>();

	public interface OnProgressChangedI {
		void onProgressChangedI(ActivityVO act);
	}

	public static void addOnActivityAddedIListener(OnProgressChangedI newListener) {
		activityAddedListeners.add(newListener);
	}

	private void notifyAllOnProgressChangedListeners(ActivityVO act) {
		for (OnProgressChangedI l : activityAddedListeners) {
			l.onProgressChangedI(act);
		}
	}

	@Override
	public void notifyAllListeners(HashMap<String, Object> hmp) {
		for (ShowEditViewActivityObserverI l : listeners) {
			l.showEditViewActivity(hmp);
		}

	}

}
