package com.octopod.networkplus.messages;

import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MessageOutServerCommand extends NetworkMessage
{
	public MessageOutServerCommand(String command)
	{
		setChannel(StaticChannel.SERVER_DISPATCH);
	}
}
