package com.octopod.networkplus.packets;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Used with broadcast()
 * A server (with NetworkPlus loaded) that recieves this packet will return another one with their current information.
 */
public class PacketOutServerDiscover extends NetworkPacket
{
	@Override
	public String[] getMessage()
	{
		return new String[]{NetworkPlus.serialize(NetworkPlus.getServer().toValueMap())};
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.OUT_SERVER_DISCOVER.toString();
	}

	@Override
	public String getChannelIn()
	{
		return StaticChannel.IN_SERVER_DISCOVER.toString();
	}
}
