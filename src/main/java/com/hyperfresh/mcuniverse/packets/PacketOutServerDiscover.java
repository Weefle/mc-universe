package com.hyperfresh.mcuniverse.packets;

import com.hyperfresh.mcuniverse.UniverseAPI;
import com.hyperfresh.mcuniverse.server.networked.UniverseServer;
import com.hyperfresh.mcuniverse.server.networked.stored.StoredServer;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketOutServerDiscover extends Packet
{
	StoredServer server;

	public PacketOutServerDiscover()
	{
		this.server = new StoredServer(UniverseAPI.getInstance().getServer());
	}

	public PacketOutServerDiscover(UniverseServer server)
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

	public UniverseServer getServer()
	{
		return server;
	}
}
