package com.octopod.networkplus.network;

import com.octopod.networkplus.NetworkPlus;

import java.util.Arrays;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class NetworkMessage
{
	private NetworkChannel channel = null;
	private NetworkChannel retChannel = null;
	private NetworkReturn ret = null;
	private String message = "";

	public void setChannel(NetworkChannel channel) {this.channel = channel;}
	public void setReturnChannel(NetworkChannel retChannel) {this.retChannel = retChannel;}
	public void setMessage(String... arguments)
	{
		this.message = NetworkPlus.getInstance().getSerializer().encode(Arrays.asList(arguments));
	}

	public String getChannel() {return channel.toString();}
	public String getReturnChannel() {return retChannel.toString();}
	public String getMessage() {return message;}

	public void send(String serverID)
	{
		NetworkPlus.getInstance().getConnection().sendNetworkRequest(serverID, this, ret);
	}
}