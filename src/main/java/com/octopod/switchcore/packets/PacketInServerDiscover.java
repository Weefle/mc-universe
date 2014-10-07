package com.octopod.switchcore.packets;

import com.octopod.switchcore.CachedServer;
import com.octopod.switchcore.Server;
import com.octopod.switchcore.StaticChannel;
import com.octopod.switchcore.SwitchCore;

/**
 * @author Octopod - octopodsquad@gmail.com
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

	@Override
	public String[] getMessage()
	{
		return new String[0];
	}

	@Override
	public String getChannelOut()
	{
		return StaticChannel.IN_SERVER_DISCOVER.toString();
	}
}
