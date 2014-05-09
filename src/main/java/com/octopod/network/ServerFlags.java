package com.octopod.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;

import com.octopod.network.NetworkConfig;
import com.octopod.network.NetworkPlus;
import com.octopod.network.NetworkQueueManager;
import com.octopod.network.bukkit.BukkitUtils;

/**
 * @author Octopod
 *         Created on 3/13/14
 */

/**
 * ServerFlags is pretty much just a wrapper around a HashMap.
 * The HashMap is what will contain all the server's information.
 */
public class ServerFlags {

    /**
     * The map of flags for this ServerInfo instance.
     * TODO: Make this HashMap a <String, String> instead. Need to settle on a way to serialize String arguments for lists first.
     */
    private HashMap<String, Object> flags = new HashMap<>();

    /**
     * Creates the default ServerInfo.
     */
    public ServerFlags() {
        this(currentGenerator.generate());
    }

    /**
     * Creates a ServerInfo instance by flags.
     * @param map A map containing the arguments for this object
     */
    private ServerFlags(HashMap<String, Object> map) {
        flags = map;
    }

    /**
     * An interface that will generate the default ServerFlags object
     */
    public static interface Generator {
        public HashMap<String, Object> generate();
    }

    /**
     * The current generator that creates the default ServerFlags object
     */
    private static Generator currentGenerator = new Generator() {

        public HashMap<String, Object> generate() {
            HashMap<String, Object> options = new HashMap<>();
            options.put("username", NetworkPlus.getUsername());             //Server's username
            options.put("serverName", NetworkConfig.getServerName());       //Server's config name
            options.put("description", Bukkit.getServer().getMotd());       //Server's MOTD
            options.put("maxPlayers", Bukkit.getServer().getMaxPlayers());  //Server's max players
            options.put("whitelistedPlayers", Arrays.asList(BukkitUtils.getWhitelistedPlayerNames())); //Server's whitelisted players
            options.put("hubPriority", NetworkConfig.isHub() ? NetworkConfig.getHubPriority() : -1); //Server's hub priority, or -1 if is not a hub.
            options.put("version", NetworkPlus.getPluginVersion()); //Server's plugin version. (<build>-<commit>)
            options.put("onlinePlayers", Arrays.asList(BukkitUtils.getPlayerNames())); //Server's online players.
            options.put("queuedPlayers", NetworkQueueManager.instance.getQueueMembers());// Players queued for this server
            return options;
        }

    };

    public HashMap<String, Object> getFlags() {
        return flags;
    }

    /**
     * Merges the map into this instance's option map.
     * Used to "patch" the current flags.
     * @param info
     */
    public void merge(HashMap<String, Object> info) {
        flags.putAll(info);
    }

    /**
     * Sets a flag to a value.
     * @param k The key.
     * @param v The value.
     */
    public void setFlag(String k, Object v) {
        flags.put(k, v);
    }

    /**
     * Gets the flag by key.
     * @param k The key.
     * @param def The value to return if the key doesn't exist.
     * @return The value located at the key, or 'def' if the key doesn't exist.
     */
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

    //A bunch of default getters

    public String getUsername() {return getString("username");}
    public String getServerName() {return getString("serverName");}
    public String getDescription() {return getString("description");}
    public String getVersion() {return getString("version");}

    public Integer getMaxPlayers() {return getInteger("maxPlayers");}
    public Integer getHubPriority() {return getInteger("hubPriority");}

    public ArrayList<String> getWhitelistedPlayers() {return getStringList("whitelistedPlayers");}
    public ArrayList<String> getOnlinePlayers() {return getStringList("onlinePlayers");}
    public ArrayList<String> getQueuedPlayers() {return getStringList("queuedPlayers");}

    /**
     * Gets this object returned as a JSON string.
     * @return The JSON representation of this object.
     */
    @Override
    public String toString() {
        return NetworkPlus.gson().toJson(this);
    }

}