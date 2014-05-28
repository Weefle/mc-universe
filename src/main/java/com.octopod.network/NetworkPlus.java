package com.octopod.network;

import java.io.File;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.octopod.network.bukkit.BukkitUtils;
import com.octopod.network.connection.NetworkConnection;
import com.octopod.network.events.EventManager;
import com.octopod.network.server.ServerManager;

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
    private static final Gson gson = new GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create();

    /**
     * The current instance of NetworkPlus.
     */
    private static NetworkPlus instance;

    /**
     * The current instance of the NetworkPlusPlugin.
     */
    private static NetworkPlusPlugin plugin;

    /**
     * The current instance of EventManager.
     */
    private static EventManager eventManager = new EventManager();

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
    public static String getServerID() {
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

    /**
     * Gets this servers' flags from the cache.
     * @return This server's flags from the cache, or null if the plugin isn't loaded or flags not in cache.
     */
    public static ServerFlags getServerFlags() {
        if(!isLoaded()) return null;
        return getServerFlags(getServerID());
    }

    public static ServerFlags getServerFlags(String serverID) {
        if(!isLoaded()) return null;
        return ServerManager.getFlags(serverID);
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
        return eventManager;
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
     * @return The List containing all the players.
     */
    public static List<String> getCachedPlayers() {
        return ServerManager.getAllOnlinePlayers();
    }

    /**
     * Gets if the player is online (on the entire network)
     * @param player The name of the player.
     * @return If the player is online.
     */
    public static boolean isPlayerOnline(String player) {
        return getNetworkedPlayers().contains(player);
    }

    public static String findPlayer(String player) {
        return ServerManager.findPlayer(player);
    }

    //=========================================================================================//
    //  Server Cache methods
    //=========================================================================================//

    /**
     * Gets if the server with this username is online.
     * @param serverID The username of the server.
     * @return If the server is online.
     */
    public static boolean isServerOnline(String serverID) {
        return getConnection().serverExists(serverID);
    }
    
	/**
	 * Gets if the server with this username is currently full of players.
	 * 
	 * @param server The username of the server.
	 * @return If the server is full.
	 */
	public static boolean isServerFull(String server) {
        ServerFlags flags = ServerManager.getFlags(server);
        if(flags == null) {
            return false;
        } else {
		    return ServerManager.getOnlinePlayers(server).size() >= flags.getMaxPlayers();
        }
	}

    public static boolean sendPlayer(String player, String serverID) {
        return getConnection().sendPlayer(player, serverID);
    }

    /**
     * Sends all players on a server to another server.
     * @param serverFrom The server where the players are from.
     * @param serverID The server the players will be sent to.
     */
    public static void sendAllPlayers(String serverFrom, String serverID) {
        sendMessage(serverFrom, NetworkMessageChannel.SERVER_SENDALL.toString(), new ServerMessage(serverID));
    }

    /**
     * Sends all players ON THE ENTIRE NETWORK to a server.
     * @param serverID The server the players will be sent to.
     */
    public static void sendAllPlayers(String serverID) {
        broadcastMessage(NetworkMessageChannel.SERVER_SENDALL.toString(), new ServerMessage(serverID));
    }

    //=========================================================================================//
    //  Request methods
    //=========================================================================================//

    @Deprecated
    public static void sendMessage(String serverID, String channel) {
        sendMessage(serverID, channel, ServerMessage.EMPTY);
    }

    @Deprecated
    public static void sendMessage(String serverID, String channel, ServerMessage message) {
        getConnection().sendMessage(serverID, channel, message.toString());
    }

    @Deprecated
    public static void sendMessage(List<String> serverIDs, String channel, ServerMessage message) {
        getConnection().sendMessage(serverIDs, channel, message.toString());
    }

    public static void sendMessage(String serverID, NetworkMessageChannel channel, ServerMessage message) {
        sendMessage(serverID, channel.toString(), message);
    }

    public static void sendMessage(String serverID, NetworkMessageChannel channel) {
        sendMessage(serverID, channel.toString());
    }

    public static void broadcastMessage(NetworkMessageChannel channel, ServerMessage message) {
        broadcastMessage(channel.toString(), message);
    }

    public static void broadcastMessage(NetworkMessageChannel channel) {
        broadcastMessage(channel.toString());
    }

    @Deprecated
    public static void broadcastMessage(String channel) {
        broadcastMessage(channel, ServerMessage.EMPTY);
    }

    @Deprecated
    public static void broadcastMessage(String channel, ServerMessage message) {
        getConnection().broadcastMessage(channel, message.toString());
    }

    //=========================================================================================//

    /**
     * Tells a server (using this plugin) to broadcast a raw message.
     * @param message The message to send.
     */
    public static void broadcastNetworkMessage(String serverID, String message) {
        sendMessage(serverID, NetworkMessageChannel.SERVER_ALERT, new ServerMessage(message));
    }

    /**
     * Tells every server (using this plugin) to broadcast a raw message.
     * @param message The message to send.
     */
    public static void broadcastNetworkMessage(String message) {
        broadcastMessage(NetworkMessageChannel.SERVER_ALERT, new ServerMessage(message));
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
            broadcastMessage(NetworkMessageChannel.PLAYER_MESSAGE, new ServerMessage(player, message));
        }
    }

    /**
     * Broadcasts a message to all servers telling them to send back their info.
     * This method should only be called only when absolutely needed.
     * This might cause messages to be recieved on the SERVER_RESPONSE and SERVER_REQUEST channel.
     */
    public static void requestServerFlags() {
        getLogger().log(3, "Requesting info from all servers");
        broadcastMessage(NetworkMessageChannel.SERVER_FLAGS_REQUEST);
    }

    /**
     * Broadcasts a message to a server telling it to send back their info.
     * This method should only be called only when absolutely needed.
     * This might cause messages to be recieved on the SERVER_RESPONSE and SERVER_REQUEST channel.
     * @param serverID The server to request information from.
     */
    public static void requestServerFlags(String serverID) {
        getLogger().log(3, "Requesting info from &a" + serverID);
        sendMessage(serverID, NetworkMessageChannel.SERVER_FLAGS_REQUEST);
    }

    public static void sendServerFlags(String serverID) {
        sendServerFlags(serverID, getServerFlags(), getServerID());
    }

    /**
     * Sends a server a ServerFlags object.
     * @param serverID The ID of the server to send it to.
     * @param flags The ServerFlags object.
     */
    public static void sendServerFlags(String serverID, ServerFlags flags, String serverIDOwnedBy) {
        getLogger().log(3, "Sending server info to &a" + serverID);
        NetworkPlus.sendMessage(serverID, NetworkMessageChannel.SERVER_FLAGS_CACHE, flags.toServerMessage(serverIDOwnedBy));
    }

    public static void broadcastServerFlags(ServerFlags flags) {
        broadcastServerFlags(getServerID(), flags);
    }

    /**
     * Broadcasts a ServerFlags object.
     * @param flags The ServerFlags object.
     */
    public static void broadcastServerFlags(String serverIDOwnedBy, ServerFlags flags)
    {
        getLogger().log(3, "Sending server info to all servers");
        NetworkPlus.broadcastMessage(NetworkMessageChannel.SERVER_FLAGS_CACHE, flags.toServerMessage(serverIDOwnedBy));
    }

}
