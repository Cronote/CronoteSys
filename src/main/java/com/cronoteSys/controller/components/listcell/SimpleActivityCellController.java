package com.cronoteSys.controller.components.listcell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.bo.ExecutionTimeBO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.SimpleActivity;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.observer.ShowEditViewActivityObservableI;
import com.cronoteSys.observer.ShowEditViewActivityObserverI;
import com.cronoteSys.util.ActivityMonitor;
import com.cronoteSys.util.ActivityMonitor.OnMonitorTick;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class SimpleActivityCellController extends ListCell<SimpleActivity> {

	@FXML
	Label lblTitle;
	@FXML
	AnchorPane activityCardRoot;
	@FXML
	Label lblCategory;

	{
		FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
		try {
			loader.setLocation(
					new File(getClass().getResource("/fxml/Templates/cell/SimpleActivityCell.fxml").getPath()).toURI()
							.toURL());
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void updateSelected(boolean selected) {
		super.updateSelected(selected);
		if (selected) {
			activityCardRoot.getStyleClass().add("cardSelected");
		} else
			activityCardRoot.getStyleClass().removeAll("cardSelected");

	}
	@Override
	protected void updateItem(SimpleActivity item, boolean empty) {
		// TODO Auto-generated method stub
		super.updateItem(item, empty);
		if (item != null || !empty) {
			lblTitle.setText(item.getTitle());
			lblCategory.setText(item.getCategoryVO().getDescription());
			setGraphic(activityCardRoot);
		} else {
			setGraphic(null);
		}
		setStyle("-fx-background-color:transparent;");

	}

}
