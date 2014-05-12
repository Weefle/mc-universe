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
     * TODO: Make this HashMap a <String, String> instead. Need to settle on a way to serialize String arguments for lists first.
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

            //Status of the server (true if online, false if not)
            flags.setFlag("serverStatus", true);

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

    /**
     * Sets a flag to a value.
     * @param k The key.
     * @param v The value.
     */
    public void setFlag(String k, Object v) {
        flagMap.put(k, v);
    }

    /**
     * Gets the flag by key.
     * @param k The key.
     * @param def The value to return if the key doesn't exist.
     * @return The value located at the key, or 'def' if the key doesn't exist.
     */
    public Object getFlag(String k, Object def) {
        Object val = flagMap.get(k);
        if(val == null)
            return def;
            return val;
    }

    public void setString(String k, String v) {setFlag(k, v);}
    public void setInteger(String k, Integer v) {setFlag(k, v);}
    public void setStringList(String k, ArrayList<String> v) {setFlag(k, v);}

    public String getString(String k) {return (String) getFlag(k, "[UNKNOWN]");}
    public Integer getInteger(String k) {return ((Double)getFlag(k, -1)).intValue() ;}
    public ArrayList<String> getStringList(String k) {return new ArrayList<>((List<String>) getFlag(k, new ArrayList<String>()));}

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
        return asMessage((String)getFlag("username", null));
    }

    public ServerMessage asMessage(String serverID) {
        return new ServerMessage(serverID, toString());
    }

}