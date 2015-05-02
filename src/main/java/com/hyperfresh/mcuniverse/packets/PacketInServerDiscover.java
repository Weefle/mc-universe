package com.hyperfresh.mcuniverse.packets;

import com.hyperfresh.mcuniverse.UniverseAPI;
import com.hyperfresh.mcuniverse.server.networked.UniverseServer;
import com.hyperfresh.mcuniverse.server.networked.stored.StoredServer;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Used with broadcast()
 * A server (with SwitchCore loaded) that recieves this packet will return another one with their current information.
 * The difference between this packet and PacketInServerDiscover is that this packet will cause a return.
 */
public class PacketInServerDiscover extends Packet
{
	StoredServer server;

	public PacketInServerDiscover()
	{
		this.server = new StoredServer(UniverseAPI.getInstance().getServer());
	}

	public PacketInServerDiscover(UniverseServer server)
	{
		if(server instanceof StoredServer)
		{
			this.server = (StoredServer)server;
		}
		else
		{
			this.server = new StoredServer(server);
		}
	}

	public StoredServer getServer()
	{
		return server;
	}
}
