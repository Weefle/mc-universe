package com.octopod.networkplus.packets;

import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * This will tell a server to run a command (as the console)
 */
public class PacketOutServerCommand extends NetworkPacket
{
	String command;

	public PacketOutServerCommand(String command)
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
		return StaticChannel.OUT_SERVER_DISPATCH.toString();
	}
}
