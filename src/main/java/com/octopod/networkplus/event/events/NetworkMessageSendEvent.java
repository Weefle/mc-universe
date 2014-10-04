package com.octopod.networkplus.event.events;

import com.octopod.networkplus.event.CancellableEvent;
import com.octopod.networkplus.event.Event;
import com.octopod.networkplus.messages.NetworkMessage;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class NetworkMessageSendEvent extends Event implements CancellableEvent
{
	private String server;
	private NetworkMessage message;
	private boolean cancelled = false;

	public NetworkMessageSendEvent(String server, NetworkMessage message)
	{
		this.server = server;
		this.message = message;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public void setMessage(NetworkMessage message)
	{
		this.message = message;
	}

	public String getServer()
	{
		return server;
	}

	public NetworkMessage getMessage()
	{
		return message;
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
