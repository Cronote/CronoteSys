package com.cronoteSys.observer;

import java.util.ArrayList;
import java.util.HashMap;

public interface ShowEditViewActivityObservableI {

	static ArrayList<ShowEditViewActivityObserverI> listeners = new ArrayList<ShowEditViewActivityObserverI>();

	static void addShowEditViewActivityListener(ShowEditViewActivityObserverI newListener) {
		listeners.clear();
		listeners.add(newListener);
	}
	void notifyAllListeners(HashMap<String, Object> hmp);
}
