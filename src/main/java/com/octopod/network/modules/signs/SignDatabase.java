package com.octopod.network.modules.signs;

import org.bukkit.Location;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod
 *         Created on 3/8/14
 */
public class SignDatabase {

    public static SignDatabase decode(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, SignDatabase.class);
    }

    public static String encode(SignDatabase config) {
        Gson gson = new Gson();
        return gson.toJson(config);
    }

    private boolean isLocationUsed(Location loc) {
        for(SignLocationList locs: signMap.values()) {
            if(locs.contains(loc)) return true;
        }
        return false;
    }

    private HashMap<String, SignLocationList> signMap = new HashMap<>();

    public void addSign(String server, Location loc) {
        if(isLocationUsed(loc)) return;
        if(!signMap.containsKey(server))
            signMap.put(server, new SignLocationList());

        signMap.get(server).add(loc);
    }

    public String getSign(Location loc) {
        for(Map.Entry<String, SignLocationList> e: signMap.entrySet()) {
            if(e.getValue().contains(loc)) return e.getKey();
        }
        return null;
    }

    public void removeSign(Location loc) {
        for(SignLocationList locs: signMap.values()) {
            if(locs.contains(loc)) {
                locs.remove(loc);
                break;
            }
        }
    }

}
