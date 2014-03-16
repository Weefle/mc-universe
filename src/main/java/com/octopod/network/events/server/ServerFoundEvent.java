package com.octopod.network.events.server;

import com.octopod.network.ServerInfo;
import com.octopod.network.events.CancellableEvent;
import com.octopod.network.events.Event;

public class ServerFoundEvent extends CancellableEvent implements Comparable<ServerFoundEvent> {

	ServerInfo serverInfo;
	String server;

	public ServerFoundEvent(ServerInfo info) {
		serverInfo = info;
	}

	public String getServer() {
		return serverInfo.getUsername();
	}

	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	@Override
	public int compareTo(ServerFoundEvent other) {
		return server.compareTo(other.getServer());
	}

}