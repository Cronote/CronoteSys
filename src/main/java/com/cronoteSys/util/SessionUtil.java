package com.cronoteSys.util;

import java.util.HashMap;

import com.google.inject.Injector;

public class SessionUtil {

	private static HashMap<String, Object> session = new HashMap<String, Object>();

	private static Injector injector;

	public static HashMap<String, Object> getSession() {
		return session;
	}
	
	public static void clearSession() {
		session.clear();
	}

	public static Injector getInjector() {
		return injector;
	}

	public static void setInjector(Injector injector) {
		SessionUtil.injector = injector;
	}

}
