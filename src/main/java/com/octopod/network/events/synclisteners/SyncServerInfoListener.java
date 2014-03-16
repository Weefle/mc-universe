package com.octopod.network.events.synclisteners;

import com.octopod.network.events.EventHandler;
import com.octopod.network.events.EventListener;
import com.octopod.network.events.SynchronizedListener;
import com.octopod.network.events.server.ServerFoundEvent;

public class SyncServerInfoListener extends SynchronizedListener<ServerFoundEvent> {

	public SyncServerInfoListener(EventListener<ServerFoundEvent> listener) {
		super(listener);
	}

	@Override
	@EventHandler(async = true)
	public void executionListener(ServerFoundEvent event) {
		execute(event);
	}

}
