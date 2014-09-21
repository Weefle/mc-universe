package com.octopod.networkplus.event.events;

import com.octopod.networkplus.event.Cancellable;
import com.octopod.networkplus.event.Event;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class NetworkMessageEvent extends Event implements Cancellable
{
	String serverID, channel, message;

	public NetworkMessageEvent(String serverID, String channel, String message)
	{
		this.serverID = serverID;
		this.channel = channel;
		this.message = message;
	}

	public String getServerID() {return serverID;}
	public String getChannel() {return channel;}
	public String getMessage() {return message;}

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
