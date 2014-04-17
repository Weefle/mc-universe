package com.octopod.network;

import com.google.gson.JsonSyntaxException;
import com.octopod.network.cache.NetworkHubCache;
import com.octopod.network.cache.NetworkServerCache;
import com.octopod.network.events.network.NetworkConnectedEvent;
import com.octopod.network.events.player.NetworkPlayerJoinEvent;
import com.octopod.network.events.player.NetworkPlayerLeaveEvent;
import com.octopod.network.events.relays.MessageEvent;
import com.octopod.network.events.server.*;
import com.octopod.network.bukkit.BukkitUtils;

import java.util.HashMap;

/**
 * @author Octopod
 */
public class NetworkListener {

    /**
     * Listens for when this plugin is connected according to the NetworkConnection instance.
     * This method should request ServerInfo and playerlists from every server.
     */
    public static void connectNetwork()
    {
        NetworkConnectedEvent event = new NetworkConnectedEvent();

        NetworkPlus.getEventManager().triggerEvent(event);

        BukkitUtils.broadcastMessage(
                "&7Successfully connected via &a" +
                NetworkPlus.getConnection().getConnectionType() +
                "&7!"
        );

        NetworkPlus.requestServerInfo();
    }

    public static void recieveServer(String server, HashMap<String, Object> info) {

        ServerRecievedEvent event = new ServerRecievedEvent(server, info);

        NetworkPlus.getEventManager().triggerEvent(event);

        if(!event.isCancelled()) {

            //If the server doesn't exist, request information from that server.
            if(!NetworkServerCache.serverExists(server)) {
                discoverServer(server, info);
            } else {
                updateServer(server, options);
            }

        }

    }

    /**
     * Fires when a ServerInfo is recieved from a server that isn't cached.
     * This method should cache the ServerInfo under the server it was sent from.
     * It should also update the NetworkPlayerCache.
     * @param server The name of the server who sent the information.
     * @param info
     */
    public static void discoverServer(String server, HashMap<String, Object> info)
    {
        ServerDiscoveredEvent event = new ServerDiscoveredEvent(server, info);

        NetworkPlus.getEventManager().triggerEvent(event);

        if(!event.isCancelled())
        {
            ServerFlags serverInfo = NetworkServerCache.addServer(server, info);

            //Checks if this server is a hub (priority above 0)
            int hubPriority = serverInfo.getHubPriority();
            if(hubPriority >= 0) {
                NetworkHubCache.addHub(server, hubPriority);
                NetworkPlus.getLogger().info("Server &a" + server + "&7 registered as hub @ priority &e" + hubPriority);
            }

            //Version checking
            if(!NetworkPlus.isTestBuild()) {
                //Checks for mismatched plugin versions between servers, and warns the server owners.
                String version = serverInfo.getVersion();

                if(!version.equals("TEST_BUILD") && !version.equals(NetworkPlus.getPluginVersion())) {
                    NetworkPlus.getLogger().info("&a" + server + "&7: Running &6Net+&7 version &6" + (version.equals("") ? "No Version" : version));
                }
            }

            NetworkPlus.sendServerInfo(server);
            NetworkPlus.getEventManager().triggerEvent(new PostServerDiscoveredEvent(serverInfo));
        }
    }

    /**
     * Listens for when an uncache request is sent from any server.
     * This method should uncache the requested server.
     */
    public static void clearServer(String server, String requester)
    {
        ServerClearedEvent event = new ServerClearedEvent(server, requester);

        NetworkPlus.getEventManager().triggerEvent(event);

        NetworkServerCache.removeServer(event.getClearedServer());
    }

    /**
     * Listens for when any player joins the network.
     * This method should cache the player into the server they joined into.
     */
    public static void firePlayerJoinEvent(String player, String server)
    {
        NetworkPlayerJoinEvent event = new NetworkPlayerJoinEvent(player, server);

        NetworkPlus.getEventManager().triggerEvent(event);
    }

    /**
     * Listens for when any player leaves the network.
     * This method should uncache the player.
     */
    public static void firePlayerLeaveEvent(String player, String server)
    {
        NetworkPlayerLeaveEvent event = new NetworkPlayerLeaveEvent(player, server);

        NetworkPlus.getEventManager().triggerEvent(event);

        BukkitUtils.console("&b" + event.getPlayer() + " &7left &a" + event.getServer());
    }

    /**
     * Listens for when any message is recieved from any server.
     * This method is a gateway to other events and features.
     */
    public static void recieveMessage(String sender, String channel, String message)
    {
        //Return if sender is this server; Cannot self-message.
        if(sender.equals(NetworkPlus.getUsername())) return;

        MessageEvent event = new MessageEvent(sender, channel, message);

        NetworkPlus.getEventManager().triggerEvent(event);

        if(event.isCancelled()) return;

        //Tells the server to send all players on this server to the server specified in 'message'
		if(channel.equals(NetworkConfig.getChannel("SENDALL")))
		{
            for(String player: BukkitUtils.getPlayerNames())
                NetworkPlus.sendPlayer(player, message);
		}

        //Tells the server to send a message to a player. Information included in 'message'
		if(channel.equals(NetworkConfig.getChannel("MESSAGE")))
		{
			NetworkPlus.gson().fromJson(message, PreparedPlayerMessage.class).send();
		}

        //Tells the server a player has joined the network
		if(channel.equals(NetworkConfig.getChannel("PLAYER_JOIN")))
			firePlayerJoinEvent(message, sender);

        //Tells the server a player has left the network
		if(channel.equals(NetworkConfig.getChannel("PLAYER_LEAVE")))
			firePlayerLeaveEvent(message, sender);

		//Tells the server to broadcast a message on the server.
		if(channel.equals(NetworkConfig.getChannel("BROADCAST")))
		    BukkitUtils.broadcastMessage(message);

		/**
		 * Server Request
         * Sends back this server's ServerInfo object converted into a string.
		 */
		if(channel.equals(NetworkConfig.getChannel("SERVER_REQUEST")))
        {
            NetworkPlus.sendMessage(sender, NetworkConfig.getChannel("SERVER_RESPONSE"),
                    NetworkPlus.gson().toJson(NetworkPlus.getServerInfo())
            );
        }

        /**
         * Server Response
         * Attempts to convert the message into a HashMap object and cache it.
         */
        if(channel.equals(NetworkConfig.getChannel("SERVER_RESPONSE")))
        {
            try {
                recieveServer(sender, Util.mapFromJson(message));
            } catch (JsonSyntaxException e) {
                NetworkPlus.getLogger().info("Server &a" + sender + " &7has sent an invalid ServerInfo.");
            } catch (NullPointerException e) {
                NetworkPlus.getLogger().info("Server &a" + sender + " &7has sent a null ServerInfo.");
                BukkitUtils.console(e.getMessage());
            }
        }

		if(channel.equals(NetworkConfig.getChannel("CLEAR_REQUEST")))
		{		
			NetworkPlus.getEventManager().triggerEvent(new ServerClearedEvent(message, sender));
		}
		
	}

}
