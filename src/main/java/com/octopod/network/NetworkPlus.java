package com.octopod.network;

import com.google.gson.Gson;
import com.octopod.network.cache.NetworkPlayerCache;
import com.octopod.network.connection.NetworkConnection;
import com.octopod.network.events.EventManager;
import com.octopod.network.bukkit.BukkitUtils;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * @author Octopod
 *         Created on 3/13/14
 */
public class NetworkPlus {

    public NetworkPlus(NetworkPlusPlugin plugin) {
        instance = this;
        this.plugin = plugin;
    }

    /**
     * A prefix to use before server messages.
     * TODO: add this to NetworkConfig
     */
    private static final String messagePrefix = "&8[&6Net+&8] &7";

    /**
     * An instance of Gson. Instead of always making new instances, just use this one.
     */
    private static final Gson gson = new Gson();

    /**
     * The current instance of NetworkPlus.
     */
    private static NetworkPlus instance;

    /**
     * The current instance of the NetworkPlusPlugin.
     */
    private static NetworkPlusPlugin plugin;

    /**
     * Gets the current instance of Gson.
     * @return
     */
    public static Gson gson() {return gson;}

    public static String prefix() {return messagePrefix;}

    public static boolean isLoaded() {
        return (plugin != null);
    }

    public static NetworkPlus getInstance() {
        return instance;
    }

    public static NetworkPlusPlugin getPlugin() {
        return plugin;
    }

    /**
     * Gets this plugin's username on LilyPad.
     * @return This plugin's username.
     */
    public static String getUsername() {
        return getConnection().getUsername();
    }

    public static boolean isTestBuild() {
        return getPluginVersion().equals("TEST_BUILD");
    }

    public static String getPluginVersion() {
        return getPlugin().getDescription().getVersion();
    }

    public static NetworkLogger getLogger() {
        if(!isLoaded()) return null;
        return plugin.logger();
    }

    public static ServerInfo getServerInfo() {
        if(!isLoaded()) return null;
        return plugin.getServerInfo();
    }

    public static NetworkConnection getConnection() {
        if(!isLoaded()) return null;
        return plugin.getConnection();
    }

    public static File getDataFolder() {
        if(!isLoaded()) return null;
        return plugin.getDataFolder();
    }

    /**
     * Returns if LilyPad is connected or not.
     * @return true, if Lilypad is connected.
     */
    public static boolean isConnected() {
        return (getConnection() != null && getConnection().isConnected());
    }

    /**
     * Gets this plugin's event manager, which is used to register custom events.
     * @return Network's EventManager.
     */
    public static EventManager getEventManager() {
        return EventManager.getManager();
    }

    //=========================================================================================//
    //  Player Cache methods
    //=========================================================================================//

    /**
     * Gets all the players on the network as a Set.
     * @return The Set containing all the players, or an empty Set if the request somehow fails.
     */
    public static List<String> getNetworkedPlayers() {
        return getConnection().getPlayers();
    }

    /**
     * Gets all the players on the network according to the cache.
     * @return The Set containing all the players.
     */
    public static Set<String> getCachedPlayers() {
        return NetworkPlayerCache.getPlayers();
    }

    /**
     * Gets if the player is online (on the entire network)
     * @param player The name of the player.
     * @return If the player is online.
     */
    public static boolean isPlayerOnline(String player) {
        return getNetworkedPlayers().contains(player);
    }

    //=========================================================================================//
    //  Server Cache methods
    //=========================================================================================//

    /**
     * Gets if the server with this username is online.
     * @param server The username of the server.
     * @return If the server is online.
     */
    public static boolean isServerOnline(String server) {
        return getConnection().serverExists(server);
    }

    public static boolean sendPlayer(String player, String server) {
        return getConnection().sendPlayer(player, server);
    }

    public static void sendAllPlayers(String serverFrom, String server) {
        sendMessage(serverFrom, NetworkConfig.getChannel("SENDALL"), server);
    }

    public static void sendAllPlayers(String server) {
        broadcastMessage(NetworkConfig.getChannel("SENDALL"), server);
    }

    //=========================================================================================//
    //  Request methods
    //=========================================================================================//

    public static void sendMessage(String server, String channel, String message) {
        getConnection().sendMessage(server, channel, message);
    }

    public static void sendMessage(List<String> servers, String channel, String message) {
        getConnection().sendMessage(servers, channel, message);
    }

    public static void broadcastMessage(String channel, String message) {
        getConnection().broadcastMessage(channel, message);
    }

    /**
     * Tells a server (using this plugin) to broadcast a raw message.
     * @param message The message to send.
     */
    public static void broadcastNetworkMessage(String server, String message) {
        sendMessage(server, NetworkConfig.getChannel("BROADCAST"), message);
    }

    /**
     * Tells every server (using this plugin) to broadcast a raw message.
     * @param message The message to send.
     */
    public static void broadcastNetworkMessage(String message) {
        broadcastMessage(NetworkConfig.getChannel("BROADCAST"), message);
    }

    /**
     * Sends a raw message to a player. Works cross-server.
     * The message will just be sent locally if the player is online on this server.
     * @param player The name of the player.
     * @param message The message to send.
     */
    public static void sendNetworkMessage(String player, String message) {
        if(BukkitUtils.isPlayerOnline(player)) {
            BukkitUtils.sendMessage(player, message);
        } else {
            broadcastMessage(NetworkConfig.getChannel("MESSAGE"), gson().toJson(new PreparedPlayerMessage(player, message)));
        }
    }

    /**
     * Broadcasts a message to all servers telling them to send back their info.
     * This method should only be called only when absolutely needed, as the info returned never changes.
     * This might cause messages to be recieved on the CHANNEL_INFO_RESPONSE channel.
     */
    public static void requestServerInfo() {
        getLogger().verbose("Requesting info from all servers");
        broadcastMessage(NetworkConfig.getChannel("INFO_REQUEST"), gson().toJson(getServerInfo()));
    }

    /**
     * Broadcasts a message to a list of servers telling them to send back their info.
     * This method should only be called only when absolutely needed, as the info returned never changes.
     * This might cause messages to be recieved on the CHANNEL_INFO_RESPONSE channel.
     * @param servers The list of servers to message.
     */
    public static void requestServerInfo(List<String> servers) {
        getLogger().verbose("Requesting info from: &a" + servers);
        sendMessage(servers, NetworkConfig.getChannel("INFO_REQUEST"), gson().toJson(getServerInfo()));
    }

    /**
     * Broadcasts a message to a list of servers telling them to send back a list of their players.
     * This method should only be called only when absolutely needed, as the PlayerCache should automatically change it.
     * This might cause messages to be recieved on the CHANNEL_PLAYERLIST_RESPONSE channel.
     */
    public static void requestPlayerList() {
        getLogger().verbose("Requesting playerlist from all servers");
        broadcastMessage(NetworkConfig.getChannel("PLAYERLIST_REQUEST"), gson().toJson(BukkitUtils.getPlayerNames()));
    }

    /**
     * Broadcasts a message telling every server to uncache a server.
     * @param server
     */
    public static void requestUncache(String server) {
        broadcastMessage(NetworkConfig.getChannel("UNCACHE"), server);
    }
}
