package com.octopod.network.modules.signs;

import com.octopod.network.NetworkLogger;
import com.octopod.network.NetworkPlus;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod
 *         Created on 3/8/14
 */
public class SignDatabase {

    private NetworkLogger logger = NetworkPlus.getLogger();

    private boolean isLocationUsed(Location loc) {
        for(ArrayList<SignLocation> locs: servers.values()) {
            if(locs.contains(new SignLocation(loc))) return true;
        }
        return false;
    }

    private HashMap<String, ArrayList<SignLocation>> servers = new HashMap<>();

    public int totalSigns() {
        int count = 0;
        for(ArrayList<SignLocation> locs: servers.values()) {
            count += locs.size();
        }
        return count;
    }

    public void addSign(String server, Location loc) {
        if(isLocationUsed(loc)) return;
        if(!servers.containsKey(server))
            servers.put(server, new ArrayList<SignLocation>());

        servers.get(server).add(new SignLocation(loc));
        logger.info("&6NetSign &7registered @ &e" + new SignLocation(loc) + "&7!");
    }

    public String getSign(Location loc) {
        for(Map.Entry<String, ArrayList<SignLocation>> e: servers.entrySet()) {
            if(e.getValue().contains(new SignLocation(loc))) return e.getKey();
        }
        return null;
    }

    public ArrayList<SignLocation> getSigns(String server) {
        return servers.get(server);
    }

    public void removeSign(Location loc) {
        for(ArrayList<SignLocation> locs: servers.values()) {
            if(locs.contains(new SignLocation(loc))) {
                locs.remove(new SignLocation(loc));
                logger.info("&6NetSign &7unregistered @ &e" + new SignLocation(loc) + "&7!");
                break;
            }
        }
    }

}
