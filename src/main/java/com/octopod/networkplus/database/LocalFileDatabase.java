package com.octopod.networkplus.database;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.server.CachedServerInfo;
import com.octopod.networkplus.server.LocalServerInfo;
import com.octopod.networkplus.server.ServerInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class LocalFileDatabase implements ServerDatabase
{
	private Map<String, ServerInfo> servers = new HashMap<>();
	private String thisServerID;

	public LocalFileDatabase()
	{
		thisServerID = NetworkPlus.getInstance().getConnection().getServerIdentifier();
		servers.put(thisServerID, new LocalServerInfo());
	}

	@Override
	public ServerInfo getServerInfo(String serverID)
	{
		if(!servers.containsKey(serverID)) servers.put(serverID, new CachedServerInfo());
		return servers.get(serverID);
	}

	@Override
	public ServerInfo getServerInfo()
	{
		return getServerInfo(thisServerID);
	}

	@Override
	public void removeServerInfo(String serverID)
	{
		servers.remove(serverID);
	}

	@Override
	public Collection<String> getServerIDs()
	{
		return servers.keySet();
	}

	@Override
	public void write()
	{

	}

	@Override
	public void read()
	{

	}
}
