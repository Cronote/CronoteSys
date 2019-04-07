/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cronoteSys.model.bo.LoginBO;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Class that cares about all Screen functions
 *
 * @author bruno
 */
public class ScreenUtil {

	/**
	 * Method that opens a new screen
	 *
	 * @param oldStage   is the old window, that will be closed
	 * @param sSceneName is the scene's name, that will be loaded
	 * @param isModal    True or false about the modal property
	 */
	public void openNewWindow(Stage oldStage, String sSceneName, boolean isModal) {

		Stage newStage = new Stage();

		newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
			}
		});
		Parent root = loadScene(sSceneName);

		Scene scene = new Scene(root);
		scene.setRoot(root);
		scene.getStylesheets().add("/styles/Styles.css");
		newStage.setTitle("Cronote");
		newStage.setScene(scene);
		if (isModal) {
			newStage.initOwner(oldStage);
			newStage.initModality(Modality.APPLICATION_MODAL);
		}
		newStage.show();
		if (!isModal) {
			closeOldStage(oldStage, newStage);
		}
	}

	/**
	 * Method that opens a new screen, thanks to 'hashMapValues' we can now pass
	 * objects from one screen to another
	 *
	 * @param oldStage      is the old window, that will be closed
	 * @param sSceneName    is the scene's name, that will be loaded
	 * @param isModal       True or false about the modal property
	 * @param hashMapValues Its filled with the objects that we need to pass to the
	 *                      new window
	 */
	public void openNewWindow(Stage oldStage, String sSceneName, boolean isModal,
			HashMap<String, Object> hashMapValues) {
		Stage newStage = new Stage();
		newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.out.println("asd");
				Platform.exit();
			}
		});

		Parent root = loadScene(sSceneName);
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/styles/Styles.css");
		newStage.setTitle("Cronote");
		newStage.setScene(scene);
		if (isModal) {
			newStage.initOwner(oldStage);
			newStage.initModality(Modality.APPLICATION_MODAL);
		}
		notifyAllListeners(sSceneName, hashMapValues);
		newStage.show();
		if (!isModal) {
			closeOldStage(oldStage, newStage);

		}
	}

	private Parent loadScene(String sSceneName) {
		Parent root = null;
		try {
			System.out.println("Scene>>>> " + sSceneName);
			URL url = new File(getClass().getResource("/fxml/" + sSceneName + ".fxml").getPath()).toURI().toURL();
			root = FXMLLoader.load(url);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		return root;
	}

	private void closeOldStage(Stage oldStage, Stage newStage) {
		if (oldStage != null) {
			oldStage.close();
		}
	}

	/**
	 *
	 *
	 * @param oldStage
	 *
	 *
	 * @author bruno
	 *
	 *
	 */
	public boolean isFilledFields(Stage oldStage, Pane pnl) {
		ObservableList<Node> comp = pnl.getChildren();
		int nodeIndex = 0;
		for (Node node : comp) {
			if (!node.isVisible()) {
				nodeIndex++;
				continue;
			}
			if (node.getStyleClass().contains("NotRequired")) {
				nodeIndex++;
				continue;
			}
			if (node instanceof TextInputControl) {
				if (((TextInputControl) node).getText().trim().equals("")) {
					System.out.println(((TextInputControl) node).getPromptText());
					node.requestFocus();
					Label label = (Label) pnl.getChildren().get(nodeIndex+1);
					label.getStyleClass().remove("hide");
					label.getStyleClass().add("show");
					label.setText("*Campo obrigatório");
					addORRemoveErrorClass(node, true);
					return false;
				} else {
					addORRemoveErrorClass(node, false);
				}
			}
			if (node instanceof ComboBox<?>) {
				System.out.println("Combo value " + ((ComboBox<?>) node).getValue());
				if (((ComboBox<?>) node).getSelectionModel().isEmpty()) {
					node.requestFocus();
					addORRemoveErrorClass(node, true);
					return false;
				} else {
					addORRemoveErrorClass(node, false);
				}

			}

			if (node instanceof Spinner<?>) {
				if (((Spinner<?>) node).getValue() == null) {
					node.requestFocus();
					addORRemoveErrorClass(node, true);
					return false;
				} else {
					addORRemoveErrorClass(node, false);
				}

			}
			if (node instanceof DatePicker) {
				if (((DatePicker) node).getValue() == null) {
					node.requestFocus();
					return false;
				}
			}
			if (node instanceof ToggleButton) {

				if (((ToggleButton) node).getToggleGroup().getSelectedToggle() == null) {
					node.requestFocus();
					return false;
				}
			}
			if (node instanceof Pane) {
				if(!isFilledFields(oldStage, (Pane) node)) {
					node.getStyleClass().addAll("hasError", "error");
				}else {
					node.getStyleClass().removeAll("hasError", "error");
				}
				if(node.getStyleClass().contains("hasError")) {
					return false;
				}
			}
			nodeIndex++;
		}
		return true;
	}

	public void clearFields(Stage oldStage, Pane pnl) {
		ObservableList<Node> comp = pnl.getChildren();
		for (Node node : comp) {
			if (node instanceof TextInputControl) {
				((TextInputControl) node).setText("");
			}
			if (node instanceof DatePicker) {
				((DatePicker) node).setValue(null);
			}
			if (node instanceof ComboBox) {
				((ComboBox<?>) node).setValue(null);
			}
		}
	}

	public void addORRemoveErrorClass(Node node, boolean isAdd) {
		if (node != null) {
			if (node.getStyleClass().contains("hide")) {
				node.getStyleClass().remove("hide");
				node.getStyleClass().add("show");
			} else if (node.getStyleClass().contains("show")) {
				node.getStyleClass().remove("show");
				node.getStyleClass().add("hide");
			} else {
				if (isAdd) {
					node.getStyleClass().remove("error");
					node.getStyleClass().add("error");
				} else {
					node.getStyleClass().remove("error");
				}
			}
		}
	}

	public static boolean verifyPassFields(String sPass1, String sPass2, List<Node> lstTextFields,
			List<Node> lstLabel) {
		if (sPass1.equals("") || sPass2.equals(""))
			return false;
		if (!new LoginBO().validatePassword(sPass1)) {
//			lstLabel.get(0).getStyleClass().remove("hide");
//			lstLabel.get(0).getStyleClass().add("show");
			new ScreenUtil().addORRemoveErrorClass(lstTextFields, true);
			return false;
		}
		if (!sPass1.equals(sPass2)) {
			new ScreenUtil().addORRemoveErrorClass(lstTextFields, true);
//			lstLabel.get(1).getStyleClass().remove("hide");
//			lstLabel.get(1).getStyleClass().add("show");
			System.out.println("Mensagem de falha por senhas diferentes");
			return false;
		}
		new ScreenUtil().addORRemoveErrorClass(lstTextFields, false);
		return true;
	}

	public void addORRemoveErrorClass(java.util.List<Node> node, boolean isAdd) {
		for (Node n : node) {
			if (n != null) {
				if (n.getStyleClass().contains("hide")) {
					n.getStyleClass().remove("hide");
					n.getStyleClass().add("show");
				} else if (n.getStyleClass().contains("show")) {
					n.getStyleClass().remove("show");
					n.getStyleClass().add("hide");
				} else {
					if (isAdd) {
						n.getStyleClass().remove("error");
						n.getStyleClass().add("error");
					} else {
						n.getStyleClass().remove("error");
					}
				}
			}
		}

	}

	public static void removeDefaultStyleClass(List<Node> nodes) {
		for (Node node : nodes) {
			if (node instanceof TextField) {
				node.getStyleClass().removeAll(new TextField().getStyleClass());
			}
			if (node instanceof TextArea) {
				node.getStyleClass().removeAll(new TextArea().getStyleClass());
			}
			if (node instanceof ComboBox) {
				node.getStyleClass().removeAll(new ComboBox<Object>().getStyleClass());
			}
			if (node instanceof Spinner) {
				node.getStyleClass().removeAll(new Spinner<Object>().getStyleClass());
			}
		}
	}

	public static void addDefaultStyleClass(Node node) {
		if (node instanceof TextField) {
			node.getStyleClass().addAll(new TextField().getStyleClass());
		}
		if (node instanceof TextArea) {
			node.getStyleClass().addAll(new TextArea().getStyleClass());
		}
		if (node instanceof ComboBox) {
			node.getStyleClass().addAll(new ComboBox<Object>().getStyleClass());
		}
		if (node instanceof Spinner) {
			node.getStyleClass().addAll(new Spinner<Object>().getStyleClass());
		}
		if (!(node instanceof Label)) {
			node.getStyleClass().remove("label");
		}
	}

	public static void addDefaultStyleClass(List<Node> nodes) {
		for (Node node : nodes) {
			if (node instanceof TextField) {
				node.getStyleClass().addAll(new TextField().getStyleClass());
			}
			if (node instanceof TextArea) {
				node.getStyleClass().addAll(new TextArea().getStyleClass());
			}
			if (node instanceof ComboBox) {
				node.getStyleClass().addAll(new ComboBox<Object>().getStyleClass());
			}
			if (node instanceof Spinner) {
				node.getStyleClass().addAll(new Spinner<Object>().getStyleClass());
			}
			if (!(node instanceof Label)) {
				node.getStyleClass().remove("label");
			}
		}
	}

	public static void removeStyleClass(List<Node> nodes, String styleClass) {
		for (Node node : nodes) {
			if (!(node.getClass().getSimpleName().equalsIgnoreCase(styleClass))) {
				node.getStyleClass().removeAll(styleClass);
			}
		}
	}

	public static void addStyleClass(List<Node> nodes, String styleClass) {
		for (Node node : nodes) {
			if (!(node instanceof ButtonBase || node instanceof ToggleButton)) {
				node.getStyleClass().addAll(styleClass);
			}
		}
	}

	public static void addStyleClass(List<Node> nodes, List<String> styleClass) {
		for (Node node : nodes) {
			node.getStyleClass().addAll(styleClass);
		}
	}

	public static void addDefaulClassOnFocusIn(List<Node> nodes) {
		for (Node node : nodes) {
			node.focusedProperty().addListener(new ChangeListener<Boolean>() {
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						List<Node> lst = new ArrayList<Node>();
						lst.add(node);
						addDefaultStyleClass(lst);
						removeStyleClass(lst, "label");
					} else {
						List<Node> lst = new ArrayList<Node>();
						lst.add(node);
						removeDefaultStyleClass(lst);
						addStyleClass(lst, "label");
					}

				}
			});
		}
	}

	private static ArrayList<OnChangeScreen> listeners = new ArrayList<OnChangeScreen>();

	public interface OnChangeScreen {
		void onScreenChanged(String newScreen, HashMap<String, Object> hmap);
	}

	public static void addOnChangeScreenListener(OnChangeScreen newListener) {
		listeners.add(newListener);
	}

	private void notifyAllListeners(String newScreen, HashMap<String, Object> hmap) {
		for (OnChangeScreen l : listeners) {
			l.onScreenChanged(newScreen, hmap);
		}
	}

	public static HBox recoverRoot(Node node) {
		while (node.getParent() != null) {
			node = node.getParent();
		}
		return (HBox) node;
	}
}
