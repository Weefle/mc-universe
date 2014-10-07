package com.octopod.switchcore.packets;

import com.octopod.switchcore.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketInServerPing extends SwitchPacket
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

	public int getPingID()
	{
		return id;
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
