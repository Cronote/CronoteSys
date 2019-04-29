package com.cronoteSys.controller;

import java.util.ArrayList;
import java.util.HashMap;

public interface ShowEditViewActivityObservableI {

	static ArrayList<ShowEditViewActivityObserverI> listeners = new ArrayList<ShowEditViewActivityObserverI>();

	static void addShowEditViewActivityListener(ShowEditViewActivityObserverI newListener) {
		listeners.add(newListener);
	}
	static void removeShowEditViewActivityListener(ShowEditViewActivityObserverI newListener) {
		listeners.remove(newListener);
	}

	void notifyAllListeners(HashMap<String, Object> hmp);
}
