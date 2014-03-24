package com.octopod.network;

import com.google.gson.JsonSyntaxException;
import com.octopod.network.cache.NetworkHubCache;
import com.octopod.network.cache.NetworkPlayerCache;
import com.octopod.network.cache.NetworkServerCache;
import com.octopod.network.events.network.NetworkConnectedEvent;
import com.octopod.network.events.player.NetworkPlayerJoinEvent;
import com.octopod.network.events.player.NetworkPlayerLeaveEvent;
import com.octopod.network.events.player.NetworkPlayerRedirectEvent;
import com.octopod.network.events.relays.MessageEvent;
import com.octopod.network.events.server.ServerAddedEvent;
import com.octopod.network.events.server.ServerClearEvent;
import com.octopod.network.events.server.ServerFoundEvent;
import com.octopod.network.bukkit.BukkitUtils;

/**
 * @author Octopod
 */
public class NetworkListener {

    /**
     * Listens for when this plugin is connected according to the NetworkConnection instance.
     * This method should request ServerInfo and playerlists from every server.
     */
    public static void triggerNetworkConnected()
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

    /**
     * Listens for when a ServerInfo object is recieved from any server.
     * This method should cache the ServerInfo under the server it was sent from.
     * It should also update the NetworkPlayerCache.
     * @param serverInfo
     */
    public static void triggerServerFound(ServerInfo serverInfo)
    {
        ServerFoundEvent event = new ServerFoundEvent(serverInfo);

        NetworkPlus.getEventManager().triggerEvent(event);

        if(!event.isCancelled())
        {
            String server = event.getServer();

            NetworkServerCache.addServer(serverInfo);

            //Update player locations in the NetworkPlayerCache
            for(String player: serverInfo.getPlayers()) {
                NetworkPlayerCache.putPlayer(player, server);
            }

            //Checks if this server is a hub (priority above 0)
            int hubPriority = serverInfo.getHubPriority();
            if(hubPriority >= 0) {
                NetworkHubCache.addHub(server, hubPriority);
                NetworkPlus.getLogger().info("Server &a" + server + "&7 registered as hub @ priority &e" + hubPriority);
            }

            //Version checking
            if(!NetworkPlus.isTestBuild()) {
                //Checks for mismatched plugin versions between servers, and warns the server owners.
                String version = serverInfo.getPluginVersion();

                if(!version.equals("TEST_BUILD") && !version.equals(NetworkPlus.getPluginVersion())) {
                    NetworkPlus.getLogger().info("&a" + server + "&7: Running &6Net+&7 version &6" + (version.equals("") ? "No Version" : version));
                }
            }

            NetworkPlus.getEventManager().triggerEvent(new ServerAddedEvent(serverInfo));
        }
    }

    /**
     * Listens for when an uncache request is sent from any server.
     * This method should uncache the requested server.
     */
    public static void triggerServerClear(String server, String requester)
    {
        ServerClearEvent event = new ServerClearEvent(server, requester);

        NetworkPlus.getEventManager().triggerEvent(event);

        NetworkServerCache.removeServer(event.getClearedServer());
    }

    /**
     * Listens for when any player joins the network.
     * This method should cache the player into the server they joined into.
     */
    public static void triggerPlayerJoin(String player, String server)
    {
        NetworkPlayerJoinEvent event = new NetworkPlayerJoinEvent(player, server);

        NetworkPlus.getEventManager().triggerEvent(event);

        NetworkPlus.getLogger().debug("&b" + event.getPlayer() + " &7joined network through &a" + event.getServer());
        NetworkPlayerCache.putPlayer(event.getPlayer(), event.getServer());
    }

    /**
     * Listens for when any player leaves the network.
     * This method should uncache the player.
     */
    public static void triggerPlayerLeave(String player, String server)
    {
        NetworkPlayerLeaveEvent event = new NetworkPlayerLeaveEvent(player, server);

        NetworkPlus.getEventManager().triggerEvent(event);

        NetworkPlus.getLogger().debug("&b" + event.getPlayer() + " &7left network through &a" + event.getServer());
        NetworkPlayerCache.removePlayer(event.getPlayer());
    }

    /**
     * Listens for when any player moves from one server to another.
     * This method should change the player's entry in the cache to the new server.
     */
    public static void triggerPlayerRedirect(String player, String from, String server)
    {
        NetworkPlayerRedirectEvent event = new NetworkPlayerRedirectEvent(player, from, server);

        NetworkPlus.getEventManager().triggerEvent(event);

        NetworkPlus.getLogger().debug("&b" + event.getPlayer() + " &7redirected from &a" + event.getFromServer() + "&7 to &a" + event.getServer());
        NetworkPlayerCache.putPlayer(event.getPlayer(), event.getServer());
    }

    /**
     * Listens for when any message is recieved from any server.
     * This method is a gateway to other events and features.
     */
    public static void triggerMessageRecieved(String sender, String channel, String message)
    {
        MessageEvent event = new MessageEvent(sender, channel, message);

        NetworkPlus.getEventManager().triggerEvent(event);

        if(event.isCancelled()) return;

        NetworkPlus.getLogger().verbose("&7Message: &a" + sender + "&7 on &b" + channel);

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
			triggerPlayerJoin(message, sender);

        //Tells the server a player has left the network
		if(channel.equals(NetworkConfig.getChannel("PLAYER_LEAVE")))
			triggerPlayerLeave(message, sender);

        //Tells the server a player has switched servers on the network
		if(channel.equals(NetworkConfig.getChannel("PLAYER_REDIRECT"))) {
            String serverFrom = NetworkPlayerCache.findPlayer(message);
            if(serverFrom == null) {
                triggerPlayerJoin(message, sender);
            } else {
			    triggerPlayerRedirect(message, NetworkPlayerCache.findPlayer(message), sender);
            }
        }

		//Tells the server to broadcast a message on the server.
		if(channel.equals(NetworkConfig.getChannel("BROADCAST")))
		    BukkitUtils.broadcastMessage(message);

		/**
		 * Server Request
         * Servers that recieve this message will send back server information.
		 */
		if(channel.equals(NetworkConfig.getChannel("SERVER_REQUEST")) || channel.equals(NetworkConfig.getChannel("SERVER_RESPONSE")))
        {
            try {
                ServerInfo serverInfo = NetworkPlus.gson().fromJson(message, ServerInfo.class);
                if(serverInfo == null) throw new NullPointerException("Server '" + sender + "' gave us a null serverinfo, json: " + message);
                triggerServerFound(serverInfo);
            } catch (JsonSyntaxException e) {
                NetworkPlus.getLogger().info("Server &a" + sender + " &7has sent an invalid ServerInfo.");
            } catch (NullPointerException e) {
                NetworkPlus.getLogger().info("Server &a" + sender + " &7has sent a null ServerInfo.");
                BukkitUtils.console(e.getMessage());
            }

            //Send back the message if it's a request
            if(channel.equals(NetworkConfig.getChannel("SERVER_REQUEST"))) {
                if(!sender.equals(NetworkPlus.getUsername())) {
                    NetworkPlus.sendMessage(sender, NetworkConfig.getChannel("SERVER_RESPONSE"),
                            NetworkPlus.gson().toJson(NetworkPlus.getServerInfo())
                    );
                }
            }
        }

		if(channel.equals(NetworkConfig.getChannel("CLEAR_REQUEST")))
		{		
			NetworkPlus.getEventManager().triggerEvent(new ServerClearEvent(message, sender));
		}
		
	}

}
