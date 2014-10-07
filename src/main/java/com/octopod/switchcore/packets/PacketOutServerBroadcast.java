package com.octopod.switchcore.packets;

import com.octopod.switchcore.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Tells the server to broadcast a message to all players.
 */
public class PacketOutServerBroadcast extends SwitchPacket
{
	String message;

	public PacketOutServerBroadcast(String message)
	{
		this.message = message;
	}

	public String getBroadcastMessage()
	{
		return message;
	}

	@Override
	public String[] getMessage()
	{
		return new String[]{message};
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.OUT_SERVER_BROADCAST.toString();
	}
}
