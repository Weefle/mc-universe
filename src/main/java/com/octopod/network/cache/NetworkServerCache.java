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
     * @param serverID The name of the server.
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
            return serverInfo;
        }
    }

	public static boolean removeServer(String server) {
		if(serverMap.containsKey(server)) {
            NetworkPlus.getLogger().debug("&cRemoved server &a" + server + "&c from cache.");
			serverMap.remove(server);
            return true;
		} else {
			return false;
		}
	}

	public static boolean serverExists(String server) {
		return serverMap.containsKey(server);
	}

    public static ServerFlags getInfo(String server) {
        if(serverExists(server)) {
            return serverMap.get(server);
        } else {
            return null;
        }
    }

    public static List<String> getOnlinePlayers(String server) {
        if(serverExists(server)) {
            return serverMap.get(server).getOnlinePlayers();
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
