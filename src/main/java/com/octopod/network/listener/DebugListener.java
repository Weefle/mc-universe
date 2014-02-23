package com.octopod.network.listener;

import com.octopod.network.Debug;
import com.octopod.network.events.EventHandler;
import com.octopod.network.events.server.ServerInfoEvent;

public class DebugListener {
	
	@EventHandler
	public void serverInfoRecieved(ServerInfoEvent event) {

		Debug.verbose("Recieved ServerInfo from &a" + event.getSender());

	}
	
}
