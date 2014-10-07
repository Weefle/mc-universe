package com.octopod.switchcore.packets;

import com.octopod.switchcore.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * This will tell a server to run a command (as the console)
 */
public class PacketOutServerCommand extends SwitchPacket
{
	String command;

	public PacketOutServerCommand(String command)
	{
		this.command = command;
	}

	public String getCommand()
	{
		return command;
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
