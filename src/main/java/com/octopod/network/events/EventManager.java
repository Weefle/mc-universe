package com.octopod.network.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventManager {
	
	private static EventManager manager = null;
	
	public static EventManager getManager() {
		if(manager != null) {
			return manager;
		} else {
			return (manager = new EventManager());
		}
	}
	
	private List<Object> listeners = Collections.synchronizedList(new ArrayList<Object>());
	
	public List<Object> getListeners() {
		return listeners;
	}
	
	public void registerListener(Object listener) {
		listeners.add(listener);
	}
	
	public void unregisterListener(Object listener) {
		listeners.remove(listener);
	}
	
	public void unregisterAll() {
		listeners.clear();
	}
	
}
