package com.cronoteSys.controller;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ShowEditViewActivityObservable {

	public interface ShowEditViewActivityI {
		void showEditViewActivity(HashMap<String, Object> hmp);
	}

	private static ArrayList<ShowEditViewActivityI> listeners = new ArrayList<ShowEditViewActivityI>();

	public static void addShowEditViewActivityListener(ShowEditViewActivityI newListener) {
		listeners.add(newListener);
	}

	protected void notifyAllListeners(HashMap<String, Object> hmp) {
		for (ShowEditViewActivityI l : listeners) {
			l.showEditViewActivity(hmp);
		}
	}
}
