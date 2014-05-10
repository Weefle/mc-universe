package com.octopod.network.cache;

import com.octopod.network.NetworkPlus;
import com.octopod.network.ServerFlags;

import java.util.*;

public class NetworkServerCache {

    private NetworkServerCache() {}

    public static void reset() {
        serverMap.clear();
    }

	private static Map<String, ServerFlags> serverMap = Collections.synchronizedMap(new HashMap<String, ServerFlags>());

    /**
     * Adds a new ServerInfo entry into the cache.
     * @param serverID The ID of the server.
     * @param flags The flags recieved from this server.
     * @return The new or patched ServerFlags object.
     */
    public static ServerFlags addServer(String serverID, HashMap<String, Object> flags) {
        if(!serverMap.containsKey(serverID)) {
            //Add a new entry
            ServerFlags serverInfo = new ServerFlags(flags);
            serverMap.put(serverID, serverInfo);
            return serverInfo;
        } else {
            //Merge into existing entry
            ServerFlags serverInfo = serverMap.get(serverID);
            serverInfo.merge(flags);
            serverMap.put(serverID, serverInfo);
            return serverInfo;
        }
    }

	public static boolean removeServer(String serverID) {
		if(serverMap.containsKey(serverID)) {
            NetworkPlus.getLogger().debug("&cRemoved server &a" + serverID + "&c from cache.");
			serverMap.remove(serverID);
            return true;
		} else {
			return false;
		}
	}

	public static boolean serverExists(String serverID) {
		return serverMap.containsKey(serverID);
	}

    public static ServerFlags getInfo(String serverID) {
        if(serverExists(serverID)) {
            return serverMap.get(serverID);
        } else {
            return null;
        }
    }

    public static List<String> getOnlinePlayers(String serverID) {
        if(serverExists(serverID)) {
            return serverMap.get(serverID).getOnlinePlayers();
        } else {
            return null;
        }
    }

    public static List<String> getAllOnlinePlayers()
    {
        List<String> players = new ArrayList<>();
        for(ServerFlags serverInfo: serverMap.values())
        {
            players.addAll(serverInfo.getOnlinePlayers());
        }
        return players;
    }

    public static String findPlayer(String player)
    {
        for(Map.Entry<String, ServerFlags> entry: serverMap.entrySet())
        {
            if(entry.getValue().getOnlinePlayers().contains(player))
                return entry.getKey();
        }
        return null;
    }

	public static Map<String, ServerFlags> getServerMap() {
		return new HashMap<>(serverMap);
	}

}
