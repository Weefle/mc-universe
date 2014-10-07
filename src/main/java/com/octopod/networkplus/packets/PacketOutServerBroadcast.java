package com.octopod.networkplus.packets;

import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Tells the server to broadcast a message to all players.
 */
public class PacketOutServerBroadcast extends NetworkPacket
{
	private String message;

	public PacketOutServerBroadcast(String message)
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
		return StaticChannel.OUT_SERVER_BROADCAST.toString();
	}
}
