package com.cronoteSys.model.vo;

import javafx.scene.paint.Color;

public enum StatusEnum {

	NOT_STARTED("Não iniciado", Color.LIGHTGRAY), NORMAL_IN_PROGRESS("Em progresso", Color.BLUE),
	NORMAL_PAUSED("Pausado", Color.YELLOW), NORMAL_FINALIZED("Finalizado", Color.GREEN),
	BROKEN_IN_PROGRESS("Em progresso", Color.RED), BROKEN_PAUSED("Pausado", Color.RED),
	BROKEN_FINALIZED("Finalizado", Color.DARKRED);

	private String description;
	private Color color;

	private StatusEnum(String description, Color color) {
		this.description = description;
		this.color = color;
	}

	public String getDescription() {
		return description;
	}

	public Color getColor() {
		return color;
	}
}
