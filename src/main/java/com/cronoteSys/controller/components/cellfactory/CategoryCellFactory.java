package com.cronoteSys.controller.components.cellfactory;

import com.cronoteSys.controller.components.listcell.CategoryCell;
import com.cronoteSys.model.vo.CategoryVO;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CategoryCellFactory implements Callback<ListView<CategoryVO>, ListCell<CategoryVO>> {
	@Override
	public ListCell<CategoryVO> call(ListView<CategoryVO> listview) {
		return new CategoryCell();
	}
}
