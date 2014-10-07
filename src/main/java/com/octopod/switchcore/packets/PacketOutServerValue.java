package com.octopod.switchcore.packets;

import com.octopod.switchcore.ServerValue;
import com.octopod.switchcore.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketOutServerValue extends SwitchPacket
{
	ServerValue value;

	public PacketOutServerValue(ServerValue value)
	{
		this.value = value;
	}

	public ServerValue getValue()
	{
		return value;
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
