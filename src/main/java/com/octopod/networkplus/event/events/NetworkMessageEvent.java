package com.octopod.networkplus.event.events;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.event.CancellableEvent;
import com.octopod.networkplus.event.Event;
import com.octopod.networkplus.exceptions.DeserializationException;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class NetworkMessageEvent extends Event implements CancellableEvent
{
	String server, channel, message;
	String[] parsed;

	public NetworkMessageEvent(String server, String channel, String message)
	{
		this.server = server;
		this.channel = channel;
		this.message = message;

		try {
			parsed = NetworkPlus.getSerializer().deserialize(message, String[].class);
		} catch (DeserializationException e) {
			parsed = new String[0];
		}
	}

	public String getServer() {return server;}
	public String getChannel() {return channel;}
	public String getRawMessage() {return message;}
	public String getMessage() {return parsed.length > 0 ? parsed[0] : "";}
	public String[] getParsed() {return parsed;}
	public String arg(int i)
	{
		if(i < 0 || i > parsed.length - 1) return null;
		return parsed[i];
	}

	boolean cancelled = false;

	@Override
	public boolean isCancelled()
	{
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel)
	{
		cancelled = cancel;
	}
}
