package com.octopod.network.events.server;

import com.octopod.network.events.CancellableEvent;

import java.util.HashMap;

public class ServerDiscoveredEvent extends CancellableEvent {

	String server;
    HashMap<String, Object> info;

	public ServerDiscoveredEvent(String sender, HashMap<String, Object> info) {
        server = sender;
		this.info = info;
	}

	public String getServer() {
		return server;
	}

	public HashMap<String, Object> getInfo() {
		return info;
	}

}