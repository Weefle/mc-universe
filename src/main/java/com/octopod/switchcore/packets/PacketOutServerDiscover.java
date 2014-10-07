package com.octopod.switchcore.packets;

import com.octopod.switchcore.CachedServer;
import com.octopod.switchcore.Server;
import com.octopod.switchcore.StaticChannel;
import com.octopod.switchcore.SwitchCore;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Used with broadcast()
 * A server (with SwitchCore loaded) that recieves this packet will return another one with their current information.
 * The difference between this packet and PacketInServerDiscover is that this packet will cause a return.
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

	@Override
	public String[] getMessage()
	{
		return new String[0];
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.OUT_SERVER_DISCOVER.toString();
	}

	@Override
	public String getChannelIn()
	{
		return StaticChannel.IN_SERVER_DISCOVER.toString();
	}
}
