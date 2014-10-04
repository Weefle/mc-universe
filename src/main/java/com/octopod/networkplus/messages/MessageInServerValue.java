package com.octopod.networkplus.messages;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.StaticChannel;
import com.octopod.networkplus.ServerValue;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MessageInServerValue extends NetworkMessage
{
	public MessageInServerValue(ServerValue value)
	{
		setChannel(StaticChannel.SERVER_VALUE_RETURN);
		setMessage(value.name(), NetworkPlus.getSerializer().serialize(NetworkPlus.getServer().getValue(value)));
	}
}
