package com.octopod.network.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerCache {

	private static ServerCache cache = null;

	public static ServerCache getCache() {
		if(cache != null) {
			return cache;
		} else {
			return new ServerCache();
		}
	}
	
	public static void deleteCache() {
		if(cache != null) {
			cache = null;
		}
	}
			
	private List<String> cachedServers = Collections.synchronizedList(new ArrayList<String>());

	public boolean addServer(String server) {
		if(!cachedServers.contains(server)) {
			return cachedServers.add(server);
		} else {
			return false;
		}
	}
	
	public boolean removeServer(String server) {
		if(cachedServers.contains(server)) {
			return cachedServers.remove(server);
		} else {
			return false;
		}
	}
	
	public boolean serverExists(String server) {
		return cachedServers.contains(server);
	}
	
	public List<String> getServers() {
		return new ArrayList<String>(cachedServers);
	}

	private ServerCache() {
		if(cache != null) {return;}
		cache = this;
	}

}
