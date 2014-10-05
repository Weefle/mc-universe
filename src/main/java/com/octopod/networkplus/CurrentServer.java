package com.octopod.networkplus;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.util.configuration.yaml.YamlConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class CurrentServer implements Server
{
	@Override
	public void setValue(ServerValue type, Object value)
	{
		//Do nothing
	}

	public Object getValue(ServerValue type)
	{
		NetworkConnection connection = NetworkPlus.getConnection();
		YamlConfiguration config = NetworkPlus.getConfig();

		switch(type)
		{
			case SERVER_NAME:
				String name = config.getString("name");
				return name == null || name.equals("") ? connection.getServerIdentifier() : name;
			case SERVER_VERSION:
				return NetworkPlus.getPlugin().getPluginVersion();
			case MAX_PLAYERS:
				return NetworkPlus.getInterface().getMaxPlayers();
			case WHITELIST_ENABLED:
				return NetworkPlus.getInterface().getWhitelistEnabled();
			case WHITELIST_PLAYERS:
				return NetworkPlus.getInterface().getWhitelistedPlayers();
			case HUB_PRIORITY:
				return config.getInt("hub-priority", -1);
			case ONLINE_PLAYERS:
				ArrayList<String> players = new ArrayList<>();
				for(MinecraftPlayer player: NetworkPlus.getInterface().getOnlinePlayers())
				{
					players.add(player.getUUID());
				}
				return players.toArray(new String[players.size()]);
			case QUEUED_PLAYERS:
				return new String[0];
			case LAST_ONLINE:
				return -1;
			default:
				return null;
		}
	}

	@Override
	public int totalValues()
	{
		return ServerValue.values().length;
	}

	@Override
	public Map<ServerValue, Object> toValueMap()
	{
		Map<ServerValue, Object> valueMap = new HashMap<>();
		for(ServerValue value: ServerValue.values())
		{
			valueMap.put(value, getValue(value));
		}
		return valueMap;
	}

	@Override
	public boolean isOnline()
	{
		return true;
	}

	@Override
	public String getServerIdentifier()
	{
		return NetworkPlus.getServerIdentifier();
	}

	@Override
	public String getServerName()
	{
		NetworkConnection connection = NetworkPlus.getConnection();
		YamlConfiguration config = NetworkPlus.getConfig();
		String name = config.getString("name");
		return name == null || name.equals("") ? connection.getServerIdentifier() : name;
	}

	@Override
	public String getServerVersion()
	{
		return NetworkPlus.getPlugin().getPluginVersion();
	}

	@Override
	public int getMaxPlayers()
	{
		return NetworkPlus.getInterface().getMaxPlayers();
	}

	@Override
	public boolean getWhitelistEnabled()
	{
		return NetworkPlus.getInterface().getWhitelistEnabled();
	}

	@Override
	public String[] getWhitelistedPlayers()
	{
		return NetworkPlus.getInterface().getWhitelistedPlayers();
	}

	@Override
	public int getHubPriority()
	{
		YamlConfiguration config = NetworkPlus.getConfig();
		return config.getInt("hub-priority", -1);
	}

	@Override
	public String[] getOnlinePlayers()
	{
		ArrayList<String> players = new ArrayList<>();
		for(MinecraftPlayer player: NetworkPlus.getInterface().getOnlinePlayers())
		{
			players.add(player.getUUID());
		}
		return players.toArray(new String[players.size()]);
	}

	@Override
	public String[] getQueuedPlayers()
	{
		return new String[0];
	}

	@Override
	public long getLastOnline()
	{
		return -1;
	}

	@Deprecated
	public Map<ServerValue, Object> getValues()
	{
		Map<ServerValue, Object> values = new HashMap<>();
		for(ServerValue value: ServerValue.values())
		{
			values.put(value, getValue(value));
		}
		return values;
	}
}
