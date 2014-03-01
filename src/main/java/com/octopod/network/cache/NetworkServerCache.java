package com.octopod.network.cache;

import com.octopod.network.NetworkDebug;
import com.octopod.network.NetworkPlugin;

import java.util.*;

public class NetworkServerCache {

    private NetworkServerCache() {}

    public static void reset() {
        serverMap.clear();
    }

	private static Map<String, NetworkPlugin.ServerInfo> serverMap = Collections.synchronizedMap(new HashMap<String, NetworkPlugin.ServerInfo>());

	public static boolean addServer(String server, NetworkPlugin.ServerInfo info) {
        boolean isNew = !serverMap.containsKey(server);
        serverMap.put(server, info);
        if(isNew) {
            NetworkDebug.debug("Recieved new info from &a" + server);
        } else {
            NetworkDebug.debug("Recieved updated info from &a" + server);
        }
        return isNew;
	}

	public static boolean removeServer(String server) {
		if(serverMap.containsKey(server)) {
            NetworkDebug.debug("&cRemoved server &a" + server + "&c from cache.");
			serverMap.remove(server);
            return true;
		} else {
			return false;
		}
	}

	public static boolean serverExists(String server) {
		return serverMap.containsKey(server);
	}

	public static Map<String, NetworkPlugin.ServerInfo> getServerMap() {
		return new HashMap<>(serverMap);
	}

}
