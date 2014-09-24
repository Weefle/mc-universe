package com.octopod.networkplus;

import com.octopod.networkplus.network.NetworkConnection;
import com.octopod.networkplus.server.ServerPlayer;
import com.octopod.util.configuration.yaml.YamlConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class LocalServerInfo implements ServerInfo
{
	@Override
	public void setValue(ServerValue infoType, Object value)
	{
		//Don't do anything, why would you change a local value?
	}

	@Override
	public Object getValue(ServerValue infoType)
	{
		NetworkConnection connection = NetworkPlus.getConnection();
		YamlConfiguration config = NetworkPlus.getConfig();
		switch(infoType)
		{
			case SERVER_NAME:
				return config.getString("name", connection.getServerIdentifier());
			case SERVER_VERSION:
				return NetworkPlus.getPlugin().getPluginVersion();
			case MAX_PLAYERS:
				return NetworkPlus.getServer().getMaxPlayers();
			case WHITELIST_ENABLED:
				return NetworkPlus.getServer().getWhitelistEnabled();
			case WHITELIST_PLAYERS:
				return NetworkPlus.getServer().getWhitelistedPlayers();
			case HUB_PRIORITY:
				return config.getInt("hub-priority", -1);
			case ONLINE_PLAYERS:
				List<String> playerIDs = new ArrayList<>();
				for(ServerPlayer player: NetworkPlus.getServer().getOnlinePlayers()) playerIDs.add(player.getID());
				return playerIDs;
			case QUEUED_PLAYERS:
				return new ArrayList<String>();
			case LAST_ONLINE:
				return -1;
			default:
				return null;
		}
	}

	@Override
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
	public boolean containsValue(ServerValue infoType)
	{
		return true;
	}

	@Override
	public String encode()
	{
		return NetworkPlus.getSerializer().encode(getValues());
	}
}
