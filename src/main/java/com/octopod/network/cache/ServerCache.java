package com.octopod.network.cache;

import com.octopod.network.Debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerCache {

    private ServerCache() {}

    public static void reset() {
        serverList.clear();
    }

	private static List<String> serverList = Collections.synchronizedList(new ArrayList<String>());

	public static boolean addServer(String server) {
		if(!serverList.contains(server)) {
            Debug.info("Cached server &a" + server);
			return serverList.add(server);
		} else {
			return false;
		}
	}
	
	public static boolean removeServer(String server) {
		if(serverList.contains(server)) {
            Debug.debug("&cUncached server &a" + server);
			return serverList.remove(server);
		} else {
			return false;
		}
	}
	
	public static boolean serverExists(String server) {
		return serverList.contains(server);
	}
	
	public static List<String> getServerList() {
		return new ArrayList<String>(serverList);
	}

}
