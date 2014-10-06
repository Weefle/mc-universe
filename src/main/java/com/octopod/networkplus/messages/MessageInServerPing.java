package com.octopod.networkplus.messages;

import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MessageInServerPing extends NetworkMessage
{
	int id;

	public MessageInServerPing(int id)
	{
		this.id = id;
	}

	public MessageInServerPing()
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
		return StaticChannel.SERVER_PING_MESSAGE.toString();
	}
}
