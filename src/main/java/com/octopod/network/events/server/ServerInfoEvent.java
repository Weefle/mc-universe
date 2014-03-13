package com.octopod.network.events.server;

import com.octopod.network.ServerInfo;
import com.octopod.network.events.Event;

public class ServerInfoEvent extends Event implements Comparable<ServerInfoEvent> {

	ServerInfo serverInfo;
	String sender;

	public ServerInfoEvent(ServerInfo info) {
		serverInfo = info;
	}

	public String getSender() {
		return serverInfo.getUsername();
	}

	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	@Override
	public int compareTo(ServerInfoEvent other) {
		return sender.compareTo(other.getSender());
	}

}