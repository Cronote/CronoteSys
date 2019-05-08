package com.cronoteSys.dialogs;

import javafx.scene.control.ListCell;

public class ThemedStringCell<T> extends ListCell<T> {
	
	@Override
	protected void updateItem(T item, boolean empty) {
		// TODO Auto-generated method stub
		super.updateItem(item, empty);
		setStyle("-fx-background-color:red;");
	}
	
}
