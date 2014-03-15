package com.octopod.network;

import com.google.gson.JsonSyntaxException;
import com.octopod.network.*;
import com.octopod.network.cache.NetworkHubCache;
import com.octopod.network.cache.NetworkPlayerCache;
import com.octopod.network.cache.NetworkServerCache;
import com.octopod.network.events.EventEmitter;
import com.octopod.network.events.EventHandler;
import com.octopod.network.events.EventPriority;
import com.octopod.network.events.network.NetworkConnectedEvent;
import com.octopod.network.events.network.NetworkHubCacheEvent;
import com.octopod.network.events.player.NetworkPlayerJoinEvent;
import com.octopod.network.events.player.NetworkPlayerLeaveEvent;
import com.octopod.network.events.player.NetworkPlayerRedirectEvent;
import com.octopod.network.events.relays.MessageEvent;
import com.octopod.network.events.server.ServerInfoEvent;
import com.octopod.network.events.server.ServerPlayerListEvent;
import com.octopod.network.events.server.ServerUncacheEvent;
import com.octopod.network.bukkit.BukkitUtils;

/**
 * @author Octopod
 */
public class NetworkListener {

    /**
     * The current NetworkLogger instance. Will be used to log messages.
     */
    private NetworkLogger logger = NetworkPlus.getLogger();

    /**
     * Listens for when this plugin is connected according to the NetworkConnection instance.
     * This method should request ServerInfo and playerlists from every server.
     * @param event
     */
    @EventHandler(priority = EventPriority.SYSTEM, runAsync = true)
	public void networkConnected(NetworkConnectedEvent event) {
        logger.debug("&aSuccessfully connected!");
        NetworkPlus.getPlugin().scan();
	}

    /**
     * Listens for when a hub server is found.
     * This method should cache the found hub server under the appropriate priority.
     * @param event
     */
    @EventHandler(priority = EventPriority.SYSTEM, runAsync = true)
    public void networkHubCache(NetworkHubCacheEvent event) {
        NetworkHubCache.addHub(event.getServer(), event.getPriority());
        logger.info("Server &a" + event.getServer() + "&7 registered as hub @ priority &e" + event.getPriority());
    }

    /**
     * Listens for when a playerlist is recieved from any server.
     * This method should cache the playerlist under the server it was sent from.
     * @param event
     */
    @EventHandler(priority = EventPriority.SYSTEM, runAsync = true)
    public void serverPlayerListEvent(ServerPlayerListEvent event) {
        String server = event.getServer();
        String[] players = event.getPlayers();
        for(String player: players) {
            NetworkPlayerCache.putPlayer(player, server);
        }
        logger.debug("Recieved &b" + players.length + " players &7from &a" + server);
    }

    /**
     * Listens for when a ServerInfo object is recieved from any server.
     * This method should cache the ServerInfo under the server it was sent from.
     * @param event
     */
	@EventHandler(priority = EventPriority.SYSTEM, runAsync = true)
	public void serverInfoEvent(ServerInfoEvent event) {
		String server = event.getSender();
        ServerInfo serverInfo = event.getServerInfo();
		NetworkServerCache.addServer(serverInfo);

        //priority at least 0 = this server is a hub
        int hubPriority = serverInfo.getHubPriority();
        if(hubPriority >= 0)
            EventEmitter.getEmitter().triggerEvent(new NetworkHubCacheEvent(server, hubPriority));

        if(!NetworkPlus.isTestBuild()) {
            //Checks for mismatched plugin versions between servers, and warns the server owners.
            String version = serverInfo.getPluginVersion();

            if(!version.equals("TEST_BUILD") && !version.equals(NetworkPlus.getPluginVersion())) {
                logger.info("&a" + server + "&7: Running &6Net+&7 version &6" + (version.equals("") ? "No Version" : version));
            }
        }

	}

    /**
     * Listens for when an uncache request is sent from any server.
     * This method should uncache the requested server.
     * @param event
     */
    @EventHandler(priority = EventPriority.SYSTEM, runAsync = true)
	public void serverUncacheEvent(ServerUncacheEvent event) {
		NetworkServerCache.removeServer(event.getUncachedServer());
	}

    /**
     * Listens for when any player joins the network.
     * This method should cache the player into the server they joined into.
     * @param event
     */
    @EventHandler(priority = EventPriority.SYSTEM, runAsync = true)
	public void playerJoinEvent(NetworkPlayerJoinEvent event) {
        logger.debug("&b" + event.getPlayer() + " &7joined network through &a" + event.getServer());
		NetworkPlayerCache.putPlayer(event.getPlayer(), event.getServer());
	}

