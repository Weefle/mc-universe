package com.octopod.network.events.server;

import com.octopod.network.events.Event;

public class ServerClearedEvent extends Event {
	
	String requester, server;
	
	public ServerClearedEvent(String server, String requester) {
		this.requester = requester;
		this.server = server;
	}

    /**
     * Gets which server sent the clear request.
     * @return
     */
	public String getRequester() {
		return requester;
	}
	
	public String getClearedServer() {
		return server;
	}
	
}
