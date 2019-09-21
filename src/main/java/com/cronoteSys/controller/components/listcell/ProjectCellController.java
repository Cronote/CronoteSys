package com.cronoteSys.controller.components.listcell;

import java.util.List;

import com.cronoteSys.filter.ActivityFilter;
import com.cronoteSys.model.bo.ActivityBO;
import com.cronoteSys.model.vo.ActivityVO;
import com.cronoteSys.model.vo.ProjectVO;
import com.cronoteSys.model.vo.StatusEnum;
import com.cronoteSys.util.ScreenUtil;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ProjectCellController extends ListCell<ProjectVO> {
	private Label lblTitle;
	private Label lblTeamMembers;
	private Label lblActivityDoneTotal;
	private AnchorPane projectCardRoot;
	private StackPane stkColorTag;

	private static double MAXWIDTH = 210.0;

	{
		projectCardRoot = new AnchorPane();

		projectCardRoot.setMaxWidth(MAXWIDTH);
		projectCardRoot.getStyleClass().addAll("card", "borders");

		lblTitle = new Label();
		lblTitle.setPrefWidth(130.0);
		lblTitle.setWrapText(true);
		lblTitle.setFont(new Font("System bold", 15));
		lblTitle.setAlignment(Pos.TOP_LEFT);

		AnchorPane.setTopAnchor(lblTitle, 10.0);
		AnchorPane.setLeftAnchor(lblTitle, 16.0);

		lblTeamMembers = new Label();
		{
			FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.USERS);
			icon.setSize("1em");
			icon.setFill(Color.TRANSPARENT);
			icon.setStroke(Color.WHITE);
			lblTeamMembers.setGraphic(icon);
			Tooltip tp = new Tooltip("Quantidade de Membros");
			Tooltip.install(lblTeamMembers, tp);
			AnchorPane.setLeftAnchor(lblTeamMembers, 15.0);
		}
		lblActivityDoneTotal = new Label();
		{
			FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.FILE_TEXT_ALT);
			icon.setSize("1em");
			icon.setFill(Color.WHITE);
			lblActivityDoneTotal.setGraphic(icon);
			Tooltip tp = new Tooltip("Quantidade de atividades: Concluidas / Total");
			Tooltip.install(lblActivityDoneTotal, tp);
			AnchorPane.setRightAnchor(lblActivityDoneTotal, 13.0);
		}
		stkColorTag = new StackPane();
		stkColorTag.setPrefSize(15.0, 38.0);
		AnchorPane.setTopAnchor(stkColorTag, -2.0);
		AnchorPane.setRightAnchor(stkColorTag, 20.0);

		projectCardRoot.getChildren().addAll(lblTitle, lblTeamMembers, lblActivityDoneTotal, stkColorTag);
	}

	private void fix() {

		Font infoFont = new Font("System bold", 13);
		Double sumHeights = 10.0;
		FontAwesomeIconView icons[] = { new FontAwesomeIconView(FontAwesomeIcon.TRASH_ALT),
				new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE_ALT) };// 0 - delete, 1 - finalize

		for (FontAwesomeIconView icon : icons) {
			icon.setSize("2em");
			icon.getStyleClass().add("letters_box_icons");
		}

		sumHeights += Double.parseDouble(getHeightOf(lblTitle)[0].toString()) + 10;

		lblTeamMembers.setLayoutY(sumHeights);
		lblActivityDoneTotal.setLayoutY(sumHeights);
		sumHeights += Double.parseDouble(getHeightOf(lblTeamMembers)[0].toString());
		sumHeights += 5.0;

		projectCardRoot.getChildren().clear();
		projectCardRoot.getChildren().addAll(lblTitle, lblTeamMembers, lblActivityDoneTotal, stkColorTag);

//		projectCardRoot.setMinHeight(sumHeights);
		projectCardRoot.setPrefHeight(sumHeights);

		for (Node n : projectCardRoot.getChildren()) {
			if (n instanceof Label) {
				((Label) n).setTextFill(Color.WHITE);
				((Label) n).getStyleClass().clear();
				((Label) n).getStyleClass().addAll("info");
				if (n.equals(lblTitle))
					continue;
				((Label) n).setFont(infoFont);

			}
		}
//		AnchorPane.setBottomAnchor(lblTeamMembers, 5.0);
//		AnchorPane.setBottomAnchor(lblActivityDoneTotal, 5.0);
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
			projectCardRoot.getStyleClass().add("cardSelected");

		} else
			projectCardRoot.getStyleClass().removeAll("cardSelected");
	}

	@Override
	public void updateItem(ProjectVO item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null || !empty) {
//			FXMLLoader loader = SessionUtil.getInjector().getInstance(FXMLLoader.class);
//			try {
//				loader.setLocation(new File(getClass().getResource("/fxml/Templates/cell/ProjectCard.fxml").getPath())
//						.toURI().toURL());
//				loader.setController(this);
//				loader.load();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			loadInfo(item);
			fix();
			setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					System.out.println(lblTitle.getWidth());
					System.out.println(getWidth());
					// TODO Auto-generated method stub

				}
			});
			setGraphic(projectCardRoot);
		} else {
			setText(null);
			setGraphic(null);
		}
		setStyle("-fx-background-color:transparent;");
	}

	private void loadInfo(ProjectVO item) {
		lblTitle.setText(item.getTitle());
		if (item.getTeam() != null) {
			String color = item.getTeam() != null ? ScreenUtil.colorToRGBString(item.getTeam().getTeamColor())
					: "1,1,1,1";
			stkColorTag.setStyle("-fx-background-radius: 0 0 20 20;" + "-fx-background-color:rgba(" + color + ");");
			stkColorTag.setVisible(true);
		} else
			stkColorTag.setVisible(false);
		activitiesInfo(item);
		lblTeamMembers.setVisible(item.getTeam() != null);
		if (item.getTeam() != null) {
			lblTeamMembers.setText(String.valueOf(item.getTeam().getMembers().size() + 1));
		}

	}

	private void activitiesInfo(ProjectVO item) {
		List<ActivityVO> lst = new ActivityBO().listAll(new ActivityFilter(item.getId(), item.getUserVO().getIdUser()));
		Integer total = lst.size();
		Integer done = 0;
		for (ActivityVO ac : lst) {
			if (StatusEnum.itsFinalized(ac.getStats()))
				done++;
		}
		lblActivityDoneTotal.setText(done + "/" + total);
	}
}
