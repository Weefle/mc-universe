package com.octopod.network.listener;

import com.octopod.network.NetworkConfig;
import com.octopod.network.events.EventEmitter;
import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;

import java.io.UnsupportedEncodingException;

public class LilyPadListeners {

	@EventListener
	public void messageReceived(MessageEvent event) {

		String channel = event.getChannel(), username, message;

		if(!channel.startsWith(NetworkConfig.getRequestPrefix())) {
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
