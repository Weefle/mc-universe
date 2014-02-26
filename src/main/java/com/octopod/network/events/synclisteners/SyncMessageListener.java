package com.octopod.network.events.synclisteners;

import com.octopod.network.events.EventHandler;
import com.octopod.network.events.SynchronizedListener;
import com.octopod.network.events.relays.MessageEvent;

public class SyncMessageListener extends SynchronizedListener<MessageEvent> {

	public SyncMessageListener(EventCallback<MessageEvent> callback) {
		super(callback);
	}

	@Override
	@EventHandler(runAsync = true)
	public void executionListener(MessageEvent event) {
		execute(event);
	}

}
