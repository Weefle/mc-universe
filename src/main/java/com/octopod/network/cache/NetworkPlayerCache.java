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
        serverMap.clear();
    }

    final static private Map<String, List<String>> serverMap = Collections.synchronizedMap(new HashMap<String, List<String>>());

	public static void putPlayer(String player, String server)
    {
        removePlayer(player);

        if(!serverMap.containsKey(server))
            serverMap.put(server, new ArrayList<String>());

        if(!serverMap.get(server).contains(player))
            serverMap.get(server).add(player);
	}
	
	public static void removePlayer(String player) {
        for(List<String> players: serverMap.values())
            players.remove(player);
	}
	
	public static String findPlayer(String player) {
        for(Map.Entry<String, List<String>> entry: serverMap.entrySet()) {
            if(entry.getValue().contains(player))
                return entry.getKey();
        }
		return null;
	}

    public static List<String> getPlayers()
    {
        List<String> allPlayers = new ArrayList<>();
        for(List<String> players: serverMap.values())
            allPlayers.addAll(players);
        return allPlayers;
    }

    public static List<String> getPlayers(String server) {
        if(serverMap.containsKey(server)) {
            return serverMap.get(server);
        } else {
            return new ArrayList<>();
        }
    }

    public static int totalPlayers() {
        return getPlayers().size();
    }

    public static int totalPlayers(String server) {
        return getPlayers(server).size();
    }

}
