package com.hyperfresh.mcuniverse.packets;

import java.io.Serializable;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class Packet implements Serializable
{
	/**
	 * Sends this message to a server.
	 *
	 * @param server the server's identifier
	 */
	public final void send(String server)
	{
		com.hyperfresh.mcuniverse.UniverseAPI.getInstance().sendPacket(server, this);
	}

	/**
	 * Sends this message to all servers.
	 */
	public final void broadcast()
	{
		com.hyperfresh.mcuniverse.UniverseAPI.getInstance().broadcastPacket(this);
	}
}