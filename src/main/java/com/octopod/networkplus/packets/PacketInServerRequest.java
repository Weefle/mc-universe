package com.octopod.networkplus.packets;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketInServerRequest extends NetworkPacket
{
	@Override
	public String[] getMessage()
	{
		return new String[]{NetworkPlus.serialize(NetworkPlus.getServer().toValueMap())};
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.IN_SERVER_DISCOVER.toString();
	}
}
