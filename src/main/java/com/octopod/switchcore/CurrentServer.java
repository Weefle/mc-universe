package com.octopod.switchcore;

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
		NetworkConnection connection = SwitchCore.getConnection();
		YamlConfiguration config = SwitchCore.getConfig();

		switch(type)
		{
			case SERVER_NAME:
				String name = config.getString("name");
				return name == null || name.equals("") ? connection.getServerIdentifier() : name;
			case SERVER_VERSION:
				return SwitchCore.getPlugin().getPluginVersion();
			case MAX_PLAYERS:
				return SwitchCore.getInterface().getMaxPlayers();
			case WHITELIST_ENABLED:
				return SwitchCore.getInterface().getWhitelistEnabled();
			case WHITELIST_PLAYERS:
				return SwitchCore.getInterface().getWhitelistedPlayers();
			case HUB_PRIORITY:
				return config.getInt("hub-priority", -1);
			case ONLINE_PLAYERS:
				ArrayList<String> players = new ArrayList<>();
				for(MinecraftPlayer player: SwitchCore.getInterface().getOnlinePlayers())
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
	public boolean isExternal()
	{
		return false;
	}

	@Override
	public boolean isOnline()
	{
		return true;
	}

	@Override
	public String getServerIdentifier()
	{
		return SwitchCore.getServerIdentifier();
	}

	@Override
	public String getServerName()
	{
		NetworkConnection connection = SwitchCore.getConnection();
		YamlConfiguration config = SwitchCore.getConfig();
		String name = config.getString("name");
		return name == null || name.equals("") ? connection.getServerIdentifier() : name;
	}

	@Override
	public String getServerVersion()
	{
		return SwitchCore.getPlugin().getPluginVersion();
	}

	@Override
	public int getMaxPlayers()
	{
		return SwitchCore.getInterface().getMaxPlayers();
	}

	@Override
	public boolean getWhitelistEnabled()
	{
		return SwitchCore.getInterface().getWhitelistEnabled();
	}

	@Override
	public String[] getWhitelistedPlayers()
	{
		return SwitchCore.getInterface().getWhitelistedPlayers();
	}

	@Override
	public int getHubPriority()
	{
		YamlConfiguration config = SwitchCore.getConfig();
		return config.getInt("hub-priority", -1);
	}

	@Override
	public String[] getOnlinePlayers()
	{
		ArrayList<String> players = new ArrayList<>();
		for(MinecraftPlayer player: SwitchCore.getInterface().getOnlinePlayers())
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

	@Override
	public String toString()
	{
		return "[server: " + getServerIdentifier() + ", values: " + totalValues() + "]";
	}
}
