package com.octopod.networkplus.packets;

import com.octopod.networkplus.NetworkPlus;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class NetworkPacket
{
	/**
	 * Gets the channel this message will be sent out on.
	 *
	 * @return the channel to send out
	 */
	public abstract String getChannelOut();

	/**
	 * Gets the channel the expected return message will be sent out on.
	 *
	 * @return the channel to expect a return from
	 */
	public String getChannelIn() {return null;}

	/**
	 * Gets the message (arguments) to send.
	 * The first element of the array will the the main message.
	 *
	 * @return the message
	 */
	public abstract String[] getMessage();

	/**
	 * Sends this message to a server.
	 *
	 * @param server the server's identifier
	 */
	public final void send(String server)
	{
		NetworkPlus.sendPacket(server, this);
	}

	/**
	 * Sends this message to all servers.
	 */
	public final void broadcast()
	{
		NetworkPlus.broadcastPacket(this);
	}

	public final String serialize()
	{
		return NetworkPlus.serialize(getMessage());
	}
}