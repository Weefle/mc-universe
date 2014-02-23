package com.octopod.network.events.server;

import com.octopod.network.events.Event;

public class ServerUncacheEvent extends Event {
	
	String sender, server;
	
	public ServerUncacheEvent(String server, String sender) {
		this.sender = sender;
		this.server = server;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getUncachedServer() {
		return server;
	}
	
}
