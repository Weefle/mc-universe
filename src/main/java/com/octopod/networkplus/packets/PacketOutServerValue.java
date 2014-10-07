package com.octopod.networkplus.packets;

import com.octopod.networkplus.ServerValue;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketOutServerValue extends NetworkPacket
{
	ServerValue value;

	public PacketOutServerValue(ServerValue value)
	{
		this.value = value;
	}

	@Override
	public String[] getMessage()
	{
		return new String[]{value.name()};
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.OUT_SERVER_VALUE_REQUEST.toString();
	}

	@Override
	public String getChannelIn()
	{
		return StaticChannel.IN_SERVER_VALUE_REQUEST.toString();
	}
}
