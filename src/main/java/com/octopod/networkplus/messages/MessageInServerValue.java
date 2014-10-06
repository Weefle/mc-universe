package com.octopod.networkplus.messages;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.StaticChannel;
import com.octopod.networkplus.ServerValue;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MessageInServerValue extends NetworkMessage
{
	ServerValue value;

	public MessageInServerValue(ServerValue value)
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
		return StaticChannel.SERVER_VALUE_RETURN.toString();
	}
}
