package com.octopod.networkplus.requests;

import com.octopod.networkplus.network.NetworkChannel;
import com.octopod.networkplus.network.NetworkMessage;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ServerPingRequest extends NetworkMessage
{
	public ServerPingRequest()
	{
		setChannel(NetworkChannel.SERVER_PING_REQUEST);
		setReturnChannel(NetworkChannel.SERVER_PING_RETURN);
	}
}
