package com.cronoteSys.observer;

import java.util.ArrayList;
import java.util.HashMap;

public interface ShowEditViewActivityObservableI {

	static ArrayList<ShowEditViewActivityObserverI> listeners = new ArrayList<ShowEditViewActivityObserverI>();

	static void addShowEditViewActivityListener(ShowEditViewActivityObserverI newListener) {
		while (listeners.size()>0) {
			listeners.remove(0);
		}
		listeners.add(newListener);
	}
	void notifyAllListeners(HashMap<String, Object> hmp);
}
