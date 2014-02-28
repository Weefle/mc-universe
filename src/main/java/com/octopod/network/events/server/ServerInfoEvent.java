package com.octopod.network.events.server;

import com.octopod.network.NetworkPlugin;
import com.octopod.network.events.Event;

public class ServerInfoEvent extends Event implements Comparable<ServerInfoEvent> {

	NetworkPlugin.ServerInfo serverInfo;
	String sender;

	public ServerInfoEvent(String encoded) {
		serverInfo = new NetworkPlugin.ServerInfo(encoded);
	}

	public String getSender() {
		return serverInfo.getUsername();
	}

	public NetworkPlugin.ServerInfo getServerInfo() {
		return serverInfo;
	}

	@Override
	public int compareTo(ServerInfoEvent other) {
		return sender.compareTo(other.getSender());
	}

}