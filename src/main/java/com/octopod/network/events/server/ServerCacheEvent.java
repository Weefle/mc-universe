package com.octopod.network.events.server;

import com.octopod.network.events.Event;

public class ServerCacheEvent extends Event {
	
	String sender, server;
	
	public ServerCacheEvent(String server, String sender) {
		this.sender = sender;
		this.server = server;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getCachedServer() {
		return server;
	}
	
}
