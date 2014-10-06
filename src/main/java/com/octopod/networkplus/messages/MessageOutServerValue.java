package com.octopod.networkplus.messages;

import com.octopod.networkplus.ServerValue;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MessageOutServerValue extends NetworkMessage
{
	ServerValue value;

	public MessageOutServerValue(ServerValue value)
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
		return StaticChannel.SERVER_VALUE_REQUEST.toString();
	}

	@Override
	public String getChannelIn()
	{
		return StaticChannel.SERVER_VALUE_RETURN.toString();
	}
}
