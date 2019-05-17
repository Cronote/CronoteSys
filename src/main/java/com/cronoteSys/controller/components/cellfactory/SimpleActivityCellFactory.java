package com.cronoteSys.controller.components.cellfactory;

import com.cronoteSys.controller.components.listcell.SimpleActivityCellController;
import com.cronoteSys.model.vo.SimpleActivity;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class SimpleActivityCellFactory implements Callback<ListView<SimpleActivity>, ListCell<SimpleActivity>> {
	@Override
	public ListCell<SimpleActivity> call(ListView<SimpleActivity> listview) {
		return new SimpleActivityCellController();
	}

}
