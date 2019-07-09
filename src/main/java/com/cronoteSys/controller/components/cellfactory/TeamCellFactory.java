package com.cronoteSys.controller.components.cellfactory;

import com.cronoteSys.controller.components.listcell.TeamCellController;
import com.cronoteSys.model.vo.TeamVO;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class TeamCellFactory implements Callback<ListView<TeamVO>, ListCell<TeamVO>> {
	@Override
	public ListCell<TeamVO> call(ListView<TeamVO> listview) {
		return new TeamCellController();
	}
}
