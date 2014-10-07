package com.octopod.networkplus.packets;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.StaticChannel;
import com.octopod.networkplus.ServerValue;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketInServerValue extends NetworkPacket
{
	ServerValue value;

	public PacketInServerValue(ServerValue value)
	{
		this.value = value;
	}

	@Override
	public String[] getMessage()
	{
		String encoded = NetworkPlus.serialize(NetworkPlus.getServer().getValue(value));
		return new String[]{value.name(), encoded};
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.IN_SERVER_VALUE_REQUEST.toString();
	}
}
