package com.octopod.switchcore.database;

import com.octopod.switchcore.CurrentServer;
import com.octopod.switchcore.SwitchCore;
import com.octopod.switchcore.Server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class LocalFileDatabase implements ServerDatabase
{
	private Map<String, Server> servers = new HashMap<>();

	public LocalFileDatabase()
	{
		servers.put(SwitchCore.getConnection().getServerIdentifier(), new CurrentServer());
	}

	@Override
	public Server getServer(String server)
	{
		if(!servers.containsKey(server)) throw new NullPointerException("Server '" + server + "' not found");
		return servers.get(server);
	}

	@Override
	public Collection<Server> getServers()
	{
		return servers.values();
	}

	@Override
	public boolean serverExists(String server)
	{
		return servers.containsKey(server);
	}

	@Override
	public void setServer(Server server)
	{
		servers.put(server.getServerIdentifier(), server);
	}

	@Override
	public void removeServer(String server)
	{
		servers.remove(server);
	}

	@Override
	public Collection<String> getServerNames()
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
