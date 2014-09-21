package com.octopod.networkplus.requests;

import com.octopod.networkplus.network.NetworkChannel;
import com.octopod.networkplus.network.NetworkMessage;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ServerOfflineMessage extends NetworkMessage
{
	public ServerOfflineMessage()
	{
		setChannel(NetworkChannel.SERVER_OFFLINE);
	}
}
