package com.octopod.network.events.synclisteners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.octopod.network.NetworkConfig;
import com.octopod.network.events.EventHandler;
import com.octopod.network.events.SynchronizedListener;
import com.octopod.network.events.server.ServerInfoEvent;

public class SyncServerInfoListener extends
		SynchronizedListener<ServerInfoEvent> {

	public SyncServerInfoListener(EventCallback<ServerInfoEvent> callback) {
		super(callback);
	}

	@Override
	@EventHandler(runAsync = true)
	public void executionListener(ServerInfoEvent event) {
		execute(event);
	}
	
	public static List<ServerInfoEvent> waitForExecutions(int expectedExecutions) {
		return waitForExecutions(expectedExecutions, null, null);
	}
	
	public static List<ServerInfoEvent> waitForExecutions(int expectedExecutions, final List<String> filter) {
		return waitForExecutions(expectedExecutions, filter, null);
	}
	
	public static List<ServerInfoEvent> waitForExecutions(int expectedExecutions, final List<String> filter, Runnable preExecution) {
		
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

		if(preExecution != null) {
			preExecution.run();
		}
		
		listener.waitFor(expectedExecutions, NetworkConfig.getConfig().getRequestTimeout()).unregister();

		return events;
				
	}

}
