package com.octopod.networkplus.requests;

import com.octopod.networkplus.network.NetworkChannel;
import com.octopod.networkplus.network.NetworkMessage;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ServerPingReturn extends NetworkMessage
{
	public ServerPingReturn()
	{
		setChannel(NetworkChannel.SERVER_PING_RETURN);
	}
}
