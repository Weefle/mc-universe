package com.hyperfresh.mcuniverse.database;

import com.hyperfresh.mcuniverse.UniverseAPI;
import com.hyperfresh.mcuniverse.server.networked.UniverseServer;
import com.hyperfresh.mcuniverse.server.networked.local.LocalServer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class LocalFileDatabase implements ServerDatabase
{
	private Map<String, UniverseServer> servers = new HashMap<>();

	public LocalFileDatabase()
	{
		servers.put(UniverseAPI.getInstance().getConnection().getServerIdentifier(), new LocalServer(UniverseAPI.getInstance().getInterface()));
	}

	@Override
	public UniverseServer getServer(String server)
	{
		if(!servers.containsKey(server)) throw new NullPointerException("UniverseServer '" + server + "' not found");
		return servers.get(server);
	}

	@Override
	public Collection<UniverseServer> getServers()
	{
		return servers.values();
	}

	@Override
	public boolean serverExists(String server)
	{
		return servers.containsKey(server);
	}

	@Override
	public void addServer(UniverseServer server)
	{
		servers.put(server.getServerUsername(), server);
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
