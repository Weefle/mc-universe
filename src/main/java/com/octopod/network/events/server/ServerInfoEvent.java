package com.octopod.network.events.server;

import com.octopod.network.ServerInfo;
import com.octopod.network.events.Event;

public class ServerInfoEvent extends Event implements Comparable<ServerInfoEvent> {
	
	ServerInfo serverInfo;
	String sender;
	long ping;
	
	public ServerInfoEvent(String server, String encoded, long ping) {
		serverInfo = ServerInfo.decode(encoded, server);
		sender = server;
		this.ping = ping;
	}
	
	public long getPing() {
		return ping;
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