package com.octopod.network.events.relays;

import com.octopod.network.ServerMessage;
import com.octopod.network.events.CancellableEvent;

public class MessageEvent extends CancellableEvent {

	String username, channel;
    ServerMessage message;
	
	public MessageEvent(String username, String channel, ServerMessage message) {
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

    public ServerMessage getServerMessage() {
        return message;
    }

    public String getMessage() {
        return message.toString();
    }

    public String[] getArguments() {
        return message.getArgs();
    }

    boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

}
