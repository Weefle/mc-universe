package com.octopod.switchcore.packets;

import com.octopod.switchcore.SwitchCore;
import com.octopod.switchcore.StaticChannel;
import com.octopod.switchcore.ServerValue;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketInServerValue extends SwitchPacket
{
	ServerValue value;
	Object object;

	public PacketInServerValue(ServerValue value)
	{
		this.value = value;
		this.object = SwitchCore.getServer().getValue(value);
	}

	public ServerValue getType()
	{
		return value;
	}

	public Object getValue()
	{
		return object;
	}

	@Override
	public String[] getMessage()
	{
//		String encoded = SwitchCore.serializePacket(SwitchCore.getServer().getValue(value));
//		return new String[]{value.name(), encoded};
		return new String[0];
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.IN_SERVER_VALUE_REQUEST.toString();
	}
}
