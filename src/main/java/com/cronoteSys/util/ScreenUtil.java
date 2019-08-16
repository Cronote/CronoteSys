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

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.base.IFXValidatableControl;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;

import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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

		Stage newStage = oldStage != null ? oldStage : new Stage();

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
//			System.out.print(node);
			if (nodeIndex > 0) {
//				System.out.println(" " +nodeIndex );
				node.getStyleClass().removeAll("tone1-background", "tone2-background");
				if (nodeIndex % 2 == 0)
					node.getStyleClass().addAll("tone1-background");
				else
					node.getStyleClass().addAll("tone2-background");
			}
			nodeIndex++;
		}

	}

	public static void removeInlineValidation(Node[] fields) {
		for (int i = 0; i < fields.length; i++) {
			Node node = fields[i];
			if (node instanceof IFXValidatableControl) {
				IFXValidatableControl jfxValidatableField = (IFXValidatableControl) node;
				jfxValidatableField.getValidators().clear();

			}
		}
	}

	public static void addInlineValidation(Node[] fields, Boolean[] isNotnull, Boolean[] isEmail) {
		// required
		RequiredFieldValidator requiredValidator = new RequiredFieldValidator();
		requiredValidator.setMessage("CAMPO OBRIGATÓRIO!");
		requiredValidator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING)
				.size("1em").styleClass("error").build());
		// email
		RegexValidator emailValidator = new RegexValidator();
		emailValidator.setRegexPattern("(^([A-z]+)([A-z0-9-_.]*)@([A-z.]+)\\.[A-z]{2,}$)|");
		emailValidator.setMessage("EMAIL EM FORMATO INVÁLIDO!");
		/*
		 * Password /^ Inicio de string (?=.*\d) deve conter ao menos um dígito
		 * (?=.*[a-z]) deve conter ao menos uma letra minúscula (?=.*[A-Z]) deve conter
		 * ao menos uma letra maiúscula (?=.*[$*&@#_!\\/()-]]) deve conter ao menos um
		 * caractere especial [0-9a-zA-Z$*&@#_!\\/()-]]{8,} deve conter ao menos 8 dos
		 * caracteres mencionados $/ Fim de string
		 */

		for (int i = 0; i < fields.length; i++) {
			Node node = fields[i];
			if (node instanceof IFXValidatableControl) {
				IFXValidatableControl jfxValidatableField = (IFXValidatableControl) node;
				if (isNotnull[i])
					jfxValidatableField.getValidators().add(requiredValidator);
				((Node) jfxValidatableField).focusedProperty().addListener((o, oldVal, newVal) -> {
					if (!newVal) {
						jfxValidatableField.validate();
					}
				});
			}
			if (node instanceof JFXTextField) {
				JFXTextField jfxTextField = (JFXTextField) node;
				if (isEmail[i]) {
					jfxTextField.getValidators().clear();
					jfxTextField.getValidators().add(emailValidator);
				}
			}
			if (node instanceof JFXPasswordField) {
				JFXPasswordField jfxPasswordField = (JFXPasswordField) node;
				if (isNotnull[i]) {
					jfxPasswordField.getValidators().clear();
					jfxPasswordField.getValidators().add(requiredValidator);

				}
			}
		}
	}

	public static void addPasswordFormatValidator(JFXPasswordField jfxPasswordField) {
		RegexValidator passwordValidator = new RegexValidator();
		passwordValidator
				.setRegexPattern("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[$*&@#_!\\/()-])[0-9a-zA-Z$*&@#_!\\/()-]{8,}$");
		passwordValidator.setMessage("SENHA EM FORMATO INVÁLIDO!");
		jfxPasswordField.getValidators().add(passwordValidator);
		jfxPasswordField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				jfxPasswordField.validate();
			}
		});
	}

	public static String colorToRGBString(String strColor) {
		// TODO: nao esta convertendo corretamente
		Color color = stringToColor(strColor);
		double r = color.getRed();
		double g = color.getGreen();
		double b = color.getBlue();
		double a = color.getOpacity();
		String rgba = (r * 255) + "," + (g * 255) + "," + (b * 255) + "," + a;
		return rgba;
	}

	public static Color stringToColor(String colorString) {
		try {
			return Color.valueOf(colorString);
		} catch (Exception e) {
			e.printStackTrace(); 
			return Color.DEEPPINK;
		}

	}
	
	public static void jfxDialogOpener(StackPane stackpane) {
		JFXDialogLayout content = new JFXDialogLayout();
		content.getStyleClass().addAll("tone1-background");
		Text texto = new Text("Information");
		texto.getStyleClass().add("letters_box_icons");
	    content.setHeading(texto);
	    texto.setText("We are going to open your default browser window to let \n" +
	            "you choose the gmail account , allow the specified permissions\n" +
	            "and then close the window.");
	    content.setBody(texto);
	    JFXDialog dialog = new JFXDialog(stackpane, content, JFXDialog.DialogTransition.NONE);
	    JFXButton button = new JFXButton("Okay");
	    button.getStyleClass().addAll("letters_box_icons","btn");
	    button.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	            dialog.close();
	        }
	    });
	    content.setActions(button);
	    dialog.show();
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
