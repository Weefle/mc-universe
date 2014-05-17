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
     * Returns a ServerFlags object from a JSON string.
     * @param json The JSON string.
     * @return A ServerFlags object.
     */
    public static ServerFlags fromJson(String json) {
        return NetworkPlus.gson().fromJson(json, ServerFlags.class);
    }

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
            flags.setFlag("maxPlayers", Bukkit.getServer().getMaxPlayers());

            //If the server's whitelist is enabled
            flags.setFlag("serverWhitelistEnabled", Bukkit.getServer().hasWhitelist());

            //Server's list of whitelisted players (empty list if whitelist is disabled)
            flags.setFlag("whitelistedPlayers", new ArrayList<>(Arrays.asList(BukkitUtils.getWhitelistedPlayerNames())));

            //Server's hub priority (-1 if not a hub)
            flags.setFlag("hubPriority", NetworkConfig.isHub() ? NetworkConfig.getHubPriority() : -1);

            //Server's plugin version
            flags.setFlag("version", NetworkPlus.getPluginVersion());

            //Server's list of online players
            flags.setFlag("onlinePlayers", new ArrayList<>(Arrays.asList(BukkitUtils.getPlayerNames())));

            //Players queued for this server.
            flags.setFlag("queuedPlayers", NetworkQueueManager.instance.getQueueMembers());

            //Time the server was last online (-1 if it's online right now)
            flags.setFlag("serverLastOnline", -1L);

            return flags;
        }

    };

    public static ServerFlags generateFlags() {
        return currentGenerator.generate();
    }

    /**
     * Merges the map into this instance's option map.
     * Used to "patch" the current flagMap.
     * @param flags The ServerFlags to copy flags from.
     */
    public void merge(ServerFlags flags) {
        flagMap.putAll(flags.toMap());
    }

    public boolean hasFlag(String key) {
        return flagMap.containsKey(key);
    }

    /**
     * Sets a flag to a value.
     * @param key The key.
     * @param value The value.
     */
    public void setFlag(String key, Object value) {
        flagMap.put(key, value);
    }

    /**
     * Gets the flag by key.
     * @param key The key.
     * @return The value located at the key, or 'defaultValue' if the key doesn't exist.
     */
    @SuppressWarnings("unchecked")
    public <T> T getFlag(String key, Class<T> clazz, T defaultValue)
    {
        Object value;

        if((value = flagMap.get(key)) == null) return defaultValue;

        if(!clazz.isInstance(value)) {
            throw new ClassCastException("Value for server flag '" + key + "' is not an instance of " + clazz.getName());
        } else {
            return (T)value;
        }
    }

    public <T> T getFlag(String key, Class<T> clazz) {
        return getFlag(key, clazz, null);
    }

    public Object getFlag(String key) {
        return getFlag(key, Object.class);
    }

    public String getFlagString(String key) {
        return getFlag(key, String.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> getFlagStringList(String key) {
        return getFlag(key, (Class<List<String>>)(Class<?>)List.class);
    }

    public int getFlagInteger(String key) {
        Object value = getFlag(key);
        if(value != null) {
            if(value instanceof Integer)
                return (Integer)value;
            if(value instanceof Double)
                return ((Double)value).intValue();
        }
        return -1;
    }

    public long getFlagLong(String key) {
        Object value = getFlag(key);
        if(value != null) {
            if(value instanceof Long)
                return (Long)value;
            if(value instanceof Integer)
                return (Integer)value;
            if(value instanceof Double)
                return ((Double)value).intValue();
        }
        return -1L;
    }

    public boolean getFlagBoolean(String key) {
        return getFlag(key, Boolean.class);
    }

    //A bunch of default getters

    public String getServerID()     {return getFlagString("serverID");}
    public String getServerName()   {return getFlagString("serverName");}
    public String getDescription()  {return getFlagString("description");}
    public String getVersion()      {return getFlagString("version");}

    public Integer getMaxPlayers()  {return getFlagInteger("maxPlayers");}
    public Integer getHubPriority() {return getFlagInteger("hubPriority");}

    public List<String> getWhitelistedPlayers() {return getFlagStringList("whitelistedPlayers");}
    public List<String> getOnlinePlayers()      {return getFlagStringList("onlinePlayers");}
    public List<String> getQueuedPlayers()      {return getFlagStringList("queuedPlayers");}

    /**
     * Gets this object returned as a JSON string.
     * @return The JSON representation of this object.
     */
    @Override
    public String toString() {
        return NetworkPlus.gson().toJson(this);
    }

    public HashMap<String, Object> toMap() {
        return flagMap;
    }

    public ServerMessage toServerMessage() {
        return toServerMessage(getFlagString("username"));
    }

    public ServerMessage toServerMessage(String serverID) {
        return new ServerMessage(serverID, toString());
    }

}