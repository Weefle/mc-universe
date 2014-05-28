package com.octopod.network.events.server;

import com.octopod.network.ServerFlags;
import com.octopod.network.events.Event;

/**
 * Runs after the ServerInfo was cached.
 */
public class PostServerFlagsReceivedEvent extends Event {

	String server;
    ServerFlags serverInfo;

	public PostServerFlagsReceivedEvent(String server, ServerFlags serverInfo) {
		this.server = server;
        this.serverInfo = serverInfo;
	}

	public String getServer() {
		return server;
	}

	public ServerFlags getServerInfo() {
		return serverInfo;
	}

}
