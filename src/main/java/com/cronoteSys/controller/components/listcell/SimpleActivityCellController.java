package com.cronoteSys.controller.components.listcell;

import com.cronoteSys.model.vo.SimpleActivity;

import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class SimpleActivityCellController extends ListCell<SimpleActivity> {

	private Label lblTitle;
	private AnchorPane activityCardRoot;
	private Label lblCategory;
	private boolean hasToFixHeitgh;

	public SimpleActivityCellController(Orientation orientation, Double width) {

		initComponents();

		switch (orientation) {
		case HORIZONTAL:
			activityCardRoot.setPrefWidth(width);
			setPrefWidth(width);

			AnchorPane.setBottomAnchor(lblCategory, 10.0);
			AnchorPane.setRightAnchor(lblCategory, 5.0);
			AnchorPane.setLeftAnchor(lblCategory, 5.0);
			break;
		case VERTICAL:
			Double parentWidth = width * 2;
			hasToFixHeitgh = true;
			activityCardRoot.setPrefWidth(parentWidth);
			setPrefWidth(parentWidth);
			lblTitle.setPrefWidth(parentWidth - (0.05 * parentWidth));
			lblTitle.setWrapText(true);

			break;
		}
	}

	private void initComponents() {
		activityCardRoot = new AnchorPane();
		activityCardRoot.getStyleClass().addAll("card", "borders");
		lblTitle = new Label();
		lblCategory = new Label();

		lblTitle.setFont(new Font("system bold", 17.0));
		lblTitle.getStyleClass().add("info");
		lblCategory.setFont(new Font("system bold", 15.0));
		lblCategory.getStyleClass().add("info");

		clearAndAddNodes();

		AnchorPane.setTopAnchor(lblTitle, 10.0);
		AnchorPane.setRightAnchor(lblTitle, 5.0);
		AnchorPane.setLeftAnchor(lblTitle, 5.0);

	}

	private void clearAndAddNodes() {
		activityCardRoot.getChildren().clear();
		activityCardRoot.getChildren().addAll(lblTitle, lblCategory);
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

	@Override
	public void updateSelected(boolean selected) {
		super.updateSelected(selected);
		if (selected) {
			activityCardRoot.getStyleClass().add("cardSelected");
		} else
			activityCardRoot.getStyleClass().removeAll("cardSelected");

	}

	public void presetGraphic(Node value) {
		if (hasToFixHeitgh) {
			Double heights = 10 + Double.parseDouble(getHeightOf(lblTitle)[0].toString()) + 5;
			AnchorPane.setTopAnchor(lblCategory, heights);
			AnchorPane.setRightAnchor(lblCategory, 5.0);
			AnchorPane.setLeftAnchor(lblCategory, 5.0);
//			AnchorPane.setBottomAnchor(lblCategory, 3.0);
			heights += Double.parseDouble(getHeightOf(lblCategory)[0].toString());
			heights += 12;
			activityCardRoot.setPrefHeight(heights);
			setPrefHeight(heights);
			clearAndAddNodes();
		}
		super.setGraphic(value);
	}

	@Override
	protected void updateItem(SimpleActivity item, boolean empty) {
		// TODO Auto-generated method stub
		super.updateItem(item, empty);
		if (item != null || !empty) {
			lblTitle.setText(item.getTitle());
			lblCategory.setText(item.getCategoryVO().getDescription());
			presetGraphic(activityCardRoot);
			setStyle("-fx-background-color:red;");
		} else {
			setGraphic(null);
		}
		setStyle("-fx-background-color:transparent;");

	}

}
