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

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
		for (Node node : comp) {
			if (!node.isVisible())
				continue;
			if (node.getStyleClass().contains("NotRequired"))
				continue;
			if (node instanceof TextField) {

				if (((TextField) node).getText().trim().equals("")) {
					node.requestFocus();
					HashMap<String, Object> hmapValues = new HashMap<String, Object>();
					hmapValues.put("msg", "Preencha o campo " + ((TextField) node).getPromptText());
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

		}
		return true;
	}

	public void clearFields(Stage oldStage, Pane pnl) {
		ObservableList<Node> comp = pnl.getChildren();
		for (Node node : comp) {
			if (node instanceof TextField) {
				((TextField) node).setText("");
			}
			if (node instanceof DatePicker) {
				((DatePicker) node).setValue(null);
			}
		}
	}

	public void addORRemoveErrorClass(Node node, boolean isAdd) {
		if (node != null) {
			if(!node.isVisible()) {
				node.setVisible(true);
			}else {
				if (isAdd) {
					node.getStyleClass().remove("error");
					node.getStyleClass().add("error");
				} else {
					node.getStyleClass().remove("error");
				}
			}
		}
	}

	public static boolean verifyPassFields(String sPass1, String sPass2, List<Node> lstTextFields) {
		if (sPass1.equals("")|| sPass2.equals("")) 
			return false;
		
		if (!new LoginBO().validatePassword(sPass1)) {
				System.out.println("Mensagem de falha por senhas fora de formato ");
				new ScreenUtil().addORRemoveErrorClass(lstTextFields, true);
				return false; 
			}
		if (!sPass1.equals(sPass2)) {
			new ScreenUtil().addORRemoveErrorClass(lstTextFields, true);
			System.out.println(	 "Mensagem de falha por senhas diferentes");
			return false;
		}
		new ScreenUtil().addORRemoveErrorClass(lstTextFields, false);

		return true;
	}
	
	public void addORRemoveErrorClass(java.util.List<Node> node, boolean isAdd) {
		for (Node n : node) {
			if (n != null) {
				if(!n.isVisible()) {
					n.setVisible(true);
				}else {
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
