package com.octopod.networkplus.messages;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MessageInServerRequest extends NetworkMessage
{
	@Override
	public String[] getMessage()
	{
		return new String[]{NetworkPlus.serialize(NetworkPlus.getServer().toValueMap())};
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.SERVER_INFO_RETURN.toString();
	}
}
