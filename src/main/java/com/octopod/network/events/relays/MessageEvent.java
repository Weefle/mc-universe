package com.octopod.network.events.relays;

import com.octopod.network.events.Event;

public class MessageEvent extends Event {

	String username, channel, message;
	
	public MessageEvent(String username, String channel, String message) {
		this.username = username;
		this.channel = channel;
		this.message = message;

	}
	
	public String getSender() {
		return username;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public String getMessage() {
		return message;
	}
	
}
