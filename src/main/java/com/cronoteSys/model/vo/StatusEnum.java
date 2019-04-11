package com.cronoteSys.model.vo;


public enum StatusEnum {

	NOT_STARTED("Não iniciado", "#D3D3D3"), NORMAL_IN_PROGRESS("Em progresso", "#0000FF"),
	NORMAL_PAUSED("Pausado", "#FFFF00"), NORMAL_FINALIZED("Finalizado", "#008000"),
	BROKEN_IN_PROGRESS("Em progresso", "#FF0000"), BROKEN_PAUSED("Pausado", "#FF0000"),
	BROKEN_FINALIZED("Finalizado", "#8B0000");

	private String description;
	private String hexColor;

	private StatusEnum(String description, String hexColor) {
		this.description = description;
		this.hexColor = hexColor;
	}

	public String getDescription() {
		return description;
	}

	public String getHexColor() {
		return hexColor;
	}
}
