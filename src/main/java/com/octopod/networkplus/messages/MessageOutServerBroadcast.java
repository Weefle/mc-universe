package com.octopod.networkplus.messages;

import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MessageOutServerBroadcast extends NetworkMessage
{
	private String message;

	public MessageOutServerBroadcast(String message)
	{
		this.message = message;
		setMessage(message);
		setChannel(StaticChannel.SERVER_BROADCAST);
	}

	public String getMessage()
	{
		return message;
	}
}
