package com.octopod.networkplus.messages;

import com.octopod.networkplus.StaticChannel;
import com.octopod.networkplus.ServerValue;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MessageOutServerValue extends NetworkMessage
{
	public MessageOutServerValue(ServerValue value)
	{
		setChannel(StaticChannel.SERVER_VALUE_REQUEST);
		setMessage(value.name());
		setReturnChannel(StaticChannel.SERVER_VALUE_RETURN);
	}
}
