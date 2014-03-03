package com.octopod.network.events.player;

import com.octopod.network.events.Event;

public class NetworkPlayerRedirectEvent extends Event {

	String player, from, server;

	public NetworkPlayerRedirectEvent(String player, String from, String server) {
		this.player = player;
        this.from = from;
		this.server = server;
	}

	public String getPlayer() {
		return player;
	}

	public String getServer() {
		return server;
	}

    public String getFromServer() {
        return from;
    }

}
