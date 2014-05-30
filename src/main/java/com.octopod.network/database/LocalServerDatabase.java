package com.octopod.network.database;

import com.google.gson.Gson;
import com.octopod.network.NetworkPlus;
import com.octopod.network.ServerFlags;
import com.octopod.network.Util;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class LocalServerDatabase extends ServerDatabase {

	private final File folder = new File(NetworkPlus.getDataFolder(), "servers");
	private final Map<String, ServerFlags> servers = new HashMap<>();

	@Override
	public String getName() {return "Local";}

	@Override
	public void onStartup() {
		int count = 0;
		Gson gson = NetworkPlus.gson();
		if(!folder.exists()) folder.mkdir();
		for(File file: folder.listFiles()) {
			String serverID = FilenameUtils.removeExtension(file.getName());
			try {
				ServerFlags flags = gson.fromJson(FileUtils.readFileToString(file), ServerFlags.class);
				set(serverID, flags);
				count++;
			} catch (IOException e) {
				NetworkPlus.getLogger().i("Unable to read " + file.getName() + "!");
				e.printStackTrace();
			}
		}
		NetworkPlus.getLogger().i(count + " servers pre-loaded.");
	}

	@Override
	public void onShutdown() {
		//Save whatever's in memory into separate files
		Gson gson = NetworkPlus.gson();
		for(Entry<String, ServerFlags> entry: servers.entrySet()) {
			entry.getValue().setFlag("lastOnline", System.currentTimeMillis());
			try {
				Util.write(new File(folder, entry.getKey() + ".json"), gson.toJson(entry.getValue()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void set(String serverID, ServerFlags flags) {
		if(!servers.containsKey(serverID)) {
			//Add a new entry
			servers.put(serverID, flags);
		} else {
			//Merge into existing entry
			ServerFlags value = servers.get(serverID);
			value.merge(flags);
			servers.put(serverID, value);
		}
	}

	@Override
	public void clear(String serverID) {
		servers.remove(serverID);
	}

	@Override
	public ServerFlags get(String serverID) {
		return servers.get(serverID);
	}

	@Override
	public Map<String, ServerFlags> toMap() {
		return servers;
	}

	@Override
	public boolean keyExists(String serverID) {
		return servers.containsKey(serverID);
	}

	@Override
	public String[] getServers() {
		Set<String> set = servers.keySet();
		return set.toArray(new String[set.size()]);
	}

}
