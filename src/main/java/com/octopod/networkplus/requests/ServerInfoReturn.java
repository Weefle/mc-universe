package com.octopod.networkplus.requests;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.network.NetworkChannel;
import com.octopod.networkplus.network.NetworkMessage;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ServerInfoReturn extends NetworkMessage
{
	public ServerInfoReturn()
	{
		setChannel(NetworkChannel.SERVER_INFO_RETURN);
		setMessage(NetworkPlus.getServerInfo().encode());
	}
}
