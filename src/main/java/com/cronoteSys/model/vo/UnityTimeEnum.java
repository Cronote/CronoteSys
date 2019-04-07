package com.cronoteSys.model.vo;

public enum UnityTimeEnum {

	MINUTES("MINUTO(S)"), HOURS("HORA(S)");

	private String description;

	private UnityTimeEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
