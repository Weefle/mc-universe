package com.octopod.networkplus.messages;

import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * This will tell a server to run a command (as the console)
 */
public class MessageOutServerCommand extends NetworkMessage
{
	String command;

	public MessageOutServerCommand(String command)
	{
		this.command = command;
	}

	@Override
	public String[] getMessage()
	{
		return new String[]{command};
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.SERVER_DISPATCH.toString();
	}
}
