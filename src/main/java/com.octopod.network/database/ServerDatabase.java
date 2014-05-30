package com.octopod.network.database;

import com.octopod.network.ServerFlags;

import java.util.*;
import java.util.Map.Entry;

public abstract class ServerDatabase {

	public abstract String getName();

	public void onStartup() {}

	public void onShutdown() {}

	public abstract void set(String serverID, ServerFlags flags);

	public abstract void clear(String serverID);

	public abstract ServerFlags get(String serverID);

	public abstract Map<String, ServerFlags> toMap();

	/**
	 * Returns if the cache contains a serverID
	 * @param serverID a server's ID
	 * @return if the serverID exists in the cache
	 */
	public abstract boolean keyExists(String serverID);

	/**
	 * Returns a collection of the currently cached serverIDs
	 * @return all the cached serverIDs
	 */
	public abstract String[] getServers();

	public List<String> getOnlinePlayers(String serverID) {
		if(keyExists(serverID)) {
			return get(serverID).getOnlinePlayers();
		} else {
			return null;
		}
	}

	public List<String> getAllOnlinePlayers()
	{
		List<String> players = new ArrayList<>();
		for(ServerFlags serverInfo: toMap().values())
		{
			players.addAll(serverInfo.getOnlinePlayers());
		}
		return players;
	}

	public String findPlayer(String player)
	{
		for(Entry<String, ServerFlags> entry: toMap().entrySet())
		{
			if(entry.getValue().getOnlinePlayers().contains(player))
				return entry.getKey();
		}
		return null;
	}

}
