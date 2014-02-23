package com.octopod.network.listener;

import java.io.UnsupportedEncodingException;

import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;

import com.octopod.network.NetworkConfig;
import com.octopod.network.events.EventEmitter;

public class LilyPadListeners {

	@EventListener
	public void messageReceived(MessageEvent event) {

		NetworkConfig config = NetworkConfig.getConfig();
		String channel = event.getChannel(), username, message;
		
		if(!channel.startsWith(config.getRequestPrefix())) {
			return;
		}
		
		try {
			username = event.getSender();
			message = event.getMessageAsString();
		} catch (UnsupportedEncodingException e) {
			return;
		}

		EventEmitter.getEmitter().triggerEvent(new com.octopod.network.events.relays.MessageEvent(username, channel, message));
		
	}
	
}
