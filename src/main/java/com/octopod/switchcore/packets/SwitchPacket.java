package com.octopod.switchcore.packets;

import com.octopod.switchcore.SwitchCore;

import java.io.Serializable;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class SwitchPacket implements Serializable
{
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