package com.cronoteSys.controller.components.dialogs;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.cronoteSys.controller.components.cellfactory.SimpleActivityCellFactory;
import com.cronoteSys.filter.ActivityFilter;
import com.cronoteSys.model.dao.ActivityDAO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.SimpleActivity;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.util.SessionUtil;
import com.google.inject.Inject;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DialogDependencyManagerController implements Initializable {

	@Inject
	private ActivityDAO actDAO;
	private UserVO loggedUser = (UserVO) SessionUtil.getSession().get("loggedUser");
	private ActivityVO selectedActivity;
	@FXML private BorderPane dialogroot;
	@FXML private Button btnCancel;
	@FXML private Button btnConfirm;
	@FXML private Label lblActivityTitle;
	@FXML private ListView<SimpleActivity> activityLst;
	@FXML private ListView<SimpleActivity> selectedActivitiesLst;
	@FXML private Button btnUnselect;
	@FXML private Button btnSelect;
	private ActivityFilter filter = new ActivityFilter();
	static final DataFormat ACT_LIST = new DataFormat("actList");

	public void setSelectedActivity(ActivityVO selectedActivity) {
		this.selectedActivity = selectedActivity;
		filter.setProject(selectedActivity.getProjectVO().getId());
		activityLst.setItems(FXCollections.observableArrayList(actDAO.getSimpleActivitiesView(filter)));
		activityLst.getItems().remove(SimpleActivity.fromActivity(selectedActivity));
		activityLst.getItems().removeAll(SimpleActivity.fromList(selectedActivity.getDependencies()));
		selectedActivitiesLst.setItems(
				FXCollections.observableArrayList(SimpleActivity.fromList(selectedActivity.getDependencies())));
		lblActivityTitle.setText(selectedActivity.getTitle());

		selectedActivitiesLst.getItems().addListener(new ListChangeListener<SimpleActivity>() {
			@Override
			public void onChanged(Change<? extends SimpleActivity> c) {
				btnUnselect.setDisable(c.getList().isEmpty());
				btnConfirm.setDisable(c.getList().isEmpty());
			}

		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		activityLst.setCellFactory(new SimpleActivityCellFactory());
		selectedActivitiesLst.setCellFactory(new SimpleActivityCellFactory());
		btnConfirm.setDisable(true);
		btnConfirm.setDisable(true);
		initEvents();

	}


	private void dragDetected(MouseEvent event, ListView<SimpleActivity> listView) {
		// Make sure at least one item is selected
		int selectedCount = listView.getSelectionModel().getSelectedIndices().size();

		if (selectedCount == 0) {
			event.consume();
			return;
		}

		// Initiate a drag-and-drop gesture
		Dragboard dragboard = listView.startDragAndDrop(TransferMode.MOVE);

		// Put the the selected items to the dragboard
		ArrayList<SimpleActivity> selectedItems = this.getSelectedActivity(listView);

		ClipboardContent content = new ClipboardContent();
		content.put(ACT_LIST, selectedItems);

		dragboard.setContent(content);
		event.consume();
	}

	private void dragOver(DragEvent event, ListView<SimpleActivity> listView) {
		// If drag board has an ITEM_LIST and it is not being dragged
		// over itself, we accept the MOVE transfer mode
		Dragboard dragboard = event.getDragboard();

		if (event.getGestureSource() != listView && dragboard.hasContent(ACT_LIST)) {
			event.acceptTransferModes(TransferMode.MOVE);
		}

		event.consume();
	}

	@SuppressWarnings("unchecked")
	private void dragDropped(DragEvent event, ListView<SimpleActivity> listView) {
		boolean dragCompleted = false;

		// Transfer the data to the target
		Dragboard dragboard = event.getDragboard();

		if (dragboard.hasContent(ACT_LIST)) {
			ArrayList<SimpleActivity> list = (ArrayList<SimpleActivity>) dragboard.getContent(ACT_LIST);
			listView.getItems().addAll(list);
			// Data transfer is successful
			dragCompleted = true;
		}

		// Data transfer is not successful
		event.setDropCompleted(dragCompleted);
		event.consume();
	}

	private void dragDone(DragEvent event, ListView<SimpleActivity> listView) {
		// Check how data was transfered to the target
		// If it was moved, clear the selected items
		TransferMode tm = event.getTransferMode();

		if (tm == TransferMode.MOVE) {
			removeSelectedActivity(listView);
		}

		event.consume();
	}

	private ArrayList<SimpleActivity> getSelectedActivity(ListView<SimpleActivity> listView) {
		// Return the list of selected activity in an ArratyList, so it is
		// serializable and can be stored in a Dragboard.
		ArrayList<SimpleActivity> list = new ArrayList<>(listView.getSelectionModel().getSelectedItems());

		return list;
	}

	private void removeSelectedActivity(ListView<SimpleActivity> listView) {
		// Get all selected Activity in a separate list to avoid the shared list issue
		List<SimpleActivity> selectedList = new ArrayList<>();

		for (SimpleActivity fruit : listView.getSelectionModel().getSelectedItems()) {
			selectedList.add(fruit);
		}

		// Clear the selection
		listView.getSelectionModel().clearSelection();
		// Remove items from the selected list
		listView.getItems().removeAll(selectedList);
	}

	private void initEvents() {
		activityLst.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				dragDetected(event, activityLst);
			}
		});

		activityLst.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				dragOver(event, activityLst);
			}
		});

		activityLst.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				dragDropped(event, activityLst);
			}
		});

		activityLst.setOnDragDone(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				dragDone(event, activityLst);
			}
		});

		// Add mouse event handlers for the target
		selectedActivitiesLst.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				dragDetected(event, selectedActivitiesLst);
			}
		});

		selectedActivitiesLst.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				dragOver(event, selectedActivitiesLst);
			}
		});

		selectedActivitiesLst.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				dragDropped(event, selectedActivitiesLst);
			}
		});

		selectedActivitiesLst.setOnDragDone(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				dragDone(event, selectedActivitiesLst);
			}
		});

		btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				selectedActivity.setDependenciesFromSimple(selectedActivitiesLst.getItems());
				((Stage) btnCancel.getScene().getWindow()).close();
			}
		});
		btnCancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Stage) btnCancel.getScene().getWindow()).close();
			}
		});
		btnSelect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				SimpleActivity selected = activityLst.getSelectionModel().getSelectedItem();
				if (selected != null) {
					activityLst.getItems().remove(selected);
					activityLst.getSelectionModel().clearSelection();
					selectedActivitiesLst.getItems().add(selected);
				}
			}
		});
		btnUnselect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				SimpleActivity unselecting = selectedActivitiesLst.getSelectionModel().getSelectedItem();
				if (unselecting != null) {
					activityLst.getItems().add(unselecting);
					selectedActivitiesLst.getItems().remove(unselecting);
					selectedActivitiesLst.getSelectionModel().clearSelection();

				}
			}
		});
	}

}
