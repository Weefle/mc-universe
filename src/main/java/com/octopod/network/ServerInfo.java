package com.octopod.network;

import com.octopod.network.bukkit.BukkitUtils;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Octopod
 *         Created on 3/13/14
 */

/**
 * The ServerInfo object.
 * IF YOU EVER CHANGE THIS PROTOCOL, ADD IT TO THE END, DON'T MIX IT UP
 * Protocol:
 *  - Username
 *  - Server name (or username)
 *  - Description (or MOTD)
 *  - Max players
 *  - Whitelisted Players, or nothing if whitelist is off. (space separated)
 */
public class ServerInfo {

    private List<Object> arguments;

    public int size() {return arguments.size();}

    /**
     * Generates the ServerInfo for this server. It should be only used once on startup/reload.
     * Also, it should be changed along with the protocol.
     */
    public ServerInfo() {
        this(
            NetworkPlus.getUsername(), //Server's username
            NetworkConfig.getServerName(), //Server's config name
            Bukkit.getServer().getMotd(), //Server's MOTD
            Bukkit.getServer().getMaxPlayers(), //Server's max players
            Arrays.asList(BukkitUtils.getWhitelistedPlayerNames()), //Server's whitelisted players
            NetworkConfig.isHub() ? NetworkConfig.getHubPriority() : -1, //Server's hub priority, or -1 if is not a hub.
            NetworkPlus.getPluginVersion(), //Server's plugin version. (<build>-<commit>)
            Arrays.asList(BukkitUtils.getPlayerNames()) //Server's online players.
        );
    }

    /**
     * Creates a ServerInfo instance by arguments. Should almost never be used,
     * as Gson will deserialize jsons for ServerInfo objects
     * @param args
     */
    private ServerInfo(Object... args)
    {
        arguments = Arrays.asList(args);
    }

    public String   getUsername()               {return getIndex(0);}
    public String   getServerName()             {return getIndex(1);}
    public String   getDescription()            {return getIndex(2);}
    public Integer  getMaxPlayers()             {return getInt(3, 0);}
    public List<String> getWhitelistedPlayers() {return get(4, new ArrayList<String>(), (Class<ArrayList<String>>)(Class<?>)ArrayList.class);}
    public Integer  getHubPriority()            {return getInt(5, -1);}
    public String   getPluginVersion()          {return getIndex(6);}
    public List<String> getPlayers()            {return get(7, new ArrayList<String>(), (Class<ArrayList<String>>)(Class<?>)ArrayList.class);}

    //Tries to get a String from the index, and returns "" if it doesn't exist.
    private String getIndex(int n) {
        try {
            return arguments.get(n).toString();
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    private <T> T get(int n, T def, Class<T> type) {
        try {
            return (T)arguments.get(n);
        } catch (Exception e) {
            return def;
        }
    }

    //Tries to get an integer from the screen, but returns 0 if it doesn't
    private Integer getInt(int n, int def) {
        Object o = arguments.get(n);
        if(!(o instanceof Integer)) return def;
        return (Integer)o;
    }

    public String toString() {
        return NetworkPlus.gson().toJson(this);
    }

}