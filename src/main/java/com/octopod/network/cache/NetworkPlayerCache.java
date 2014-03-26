package com.octopod.network.cache;

import java.util.*;

/**
 * This cache will be used to track which server players are on.
 * The player join and quit events should update the cache.
 * @author Octopod
 */
public class NetworkPlayerCache {

    private NetworkPlayerCache() {}

    public static void reset() {
        playerMap.clear();
    }

    final static private Map<String, String> playerMap = Collections.synchronizedMap(new HashMap<String, String>());

	public static void putPlayer(String player, String server)
    {
        playerMap.put(player, server);
	}
	
	public static void removePlayer(String player) {
        playerMap.remove(player);
	}
	
	public static String findPlayer(String player) {
        return playerMap.get(player);
	}

    public static List<String> getPlayers()
    {
        return new ArrayList<>(playerMap.keySet());
    }

    public static List<String> getPlayers(String server) {
        List<String> players = new ArrayList<>();
        for(Map.Entry<String, String> entry: playerMap.entrySet()) {
            if(entry.getValue().equals(server))
                players.add(entry.getKey());
        }
        return players;
    }

    public static int totalPlayers() {
        return playerMap.size();
    }

    public static int totalPlayers(String server) {
        return getPlayers(server).size();
    }

}
