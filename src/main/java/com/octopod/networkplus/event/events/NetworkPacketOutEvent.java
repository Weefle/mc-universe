package com.octopod.networkplus.event.events;

import com.octopod.networkplus.event.CancellableEvent;
import com.octopod.networkplus.event.Event;
import com.octopod.networkplus.packets.NetworkPacket;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class NetworkPacketOutEvent extends Event implements CancellableEvent
{
	private String server;
	private NetworkPacket message;

	private boolean cancelled = false;

	public NetworkPacketOutEvent(String server, NetworkPacket message)
	{
		this.server = server;
		this.message = message;
	}

	public NetworkPacketOutEvent(NetworkPacket message)
	{
		this.server = null;
		this.message = message;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public void setMessage(NetworkPacket message)
	{
		this.message = message;
	}

	public String getServer()
	{
		return server;
	}

	public NetworkPacket getMessage()
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
