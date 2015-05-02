package com.hyperfresh.mcuniverse.event.events;

import com.hyperfresh.mcuniverse.event.CancellableEvent;
import com.hyperfresh.mcuniverse.event.Event;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class NetworkMessageOutEvent extends Event implements CancellableEvent
{
	private String server;
	private String channel;
	private String message;

	private boolean cancelled = false;

	public NetworkMessageOutEvent(String server, String channel, String message)
	{
		this.server = server;
		this.channel = channel;
		this.message = message;
	}

	public NetworkMessageOutEvent(String channel, String message)
	{
		this.server = null;
		this.channel = channel;
		this.message = message;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public String getServer()
	{
		return server;
	}

	public String getMessage()
	{
		return message;
	}

	public String getChannel()
	{
		return channel;
	}

	@Override
	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}

	@Override
	public boolean isCancelled()
	{
		return cancelled;
	}
}
