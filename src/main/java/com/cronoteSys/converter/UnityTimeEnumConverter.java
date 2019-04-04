package com.cronoteSys.converter;

import com.cronoteSys.model.vo.UnityTimeEnum;

import javafx.util.StringConverter;

public class UnityTimeEnumConverter extends StringConverter<UnityTimeEnum> {

	@Override
	public String toString(UnityTimeEnum object) {
		return object != null ? object.getDescription() : "";
	}

	@Override
	public UnityTimeEnum fromString(String string) {
		for (UnityTimeEnum unityTimeEnum : UnityTimeEnum.values()) {
			if (unityTimeEnum.getDescription().equals(string))
				return unityTimeEnum;
		}
		return null;
	}

}
