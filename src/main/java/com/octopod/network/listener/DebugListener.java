package com.octopod.network.listener;

import com.octopod.network.NetworkDebug;
import com.octopod.network.events.EventHandler;
import com.octopod.network.events.server.ServerInfoEvent;

public class DebugListener {

	@EventHandler
	public void serverInfoRecieved(ServerInfoEvent event) {

		NetworkDebug.verbose("Recieved ServerInfo from &a" + event.getSender());

	}

}
