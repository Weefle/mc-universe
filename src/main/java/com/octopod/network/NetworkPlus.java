package com.octopod.network;

import java.io.File;
import java.util.List;

import com.google.gson.Gson;
import com.octopod.network.bukkit.BukkitUtils;
import com.octopod.network.cache.NetworkServerCache;
import com.octopod.network.connection.NetworkConnection;
import com.octopod.network.events.EventManager;
import net.md_5.bungee.api.config.ServerInfo;

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

    /**
     * Gets current information about the server represented by flags.
     * Because it's /current/ information, a new ServerFlags object is created every time this method is used.
     * Try not to overuse this method too much, use merge() to update specific flags of an object instead.
     * @return A new ServerFlags object.
     */
    public static ServerFlags getServerInfo() {
        if(!isLoaded()) return null;
        return new ServerFlags();
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
        return NetworkServerCache.getAllOnlinePlayers();
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
        return NetworkServerCache.findPlayer(player);
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
    
	/**
	 * Gets if the server with this username is currently full of players.
	 * 
	 * @param server
	 *            The username of the server.
	 * @return If the server is full.
	 */
	public static boolean isServerFull(String server) {
		return NetworkServerCache.getOnlinePlayers(server).size() >= NetworkServerCache
				.getInfo(server).getMaxPlayers();
	}

    public static boolean sendPlayer(String player, String server) {
        return getConnection().sendPlayer(player, server);
    }

    public static void sendAllPlayers(String serverFrom, String server) {
        sendMessage(serverFrom, NetworkConfig.getChannel("SENDALL"), new ServerMessage(server));
    }

    public static void sendAllPlayers(String server) {
        broadcastMessage(NetworkConfig.getChannel("SENDALL"), new ServerMessage(server));
    }

    //=========================================================================================//
    //  Request methods
    //=========================================================================================//

    public static void sendMessage(String server, String channel) {
        sendMessage(server, channel, ServerMessage.EMPTY);
    }

    public static void sendMessage(String server, String channel, ServerMessage message) {
        getConnection().sendMessage(server, channel, message.toString());
    }

    public static void sendMessage(List<String> servers, String channel, ServerMessage message) {
        getConnection().sendMessage(servers, channel, message.toString());
    }

    public static void broadcastMessage(String channel) {
        broadcastMessage(channel, ServerMessage.EMPTY);
    }

    public static void broadcastMessage(String channel, ServerMessage message) {
        getConnection().broadcastMessage(channel, message.toString());
    }

    //=========================================================================================//

    /**
     * Tells a server (using this plugin) to broadcast a raw message.
     * @param message The message to send.
     */
    public static void broadcastNetworkMessage(String server, String message) {
        sendMessage(server, NetworkConfig.getChannel("BROADCAST"), new ServerMessage(message));
    }

    /**
     * Tells every server (using this plugin) to broadcast a raw message.
     * @param message The message to send.
     */
    public static void broadcastNetworkMessage(String message) {
        broadcastMessage(NetworkConfig.getChannel("BROADCAST"), new ServerMessage(message));
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
            broadcastMessage(NetworkConfig.getChannel("MESSAGE"), new ServerMessage(player, message));
        }
    }

    /**
     * Broadcasts a message to all servers telling them to send back their info.
     * This method should only be called only when absolutely needed.
     * This might cause messages to be recieved on the SERVER_RESPONSE and SERVER_REQUEST channel.
     */
    public static void requestServerInfo() {
        getLogger().verbose("Requesting info from all servers");
        broadcastMessage(NetworkConfig.getChannel("SERVER_REQUEST"));
    }

    /**
     * Broadcasts a message to a server telling it to send back their info.
     * This method should only be called only when absolutely needed.
     * This might cause messages to be recieved on the SERVER_RESPONSE and SERVER_REQUEST channel.
     * @param server The server to request information from.
     */
    public static void requestServerInfo(String server) {
        getLogger().verbose("Requesting info from &a" + server);
        sendMessage(server, NetworkConfig.getChannel("SERVER_REQUEST"));
    }

    public static void sendServerInfo(String server) {
        NetworkPlus.sendMessage(server, NetworkConfig.getChannel("SERVER_RESPONSE"), getServerInfo().toMessage());
    }

    public static void broadcastServerInfo() {
        NetworkPlus.broadcastMessage(NetworkConfig.getChannel("SERVER_RESPONSE"), getServerInfo().toMessage());
    }

}
