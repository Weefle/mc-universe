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

	protected void setChannel(NetworkChannel channel) {this.channel = channel;}
	protected void setReturnChannel(NetworkChannel retChannel) {this.retChannel = retChannel;}
	protected void setMessage(String... arguments)
	{
		this.message = NetworkPlus.getSerializer().encode(Arrays.asList(arguments));
	}
	public void setReturn(NetworkReturn ret)
	{
		this.ret = ret;
	}

	public String getChannel() {return channel == null ? null : channel.toString();}
	public String getReturnChannel() {return retChannel == null ? null : retChannel.toString();}
	public String getMessage() {return message;}

	public void send(String serverID)
	{
		NetworkPlus.getConnection().sendNetworkRequest(serverID, this, ret);
	}
}