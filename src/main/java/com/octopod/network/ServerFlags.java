package com.octopod.network;

import com.octopod.network.bukkit.BukkitUtils;
import org.bukkit.Bukkit;

import java.util.*;

/**
 * @author Octopod
 *         Created on 3/13/14
 */

public class ServerFlags {

    /**
     * Generates the ServerInfo for this server. It should be only used once on startup/reload.
     * Also, it should be changed along with the protocol.
     */
    public static ServerFlags createLocal() {
        HashMap<String, Object> options = new HashMap<>();
        options.put("username", NetworkPlus.getUsername());             //Server's username
        options.put("serverName", NetworkConfig.getServerName());       //Server's config name
        options.put("description", Bukkit.getServer().getMotd());       //Server's MOTD
        options.put("maxPlayers", Bukkit.getServer().getMaxPlayers());  //Server's max players
        options.put("whitelistedPlayers", Arrays.asList(BukkitUtils.getWhitelistedPlayerNames())); //Server's whitelisted players
        options.put("hubPriority", NetworkConfig.isHub() ? NetworkConfig.getHubPriority() : -1); //Server's hub priority, or -1 if is not a hub.
        options.put("version", NetworkPlus.getPluginVersion()); //Server's plugin version. (<build>-<commit>)
        options.put("onlinePlayers", Arrays.asList(BukkitUtils.getPlayerNames())); //Server's online players.
        return new ServerFlags(options);
    }

    /**
     * The map of flags for this ServerInfo instance.
     */
    private HashMap<String, Object> flags = new HashMap<>();

    /**
     * Creates a ServerInfo instance by flags.
     * @param map A map containing the arguments for this object
     */
    public ServerFlags(HashMap<String, Object> map) {
        flags = map;
    }

    /**
     * Merges the map into this instance's option map.
     * Used to "patch" the current flags.
     * @param info
     */
    public void merge(HashMap<String, Object> info) {
        flags.putAll(info);
    }

    public void setFlag(String k, Object v) {
        flags.put(k, v);
    }

    public Object getFlag(String k, Object def) {
        Object val = flags.get(k);
        if(val == null)
            return def;
            return val;
    }

    public void setString(String k, String v) {setFlag(k, v);}
    public void setInteger(String k, Integer v) {setFlag(k, v);}
    public void setStringList(String k, ArrayList<String> v) {setFlag(k, v);}

    public String getString(String k) {return (String) getFlag(k, "[UNKNOWN]");}
    public Integer getInteger(String k) {return (Integer) getFlag(k, -1);}
    public ArrayList<String> getStringList(String k) {return (ArrayList<String>) getFlag(k, new ArrayList<String>());}

    public String getUsername() {return getString("username");}
    public String getServerName() {return getString("serverName");}
    public String getDescription() {return getString("description");}
    public String getVersion() {return getString("version");}

    public Integer getMaxPlayers() {return getInteger("maxPlayers");}
    public Integer getHubPriority() {return getInteger("hubPriority");}

    public ArrayList<String> getWhitelistedPlayers() {return getStringList("whitelistedPlayers");}
    public ArrayList<String> getOnlinePlayers() {return getStringList("onlinePlayers");}

    public String toString() {
        return NetworkPlus.gson().toJson(this);
    }

}