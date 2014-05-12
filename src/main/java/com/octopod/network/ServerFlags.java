package com.octopod.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;

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
     */
    private HashMap<String, Object> flagMap = new HashMap<>();

    /**
     * An interface that will generate the default ServerFlags object
     */
    public static interface Generator {
        public ServerFlags generate();
    }

    /**
     * The current generator that creates the default ServerFlags object
     */
    private static Generator currentGenerator = new Generator() {

        public ServerFlags generate()
        {
            ServerFlags flags = new ServerFlags();

            //Server's identification name
            flags.setFlag("serverID", NetworkPlus.getServerID());

            //Server's config name (or ID if blank)
            flags.setFlag("serverName", NetworkConfig.getServerName());

            //Server's config description (or MOTD if blank)
            flags.setFlag("description", Bukkit.getServer().getMotd());

            //Server's maximum players
            flags.setInteger("maxPlayers", Bukkit.getServer().getMaxPlayers());

            //If the server's whitelist is enabled
            flags.setBoolean("serverWhitelistEnabled", Bukkit.getServer().hasWhitelist());

            //Server's list of whitelisted players (empty list if whitelist is disabled)
            flags.setStringList("whitelistedPlayers", new ArrayList<>(Arrays.asList(BukkitUtils.getWhitelistedPlayerNames())));

            //Server's hub priority (-1 if not a hub)
            flags.setInteger("hubPriority", NetworkConfig.isHub() ? NetworkConfig.getHubPriority() : -1);

            //Server's plugin version
            flags.setFlag("version", NetworkPlus.getPluginVersion());

            //Server's list of online players
            flags.setStringList("onlinePlayers", new ArrayList<>(Arrays.asList(BukkitUtils.getPlayerNames())));

            //Players queued for this server.
            flags.setStringList("queuedPlayers", NetworkQueueManager.instance.getQueueMembers());

            //Status of the server (true if online, false if not)
            flags.setBoolean("serverStatus", true);

            return flags;
        }

    };

    public static ServerFlags generateFlags() {
        return currentGenerator.generate();
    }

    /**
     * Merges the map into this instance's option map.
     * Used to "patch" the current flagMap.
     * @param flags
     */
    public void merge(ServerFlags flags) {
        flagMap.putAll(flags.asMap());
    }

    public boolean hasFlag(String key) {
        return flagMap.containsKey(key);
    }

    /**
     * Sets a flag to a value.
     * @param key The key.
     * @param value The value.
     */
    public void setFlag(String key, String value) {
        flagMap.put(key, value);
    }

    /**
     * Gets the flag by key.
     * @param key The key.
     * @param defaultValue The value to return if the key doesn't exist.
     * @return The value located at the key, or 'defaultValue' if the key doesn't exist.
     */
    public Object getFlag(String key, Object defaultValue) {
        Object val = flagMap.get(key);
        if(val == null)
            return defaultValue;
            return val;
    }

    public void setString(String key, String value) {setFlag(key, value);}
    public void setBoolean(String key, Boolean value) {setFlag(key, value.toString());}
    public void setInteger(String key, Integer value) {setFlag(key, value.toString());}
    public void setStringList(String key, List<String> value) {setFlag(key, Util.generateArgs(value.toArray(new String[value.size()])));}

    public String getString(String key) {return (String)getFlag(key, "null");}
    public Boolean getBoolean(String key) {return (Boolean)getFlag(key, false);}
    public Integer getInteger(String key) {
        Object value = getFlag(key, -1);
        if(value instanceof Integer)
            return (Integer)value;
        if(value instanceof Double)
            return ((Double)value).intValue();
            return null;
    }

    public ArrayList<String> getStringList(String key) {return new ArrayList<>((List<String>)getFlag(key, new ArrayList<>()));}

    //A bunch of default getters

    public String getServerID() {return getString("serverID");}
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

    /**
     * Returns a ServerFlags object from a JSON string.
     * @param json The JSON string.
     * @return A ServerFlags object.
     */
    public static ServerFlags readFromJson(String json) {
        return NetworkPlus.gson().fromJson(json, ServerFlags.class);
    }

    public HashMap<String, Object> asMap() {
        return flagMap;
    }

    public ServerMessage asMessage() {
        return asMessage(getString("username"));
    }

    public ServerMessage asMessage(String serverID) {
        return new ServerMessage(serverID, toString());
    }

}