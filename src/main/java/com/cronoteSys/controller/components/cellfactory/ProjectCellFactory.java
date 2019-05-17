package com.cronoteSys.controller.components.cellfactory;

import com.cronoteSys.controller.components.listcell.ProjectCellController;
import com.cronoteSys.model.vo.ProjectVO;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ProjectCellFactory implements Callback<ListView<ProjectVO>, ListCell<ProjectVO>> {
	@Override
	public ListCell<ProjectVO> call(ListView<ProjectVO> listview) {
		return new ProjectCellController();
	}
}
