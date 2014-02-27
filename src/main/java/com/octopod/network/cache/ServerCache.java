package com.octopod.network.cache;

import com.octopod.network.Debug;
import com.octopod.network.ServerInfo;

import java.util.*;

public class ServerCache {

    private ServerCache() {}

    public static void reset() {
        serverMap.clear();
    }

	private static Map<String, ServerInfo> serverMap = Collections.synchronizedMap(new HashMap<String, ServerInfo>());

	public static boolean addServer(String server, ServerInfo info) {
        boolean isNew = !serverMap.containsKey(server);
        serverMap.put(server, info);
        if(isNew) {
            Debug.info("Recieved new info from &a" + server);
        } else {
            Debug.info("Recieved updated info from &a" + server);
        }
        return isNew;
	}

	public static boolean removeServer(String server) {
		if(serverMap.containsKey(server)) {
            Debug.debug("&cRemoved server &a" + server + "&c from cache.");
			serverMap.remove(server);
            return true;
		} else {
			return false;
		}
	}

	public static boolean serverExists(String server) {
		return serverMap.containsKey(server);
	}

	public static Map<String, ServerInfo> getServerMap() {
		return new HashMap<>(serverMap);
	}

}
