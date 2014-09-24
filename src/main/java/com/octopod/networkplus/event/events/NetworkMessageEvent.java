package com.octopod.networkplus.event.events;

import com.octopod.networkplus.NetworkDecodeException;
import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.event.Cancellable;
import com.octopod.networkplus.event.Event;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class NetworkMessageEvent extends Event implements Cancellable
{
	String serverID, channel, message;
	String[] parsed;

	public NetworkMessageEvent(String serverID, String channel, String message)
	{
		this.serverID = serverID;
		this.channel = channel;
		this.message = message;

		try {
			@SuppressWarnings("unchecked")
			List<String> decoded = NetworkPlus.getSerializer().decode(message, List.class);
			parsed = decoded.toArray(new String[decoded.size()]);
		} catch (NetworkDecodeException e) {
			parsed = new String[0];
		}
	}

	public String getServerID() {return serverID;}
	public String getChannel() {return channel;}
	public String getRawMessage() {return message;}
	public String[] getParsed() {return parsed;}

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
