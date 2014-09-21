package com.octopod.networkplus.requests;

import com.octopod.networkplus.network.NetworkChannel;
import com.octopod.networkplus.network.NetworkMessage;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ServerOnlineMessage extends NetworkMessage
{
	public ServerOnlineMessage()
	{
		setChannel(NetworkChannel.SERVER_ONLINE);
	}
}
