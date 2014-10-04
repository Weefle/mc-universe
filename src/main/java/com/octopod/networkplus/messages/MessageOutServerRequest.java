package com.octopod.networkplus.messages;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MessageOutServerRequest extends NetworkMessage
{
	public MessageOutServerRequest()
	{
		setChannel(StaticChannel.SERVER_INFO_RETURN);
		setMessage(NetworkPlus.serialize(NetworkPlus.getServer()));
	}
}
