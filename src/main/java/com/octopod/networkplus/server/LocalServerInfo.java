package com.octopod.networkplus.server;

import com.octopod.networkplus.NetworkPlus;
import com.octopod.networkplus.network.NetworkConnection;
import com.octopod.util.configuration.yaml.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

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
		NetworkPlus networkPlus = NetworkPlus.getInstance();
		NetworkConnection connection = networkPlus.getConnection();
		YamlConfiguration config = networkPlus.getConfig();
		switch(infoType)
		{
			case SERVER_NAME:
				return config.getString("name", connection.getServerIdentifier());
			case SERVER_VERSION:
				return networkPlus.getPlugin().getPluginVersion();
			case MAX_PLAYERS:
				return networkPlus.getServer().getMaxPlayers();
			case WHITELIST_ENABLED:
				return networkPlus.getServer().getWhitelistEnabled();
			case WHITELIST_PLAYERS:
				return networkPlus.getServer().getWhitelistedPlayers();
			case HUB_PRIORITY:
				return config.getInt("hub-priority", -1);
			case ONLINE_PLAYERS:
				List<String> playerIDs = new ArrayList<>();
				for(ServerPlayer player: networkPlus.getServer().getOnlinePlayers()) playerIDs.add(player.getID());
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
	public boolean containsValue(ServerValue infoType)
	{
		return true;
	}
}
