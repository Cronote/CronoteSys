package com.cronoteSys.controller.components.listcell;

import com.cronoteSys.model.interfaces.ThreatingUser;
import com.cronoteSys.model.vo.UserVO;
import com.cronoteSys.model.vo.relation.side.TeamMember;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TeamMemberCellController extends ListCell<ThreatingUser> {

	private BorderPane initialPnl;
	private HBox rootPane;
	private Label lblUserInitial;
	private Label lblUsername;
	private Label lblUserEmail;
	private Label lblInviteStatus;
	private Tooltip ttpUserEmail;
	private ThreatingUser item;

	public TeamMemberCellController() {
		lblUserInitial = new Label();
		initialPnl = new BorderPane(lblUserInitial);
		initialPnl.setStyle("-fx-background-color:grey;-fx-background-radius:30;");
		initialPnl.setMinWidth(60.0);
		initialPnl.setPrefWidth(60.0);
		lblUserEmail = new Label();
		lblUsername = new Label();
		lblInviteStatus = new Label();
		VBox vbox1 = new VBox(5);
		VBox vbox2 = new VBox(5);
		vbox2.getChildren().addAll(lblUserEmail, lblInviteStatus);
		vbox1.setSpacing(1);
		vbox2.setSpacing(10);
		vbox1.getChildren().addAll(lblUsername, vbox2);
		ttpUserEmail = new Tooltip();
		Tooltip.install(lblUserEmail, ttpUserEmail);
		rootPane = new HBox(7.0);
		rootPane.setPadding(new Insets(10));
		rootPane.getChildren().addAll(initialPnl, vbox1);
		rootPane.getStyleClass().addAll("card", "borders");
		HBox.setHgrow(vbox1, Priority.ALWAYS);

		lblUserInitial.setTextOverrun(OverrunStyle.CLIP);
		lblUserInitial.setFont(new Font("system bold", 22));
		lblUsername.setFont(new Font(16));
		ttpUserEmail.setFont(new Font(17));
		lblUserEmail.setFont(new Font(12));
		lblInviteStatus.setFont(new Font("system bold", 12));
		lblUserInitial.setTextFill(Color.WHITE);
		lblUsername.setTextFill(Color.WHITE);
		lblUserEmail.setTextFill(Color.WHITE);
		lblInviteStatus.setTextFill(Color.WHITE);

		lblUsername.needsLayoutProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				Tooltip tp = new Tooltip("");
				String originalString = lblUsername.getText();
				Text textNode = (Text) lblUsername.lookup(".text");
				String actualString = textNode.getText();
				boolean clipped = !actualString.isEmpty() && !originalString.equals(actualString);
				if (clipped) {
					tp.setText(originalString);
					Tooltip.install(lblUsername, tp);
				} else {
					Tooltip.uninstall(lblUsername, tp);
				}
			}
		});
	}

	public TeamMemberCellController(Double listViewWidth, boolean fill) {
		this();
		if (fill) {
			rootPane.setPrefWidth(listViewWidth * 0.99);
			Double moveLeft = (rootPane.getPrefWidth() * 0.025) * -1;
			rootPane.setTranslateX(moveLeft);
		} else
			rootPane.setPrefWidth(listViewWidth * 0.8);
	}

	@Override
	protected void updateItem(ThreatingUser item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null || !empty) {
			this.item = item;
			loadInfos(item);
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			setGraphic(rootPane);

		} else {
			setText(null);
			setGraphic(null);
		}
		setStyle("-fx-background-color:transparent;");
	}

	private void loadInfos(ThreatingUser item) {
		UserVO u = null;
		if (item instanceof TeamMember) {
			TeamMember tm = ((TeamMember) item);
			u = tm.getUser();

			if (tm.getExpiresAt() == null) {
				lblInviteStatus.setText("Membro");
			} else {
				lblInviteStatus.setText("Convite enviado");

			}

		} else {
			u = ((UserVO) item);
		}
		if (u == null)
			return;
		lblUserEmail.setText(u.getLogin().getEmail());
		ttpUserEmail.setText(u.getLogin().getEmail());
		String[] userNames = u.getCompleteName().split(" ");
		String name = userNames.length > 1 ? userNames[0] + " " + userNames[(userNames.length - 1)] : userNames[0];
		String initials = "";
		for (String string : userNames) {
			initials += string.substring(0, 1).toUpperCase();
		}
		lblUserInitial.setText(initials.replaceAll(" ", ""));
		double textSize = initials.length();
		textSize = (1 - (textSize * 10 / 100.0)) * 25.0;
		lblUserInitial.setFont(new Font(textSize));
		lblUsername.setText(name);

	}

	@Override
	public void updateSelected(boolean selected) {
		super.updateSelected(selected);
		if (selected) {
			rootPane.getStyleClass().addAll("cardSelected");
		} else
			rootPane.getStyleClass().removeAll("cardSelected");

	}
}
