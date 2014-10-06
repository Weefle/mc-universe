package com.octopod.networkplus.messages;

import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MessageOutServerPing extends NetworkMessage
{
	/**
	 * The id of the ping, to tell apart different pings in the case of multiple pinging.
	 */
	int id;

	public MessageOutServerPing(int id)
	{
		this.id = id;
	}

	public MessageOutServerPing()
	{
		this.id = 0;
	}

	@Override
	public String[] getMessage()
	{
		return new String[]{Integer.toString(id)};
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.SERVER_PING_REQUEST.toString();
	}

	@Override
	public String getChannelIn()
	{
		return StaticChannel.SERVER_PING_MESSAGE.toString();
	}
}
