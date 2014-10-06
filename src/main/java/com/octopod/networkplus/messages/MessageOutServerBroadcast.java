package com.octopod.networkplus.messages;

import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Tells the server to broadcast a message to all players.
 */
public class MessageOutServerBroadcast extends NetworkMessage
{
	private String message;

	public MessageOutServerBroadcast(String message)
	{
		this.message = message;
	}

	@Override
	public String[] getMessage()
	{
		return new String[]{message};
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.SERVER_BROADCAST.toString();
	}
}
