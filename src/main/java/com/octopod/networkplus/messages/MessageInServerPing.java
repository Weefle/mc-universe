package com.octopod.networkplus.messages;

import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MessageInServerPing extends NetworkMessage
{
	public MessageInServerPing(int id)
	{
		setChannel(StaticChannel.SERVER_PING_MESSAGE);
		setMessage("" + id);
	}

	public MessageInServerPing()
	{
		this(0);
	}
}
