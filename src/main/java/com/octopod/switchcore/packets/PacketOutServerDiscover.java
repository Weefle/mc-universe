package com.octopod.switchcore.packets;

import com.octopod.switchcore.CachedServer;
import com.octopod.switchcore.Server;
import com.octopod.switchcore.SwitchCore;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketOutServerDiscover extends SwitchPacket
{
	CachedServer server;

	public PacketOutServerDiscover()
	{
		this.server = new CachedServer(SwitchCore.getServer());
	}

	public PacketOutServerDiscover(Server server)
	{
		if(server instanceof CachedServer)
		{
			this.server = (CachedServer)server;
		}
		else
		{
			this.server = new CachedServer(server);
		}
	}

	public Server getServer()
	{
		return server;
	}
}
