package com.cronoteSys.controller.components.cellfactory;

import com.cronoteSys.controller.components.listcell.ActivityCellController;
import com.cronoteSys.model.vo.ActivityVO;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ActivityCellFactory implements Callback<ListView<ActivityVO>, ListCell<ActivityVO>> {
	@Override
	public ListCell<ActivityVO> call(ListView<ActivityVO> listview) {
		return new ActivityCellController();
	}

}