    /**
     * Listens for when any player leaves the network.
     * This method should uncache the player.
     * @param event
     */
    @EventHandler(priority = EventPriority.SYSTEM, runAsync = true)
	public void playerLeaveEvent(NetworkPlayerLeaveEvent event) {
        logger.debug("&b" + event.getPlayer() + " &7left network through &a" + event.getServer());
		NetworkPlayerCache.removePlayer(event.getPlayer());
	}

    /**
     * Listens for when any player moves from one server to another.
     * This method should change the player's entry in the cache to the new server.
     * @param event
     */
    @EventHandler(priority = EventPriority.SYSTEM, runAsync = true)
	public void playerRedirectEvent(NetworkPlayerRedirectEvent event) {
        logger.debug("&b" + event.getPlayer() + " &7redirected from &a" + event.getFromServer() + "&7 to &a" + event.getServer());
		NetworkPlayerCache.putPlayer(event.getPlayer(), event.getServer());
	}

    /**
     * Listens for when any message is recieved from any server.
     * This method is a gateway to other events and features.
     * @param event
     */
    @EventHandler(priority = EventPriority.SYSTEM, runAsync = true)
	public void messageRecieved(MessageEvent event) {

		String sender = event.getSender();
		String channel = event.getChannel();
		String message = event.getMessage();

        logger.verbose("&7Message: &a" + sender + "&7 on &b" + channel);

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
			EventEmitter.getEmitter().triggerEvent(new NetworkPlayerJoinEvent(message, sender));

        //Tells the server a player has left the network
		if(channel.equals(NetworkConfig.getChannel("PLAYER_LEAVE")))
			EventEmitter.getEmitter().triggerEvent(new NetworkPlayerLeaveEvent(message, sender));

        //Tells the server a player has switched servers on the network
		if(channel.equals(NetworkConfig.getChannel("PLAYER_REDIRECT")))
			EventEmitter.getEmitter().triggerEvent(new NetworkPlayerRedirectEvent(message, NetworkPlayerCache.findPlayer(message), sender));

		//Tells the server to broadcast a message on the server.
		if(channel.equals(NetworkConfig.getChannel("BROADCAST")))
		{
			BukkitUtils.broadcastMessage(message);
		}

		/**
		 * Info Request
         * Servers that recieve this message will send back server information.
		 */
		if(channel.equals(NetworkConfig.getChannel("INFO_REQUEST")) || channel.equals(NetworkConfig.getChannel("INFO_RESPONSE")))
        {
            try {
                ServerInfo info = NetworkPlus.gson().fromJson(message, ServerInfo.class);
                if(info == null) throw new NullPointerException("Server '" + sender + "' gave us a null serverinfo, json: " + message);
                EventEmitter.getEmitter().triggerEvent(
                        new ServerInfoEvent(NetworkPlus.gson().fromJson(message, ServerInfo.class))
                );
            } catch (JsonSyntaxException e) {
                NetworkPlus.getLogger().info("Server &a" + sender + " &7has sent an invalid ServerInfo.");
            }

            //Send back the message if it's a request
            if(channel.equals(NetworkConfig.getChannel("INFO_REQUEST"))) {
                if(!sender.equals(NetworkPlus.getUsername())) {
                    NetworkPlus.sendMessage(sender, NetworkConfig.getChannel("INFO_RESPONSE"),
                            NetworkPlus.gson().toJson(NetworkPlus.getServerInfo())
                    );
                }
            }
        }

        /**
         * PlayerList Request
         * Servers that recieve this message will send back the server's playerlist.
         */
        if(channel.equals(NetworkConfig.getChannel("PLAYERLIST_REQUEST")) || channel.equals(NetworkConfig.getChannel("PLAYERLIST_RESPONSE")))
        {
            try {
                ServerPlayerListEvent playerListEvent = NetworkPlus.gson().fromJson(message, ServerPlayerListEvent.class);
                if(event == null) throw new NullPointerException("Server '" + sender + "' gave us a null playerlist, json: " + message);
                EventEmitter.getEmitter().triggerEvent(playerListEvent);
            } catch (JsonSyntaxException e) {
                NetworkPlus.getLogger().info("Server &a" + sender + " &7has sent an invalid playerlist.");
            }

            if(channel.equals(NetworkConfig.getChannel("PLAYERLIST_REQUEST"))) {
                if(!sender.equals(NetworkPlus.getUsername())) {
                    NetworkPlus.sendMessage(sender, NetworkConfig.getChannel("PLAYERLIST_RESPONSE"),
                            NetworkPlus.gson().toJson(new ServerPlayerListEvent(NetworkPlus.getUsername(), BukkitUtils.getPlayerNames()))
                    );
                }
            }
        }

		if(channel.equals(NetworkConfig.getChannel("UNCACHE")))
		{		
			EventEmitter.getEmitter().triggerEvent(new ServerUncacheEvent(message, sender));
		}
		
	}

}
