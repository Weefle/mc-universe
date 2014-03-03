package com.octopod.network.events.synclisteners;

import com.octopod.network.NetworkConfig;
import com.octopod.network.events.EventHandler;
import com.octopod.network.events.SynchronizedListener;
import com.octopod.network.events.server.ServerInfoEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SyncServerInfoListener extends SynchronizedListener<ServerInfoEvent> {

	public SyncServerInfoListener(EventCallback<ServerInfoEvent> callback) {
		super(callback);
	}

	@Override
	@EventHandler(runAsync = true)
	public void executionListener(ServerInfoEvent event) {
		execute(event);
	}

	public static List<ServerInfoEvent> waitForExecutions(int expectedExecutions) {
		return collectExecutionsFrom(expectedExecutions, null);
	}

	public static List<ServerInfoEvent> collectExecutionsFrom(int expectedExecutions, final List<String> filter) {
		
		final List<ServerInfoEvent> events = Collections.synchronizedList(new ArrayList<ServerInfoEvent>());

		EventCallback<ServerInfoEvent> callback = new EventCallback<ServerInfoEvent>() 
		{
			@Override
			public boolean onEvent(ServerInfoEvent event) {
				if(filter == null || filter.contains(event.getSender())) {
					events.add(event);
					return true;					
				}
				return false;
			}
		};

		SynchronizedListener<ServerInfoEvent> listener = new SyncServerInfoListener(callback).register();

		listener.waitFor(expectedExecutions, NetworkConfig.getRequestTimeout()).unregister();

		return events;
				
	}

}
