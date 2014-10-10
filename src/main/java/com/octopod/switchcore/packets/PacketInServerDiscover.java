package com.octopod.switchcore.packets;

import com.octopod.switchcore.CachedServer;
import com.octopod.switchcore.Server;
import com.octopod.switchcore.SwitchCore;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Used with broadcast()
 * A server (with SwitchCore loaded) that recieves this packet will return another one with their current information.
 * The difference between this packet and PacketInServerDiscover is that this packet will cause a return.
 */
public class PacketInServerDiscover extends SwitchPacket
{
	CachedServer server;

	public PacketInServerDiscover()
	{
		this.server = new CachedServer(SwitchCore.getServer());
	}

	public PacketInServerDiscover(Server server)
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
