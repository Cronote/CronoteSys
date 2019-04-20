package com.cronoteSys.util;

import java.util.HashMap;

import com.google.inject.Injector;

public class SessionUtil {

	private static HashMap<String, Object> SESSION = new HashMap<String, Object>();

	private static Injector injector;

	public static HashMap<String, Object> getSESSION() {
		return SESSION;
	}

	public static Injector getInjector() {
		return injector;
	}

	public static void setInjector(Injector injector) {
		SessionUtil.injector = injector;
	}

}
