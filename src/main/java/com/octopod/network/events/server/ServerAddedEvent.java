package com.octopod.network.events.server;

import com.octopod.network.ServerInfo;
import com.octopod.network.events.Event;

/**
 * Runs after the ServerInfo was cached.
 */
public class ServerAddedEvent extends Event {
	
	String server;
    ServerInfo serverInfo;
	
	public ServerAddedEvent(ServerInfo serverInfo) {
		this.server = serverInfo.getUsername();
        this.serverInfo = serverInfo;
	}
	
	public String getServer() {
		return server;
	}
	
	public ServerInfo getServerInfo() {
		return serverInfo;
	}
	
}
