package com.hyperfresh.mcuniverse.event.events;

import com.hyperfresh.mcuniverse.event.CancellableEvent;
import com.hyperfresh.mcuniverse.event.Event;
import com.hyperfresh.mcuniverse.packets.Packet;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class NetworkPacketInEvent extends Event implements CancellableEvent
{
	private String server;
	private Packet packet;

	private boolean cancelled = false;

	public NetworkPacketInEvent(String server, Packet packet)
	{
		this.server = server;
		this.packet = packet;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public void setPacket(Packet packet)
	{
		this.packet = packet;
	}

	public String getServer()
	{
		return server;
	}

	public Packet getPacket()
	{
		return packet;
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
