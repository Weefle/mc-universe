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

	public static void putPlayer(String player, String server) {
		playerMap.put(player, server);
	}
	
	public static void removePlayer(String player) {
		playerMap.remove(player);
	}
	
	public static String findPlayer(String player) {
		return playerMap.get(player);
	}
	
	public static Map<String, String> getPlayerMap() {
		return playerMap;
	}

    public static Map<String, List<String>> getReverseMap() {
        Map<String, List<String>> serverMap = new HashMap<>();
        //Loops through the entries in the map.
        for(Map.Entry<String, String> e: playerMap.entrySet()) {
            //Create a new list @ the server (the value) if one doesn't exist
            if(!serverMap.containsKey(e.getValue()))
                serverMap.put(e.getValue(), new ArrayList<String>());
            //Get the list @ the server and add the player's name (the key)
            serverMap.get(e.getValue()).add(e.getKey());
        }
        return serverMap;
    }

    public static int totalPlayers(String server) {
        Map<String, List<String>> reverse = getReverseMap();
        if(reverse.containsKey(server)) {
            return reverse.get(server).size();
        } else {
            return 0;
        }
    }

    public static int totalPlayers() {
        return playerMap.size();
    }

}
