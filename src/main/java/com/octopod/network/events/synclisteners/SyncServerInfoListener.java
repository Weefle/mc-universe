package com.octopod.network.events.synclisteners;

import com.octopod.network.events.EventHandler;
import com.octopod.network.events.EventListener;
import com.octopod.network.events.SynchronizedListener;
import com.octopod.network.events.server.ServerInfoEvent;

public class SyncServerInfoListener extends SynchronizedListener<ServerInfoEvent> {

	public SyncServerInfoListener(EventListener<ServerInfoEvent> listener) {
		super(listener);
	}

	@Override
	@EventHandler(runAsync = true)
	public void executionListener(ServerInfoEvent event) {
		execute(event);
	}

}
