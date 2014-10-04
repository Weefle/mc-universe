package com.octopod.networkplus.messages;

import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MessageOutServerPing extends NetworkMessage
{
	public MessageOutServerPing(int id)
	{
		setChannel(StaticChannel.SERVER_PING_REQUEST);
		setMessage("" + id);
		setReturnChannel(StaticChannel.SERVER_PING_MESSAGE);
	}

	public MessageOutServerPing()
	{
		this(0);
	}
}
