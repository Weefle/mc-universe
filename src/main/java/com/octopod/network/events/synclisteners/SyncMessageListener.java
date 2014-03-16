package com.octopod.network.events.synclisteners;

import com.octopod.network.events.EventHandler;
import com.octopod.network.events.EventListener;
import com.octopod.network.events.SynchronizedListener;
import com.octopod.network.events.relays.MessageEvent;

public class SyncMessageListener extends SynchronizedListener<MessageEvent> {

	public SyncMessageListener(EventListener<MessageEvent> listener) {
		super(listener);
	}

	@Override
	@EventHandler(async = true)
	public void executionListener(MessageEvent event) {
		execute(event);
	}

}
