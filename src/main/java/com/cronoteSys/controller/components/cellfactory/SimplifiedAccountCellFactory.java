package com.cronoteSys.controller.components.cellfactory;

import com.cronoteSys.controller.components.listcell.SimplifiedAccountCellController;
import com.cronoteSys.model.vo.view.SimpleUser;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class SimplifiedAccountCellFactory implements Callback<ListView<SimpleUser>, ListCell<SimpleUser>> {
	@Override
	public ListCell<SimpleUser> call(ListView<SimpleUser> listview) {
		return new SimplifiedAccountCellController();
	}
}
