package com.octopod.network.cache;

import com.octopod.network.Debug;
import com.octopod.network.LPRequestUtils;
import com.octopod.network.events.server.ServerInfoEvent;
import com.octopod.network.events.synclisteners.SyncServerInfoListener;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * This cache will be used to track which server players are on.
 * The player join and quit events should update the cache.
 * @author Octopod
 */
public class PlayerCache {
	
	private static PlayerCache cache = null;
	
	public static PlayerCache getCache() {
		if(cache == null) {
			return (cache = new PlayerCache());
		} else {
			return cache;
		}
	}

    public static void deleteCache() {
        if(cache != null) {
            cache = null;
        }
    }
	
	public void importServer(final String server) 
	{
		LPRequestUtils.requestServerInfo(Arrays.asList(server));
		List<ServerInfoEvent> events = SyncServerInfoListener.waitForExecutions(1, Arrays.asList(server));
		
		if(events.isEmpty()) {
			Debug.info("Failed to import players from &a" + server);
		} else {
			ServerInfoEvent event = events.get(0);
            Iterator<String> it = event.getServerInfo().getPlayers().iterator();
            int playerCount = 0;
			while(it.hasNext()) {
                PlayerCache.this.putPlayer(it.next(), server);
                playerCount++;
            }
			Debug.info("Imported &a" + playerCount + "&7 players from &a" + server);
		}
	}

	final private Map<String, String> playerMap = Collections.synchronizedMap(new HashMap<String, String>());

	public void putPlayer(String player, String server) {
		playerMap.put(player, server);
	}
	
	public void removePlayer(String player) {
		playerMap.remove(player);
	}
	
	public String findPlayer(String player) {
		return playerMap.get(player);
	}
	
	public Map<String, String> getPlayerMap() {
		return playerMap;
	}

}
