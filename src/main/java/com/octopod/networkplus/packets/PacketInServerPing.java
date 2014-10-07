package com.octopod.networkplus.packets;

import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketInServerPing extends NetworkPacket
{
	int id;

	public PacketInServerPing(int id)
	{
		this.id = id;
	}

	public PacketInServerPing()
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
		return StaticChannel.IN_SERVER_PING.toString();
	}
}
