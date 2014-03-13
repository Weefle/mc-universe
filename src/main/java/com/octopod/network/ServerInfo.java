package com.octopod.network;

import com.octopod.network.util.BukkitUtils;
import com.octopod.octolib.common.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;

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

    private List<String> arguments;

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
            StringUtils.implode(BukkitUtils.getWhitelistedPlayerNames(), " "), //Server's whitelisted players
            NetworkConfig.isHub() ? NetworkConfig.getHubPriority() : -1, //Server's hub priority, or -1 if is not a hub.
            NetworkPlus.getPluginVersion() //Server's plugin version. (<build>-<commit>)
        );
    }

    /**
     * Creates a ServerInfo instance by arguments. Should almost never be used,
     * as Gson will deserialize jsons for ServerInfo objects
     * @param args
     */
    private ServerInfo(Object... args)
    {
        arguments = new ArrayList<>();
        for(Object arg: args) arguments.add(arg.toString());
    }

    public String   getUsername()           {return getIndex(0);}
    public String   getServerName()         {return getIndex(1);}
    public String   getDescription()        {return getIndex(2);}
    public Integer  getMaxPlayers()         {return getInt(getIndex(3), 0);}
    public String[] getWhitelistedPlayers() {return getIndex(4).equals("") ? new String[0] : getIndex(4).split(" ");}
    public Integer  getHubPriority()        {return getInt(getIndex(5), -1);}
    public String   getPluginVersion()      {return getIndex(6);}

    //Tries to get a String from the index, and returns "" if it doesn't exist.
    private String getIndex(int n) {
        try {
            return arguments.get(n);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    //Tries to get an integer from the screen, but returns 0 if it doesn't
    private Integer getInt(String n, int def) {
        try {
            return Integer.valueOf(n);
        } catch (NumberFormatException e) {
            return def;
        }
    }

}