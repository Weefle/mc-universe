package com.octopod.network.events.player;

import com.octopod.network.events.Event;

public class NetworkPlayerJoinEvent extends Event {
	
	String player, server;
	
	public NetworkPlayerJoinEvent(String player, String server) {
		this.player = player;
		this.server = server;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public String getServer() {
		return server;
	}

}
