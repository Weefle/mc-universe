package com.octopod.network.events.server;

import com.octopod.network.ServerInfo;
import com.octopod.network.events.Event;

public class ServerInfoEvent extends Event implements Comparable<ServerInfoEvent> {

	ServerInfo serverInfo;
	String sender;

	public ServerInfoEvent(String server, String encoded) {
		serverInfo = ServerInfo.decode(encoded, server);
		sender = server;
	}

	public String getSender() {
		return sender;
	}

	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	@Override
	public int compareTo(ServerInfoEvent other) {
		return sender.compareTo(other.getSender());
	}

}