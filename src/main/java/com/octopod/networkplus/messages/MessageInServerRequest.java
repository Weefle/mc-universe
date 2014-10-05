package com.octopod.networkplus.messages;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MessageInServerRequest extends NetworkMessage
{
	public MessageInServerRequest()
	{
		setChannel(StaticChannel.SERVER_INFO_RETURN);
		setMessage(NetworkPlus.serialize(NetworkPlus.getServer().toValueMap()));
	}
}
