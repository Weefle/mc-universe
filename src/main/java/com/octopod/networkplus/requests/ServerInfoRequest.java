package com.octopod.networkplus.requests;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.network.NetworkChannel;
import com.octopod.networkplus.network.NetworkMessage;
import com.octopod.networkplus.ServerInfo;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ServerInfoRequest extends NetworkMessage
{
	public ServerInfoRequest()
	{
		ServerInfo info = NetworkPlus.getDatabase().getServerInfo();
		setChannel(NetworkChannel.SERVER_INFO_REQUEST);
		setMessage(NetworkPlus.getSerializer().encode(info));
		setReturnChannel(NetworkChannel.SERVER_INFO_RETURN);
	}
}
