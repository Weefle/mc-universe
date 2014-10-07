package com.octopod.networkplus.packets;

import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketOutServerPing extends NetworkPacket
{
	/**
	 * The id of the ping, to tell apart different pings in the case of multiple pinging.
	 */
	int id;

	public PacketOutServerPing(int id)
	{
		this.id = id;
	}

	public PacketOutServerPing()
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
		return StaticChannel.OUT_SERVER_PING.toString();
	}

	@Override
	public String getChannelIn()
	{
		return StaticChannel.IN_SERVER_PING.toString();
	}
}
