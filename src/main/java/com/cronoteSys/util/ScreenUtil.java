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
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
	public static void openNewWindow(Stage oldStage, String sSceneName, boolean isModal) {

		Stage newStage = oldStage;

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
		newStage.show();

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
	public static void openNewWindow(Stage oldStage, String sSceneName, boolean isModal,
			HashMap<String, Object> hashMapValues) {
		openNewWindow(oldStage, sSceneName, isModal);
		new ScreenUtil().notifyAllListeners(sSceneName, hashMapValues);

	}

	private static Parent loadScene(String sSceneName) {
		Parent root = null;
		try {
			FXMLLoader fxmlLoader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
			URL url = new File(ScreenUtil.class.getResource("/fxml/" + sSceneName + ".fxml").getPath()).toURI().toURL();
			fxmlLoader.setLocation(url);
			root = fxmlLoader.load();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return root;
	}

	public static FXMLLoader loadTemplate(String template) {
		try {
			FXMLLoader fxmlLoader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
			URL url = new File(ScreenUtil.class.getResource("/fxml/Templates/" + template + ".fxml").getPath()).toURI()
					.toURL();
			fxmlLoader.setLocation(url);
			return fxmlLoader;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;

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
	public boolean isFilledFields(Stage oldStage, Pane pnl, boolean isRecursive) {
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
					node.requestFocus();
					if (!isRecursive) {
						showErrorLabel(pnl, nodeIndex, "Campo obrigatório");
						addORRemoveErrorClass(node, true);
					}
					return false;
				} else {
					if (!isRecursive) {
						hideErrorLabel(pnl, nodeIndex);
						addORRemoveErrorClass(node, false);
					}
				}
			}
			if (node instanceof ComboBox<?>) {
				if (((ComboBox<?>) node).getSelectionModel().isEmpty()) {
					node.requestFocus();
					if (!isRecursive) {
						showErrorLabel(pnl, nodeIndex, "Campo obrigatório");
						addORRemoveErrorClass(node, true);
					}
					return false;
				} else {
					if (!isRecursive) {
						hideErrorLabel(pnl, nodeIndex);
						addORRemoveErrorClass(node, false);
					}
				}

			}

			if (node instanceof Spinner<?>) {
				if (((Spinner<?>) node).getValue() == null) {
					node.requestFocus();
					if (!isRecursive) {
						showErrorLabel(pnl, nodeIndex, "Campo obrigatório");
						addORRemoveErrorClass(node, true);
					}
					return false;
				} else {
					if (!isRecursive) {
						hideErrorLabel(pnl, nodeIndex);
						addORRemoveErrorClass(node, false);
					}
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
				if (!isFilledFields(oldStage, (Pane) node, true)) {
					node.getStyleClass().addAll("hasError", "error");
					showErrorLabel(pnl, nodeIndex, "Campo obrigatório");
				} else {
					node.getStyleClass().removeAll("hasError", "error");
					hideErrorLabel(pnl, nodeIndex);
				}
				if (node.getStyleClass().contains("hasError")) {
					return false;
				}
			}
			nodeIndex++;
		}
		return true;
	}

	private void showErrorLabel(Pane pnl, int nodeIndex, String message) {
		Label label = (Label) pnl.getChildren().get(nodeIndex + 1);
		label.getStyleClass().remove("hide");
		label.getStyleClass().add("show");
		label.setText(message);
	}

	private void hideErrorLabel(Pane pnl, int nodeIndex) {
		Label label = (Label) pnl.getChildren().get(nodeIndex + 1);
		label.getStyleClass().add("hide");
		label.getStyleClass().remove("show");
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
			new ScreenUtil().addORRemoveErrorClass(lstTextFields, true);
			return false;
		}
		if (!sPass1.equals(sPass2)) {
			new ScreenUtil().addORRemoveErrorClass(lstTextFields, true);
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

	public static void paintScreen(HBox root) {
		int nodeIndex = 0;
		for (Node node : root.getChildren()) {
			if (nodeIndex > 0) {
				node.getStyleClass().removeAll("tone1-background", "tone2-background");
				if (nodeIndex % 2 == 0)
					node.getStyleClass().addAll("tone1-background");
				else
					node.getStyleClass().addAll("tone2-background");
			}
			nodeIndex++;
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

}
