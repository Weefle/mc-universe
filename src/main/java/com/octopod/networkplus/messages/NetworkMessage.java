package com.octopod.networkplus.messages;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.StaticChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class NetworkMessage
{
	private StaticChannel channel = null;
	private StaticChannel retChannel = null;
	private String message = "";

	protected void setChannel(StaticChannel channel) {this.channel = channel;}
	protected void setReturnChannel(StaticChannel retChannel) {this.retChannel = retChannel;}
	protected void setMessage(String... arguments)
	{
		this.message = NetworkPlus.getSerializer().serialize(arguments);
	}

	public String getChannel() {return channel == null ? null : channel.toString();}
	public String getReturnChannel() {return retChannel == null ? null : retChannel.toString();}
	public String getMessage() {return message;}

	public void send(String serverID)
	{
		NetworkPlus.getConnection().sendNetworkMessage(serverID, this);
	}

	public void broadcast()
	{
		NetworkPlus.getConnection().broadcastNetworkMessage(this);
	}
}