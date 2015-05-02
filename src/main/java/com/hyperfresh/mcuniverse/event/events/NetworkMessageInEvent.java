package com.hyperfresh.mcuniverse.event.events;

import com.hyperfresh.mcuniverse.event.CancellableEvent;
import com.hyperfresh.mcuniverse.event.Event;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class NetworkMessageInEvent extends Event implements CancellableEvent
{
	String server, channel, message;

	public NetworkMessageInEvent(String server, String channel, String message)
	{
		this.server = server;
		this.channel = channel;
		this.message = message;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getServer()
	{
		return server;
	}

	public String getChannel()
	{
		return channel;
	}

	public String getMessage()
	{
		return message;
	}

//	public String[] getParsed() {return parsed;}
//	public String arg(int i)
//	{
//		if(i < 0 || i > parsed.length - 1) return null;
//		return parsed[i];
//	}

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
