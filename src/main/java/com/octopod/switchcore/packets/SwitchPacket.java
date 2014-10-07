package com.octopod.switchcore.packets;

import com.octopod.switchcore.SwitchCore;

import java.io.Serializable;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class SwitchPacket implements Serializable
{
	/**
	 * Gets the channel this message will be sent out on.
	 *
	 * @return the channel to send out
	 */
	@Deprecated
	public abstract String getChannelOut();

	/**
	 * Gets the channel the expected return message will be sent out on.
	 *
	 * @return the channel to expect a return from
	 */
	@Deprecated
	public String getChannelIn() {return null;}

	/**
	 * Gets the message (arguments) to send.
	 * The first element of the array will the the main message.
	 *
	 * @return the message
	 */
	@Deprecated
	public abstract String[] getMessage();

	/**
	 * Sends this message to a server.
	 *
	 * @param server the server's identifier
	 */
	public final void send(String server)
	{
		SwitchCore.sendPacket(server, this);
	}

	/**
	 * Sends this message to all servers.
	 */
	public final void broadcast()
	{
		SwitchCore.broadcastPacket(this);
	}
}