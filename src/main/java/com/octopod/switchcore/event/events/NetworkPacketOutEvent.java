package com.octopod.switchcore.event.events;

import com.octopod.switchcore.event.CancellableEvent;
import com.octopod.switchcore.event.Event;
import com.octopod.switchcore.packets.SwitchPacket;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class NetworkPacketOutEvent extends Event implements CancellableEvent
{
	private String server;
	private SwitchPacket packet;

	private boolean cancelled = false;

	public NetworkPacketOutEvent(String server, SwitchPacket packet)
	{
		this.server = server;
		this.packet = packet;
	}

	public NetworkPacketOutEvent(SwitchPacket packet)
	{
		this.server = null;
		this.packet = packet;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public void setPacket(SwitchPacket packet)
	{
		this.packet = packet;
	}

	public String getServer()
	{
		return server;
	}

	public SwitchPacket getPacket()
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
