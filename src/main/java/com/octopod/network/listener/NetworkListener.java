package com.octopod.network.listener;

import com.octopod.network.*;
import com.octopod.network.cache.NetworkHubCache;
import com.octopod.network.cache.NetworkPlayerCache;
import com.octopod.network.cache.NetworkServerCache;
import com.octopod.network.events.EventEmitter;
import com.octopod.network.events.EventHandler;
import com.octopod.network.events.network.NetworkConnectedEvent;
import com.octopod.network.events.network.NetworkHubCacheEvent;
import com.octopod.network.events.player.NetworkPlayerJoinEvent;
import com.octopod.network.events.player.NetworkPlayerLeaveEvent;
import com.octopod.network.events.player.NetworkPlayerRedirectEvent;
import com.octopod.network.events.relays.MessageEvent;
import com.octopod.network.events.server.ServerInfoEvent;
import com.octopod.network.events.server.ServerPlayerListEvent;
import com.octopod.network.events.server.ServerUncacheEvent;
import com.octopod.network.util.BukkitUtils;
import com.octopod.network.util.RequestUtils;
import com.octopod.octolib.common.StringUtils;
import lilypad.client.connect.api.request.impl.RedirectRequest;

import java.util.List;

public class NetworkListener {

    @EventHandler(runAsync = true)
	public void networkConnected(NetworkConnectedEvent event) {
		NetworkDebug.debug("&aSuccessfully connected to LilyPad!");
        NetworkPlugin.scan();
	}

    @EventHandler(runAsync = true)
    public void networkHubCache(NetworkHubCacheEvent event) {
        NetworkHubCache.addHub(event.getServer(), event.getPriority());
        NetworkDebug.info("Server &a" + event.getServer() + "&7 registered as hub @ priority &e" + event.getPriority());
    }

    @EventHandler(runAsync = true)
    public void serverPlayerListEvent(ServerPlayerListEvent event) {
        String server = event.getServer();
        String[] players = event.getPlayers();
        for(String player: players) {
            NetworkPlayerCache.putPlayer(player, server);
        }
        NetworkDebug.debug("Recieved &b" + players.length + " players &7from &a" + server);
    }

	@EventHandler(runAsync = true)
	public void serverInfoEvent(ServerInfoEvent event) {
		String server = event.getSender();
        NetworkPlugin.ServerInfo serverInfo = event.getServerInfo();
		NetworkServerCache.addServer(server, serverInfo);

        //priority at least 0 = this server is a hub
        int hubPriority = serverInfo.getHubPriority();
        if(hubPriority >= 0)
            EventEmitter.getEmitter().triggerEvent(new NetworkHubCacheEvent(server, hubPriority));

        //Checks for mismatched plugin versions between servers, and warns the server owners.
        String version = serverInfo.getPluginVersion();

        if(!version.equals(NetworkPlugin.getPluginVersion())) {
            NetworkDebug.info("&a" + server + "&7: Running &6Net+&7 version &6" + (version.equals("") ? "No Version" : version));
        }

	}

    @EventHandler(runAsync = true)
	public void serverUncacheEvent(ServerUncacheEvent event) {
		NetworkServerCache.removeServer(event.getUncachedServer());
	}

    @EventHandler(runAsync = true)
	public void playerJoinEvent(NetworkPlayerJoinEvent event) {
		NetworkDebug.debug("&b" + event.getPlayer() + " &7joined network through &a" + event.getServer());
		NetworkPlayerCache.putPlayer(event.getPlayer(), event.getServer());
	}

    @EventHandler(runAsync = true)
	public void playerLeaveEvent(NetworkPlayerLeaveEvent event) {
		NetworkDebug.debug("&b" + event.getPlayer() + " &7left network through &a" + event.getServer());
		NetworkPlayerCache.removePlayer(event.getPlayer());
	}

    @EventHandler(runAsync = true)
	public void playerRedirectEvent(NetworkPlayerRedirectEvent event) {
		NetworkDebug.debug("&b" + event.getPlayer() + " &7redirected from &a" + event.getFromServer() + "&7 to &a" + event.getServer());
		NetworkPlayerCache.putPlayer(event.getPlayer(), event.getServer());
	}

    @EventHandler(runAsync = true)
	public void messageRecieved(MessageEvent event) {

		String sender = event.getSender();
		String channel = event.getChannel();
		String message = event.getMessage();

		NetworkDebug.verbose("&7Message: &a" + sender + "&7 on &b" + channel);

        //Tells the server to send all players on this server to the server specified in 'message'
		if(channel.equals(NetworkConfig.CHANNEL_SENDALL))
		{
            for(String player: BukkitUtils.getPlayerNames())
                RequestUtils.request(new RedirectRequest(message, player));
		}

        //Tells the server to send a message to a player. Information included in 'message'
		if(channel.equals(NetworkConfig.CHANNEL_MESSAGE))
		{
			List<String> args = StringUtils.parseArgs(message);
			String player = args.get(0);
			String playerMessage = args.get(1);
			BukkitUtils.sendMessage(player, playerMessage, null);
		}

        //Tells the server a player has joined the network
		if(channel.equals(NetworkConfig.CHANNEL_PLAYER_JOIN))
			EventEmitter.getEmitter().triggerEvent(new NetworkPlayerJoinEvent(message, sender));

        //Tells the server a player has left the network
		if(channel.equals(NetworkConfig.CHANNEL_PLAYER_LEAVE))
			EventEmitter.getEmitter().triggerEvent(new NetworkPlayerLeaveEvent(message, sender));

        //Tells the server a player has switched servers on the network
		if(channel.equals(NetworkConfig.CHANNEL_PLAYER_REDIRECT))
			EventEmitter.getEmitter().triggerEvent(new NetworkPlayerRedirectEvent(message, NetworkPlayerCache.findPlayer(message), sender));

		//Tells the server to broadcast a message on the server.
		if(channel.equals(NetworkConfig.CHANNEL_BROADCAST))
		{
			BukkitUtils.broadcastMessage(message);
		}

		/**
		 * Info Request: Servers that recieve this message will send back server information.
		 *  Use synchronized listeners to wait for the server's response.
		 */
		if(channel.equals(NetworkConfig.CHANNEL_INFO_REQUEST))
        {
            EventEmitter.getEmitter().triggerEvent(new ServerInfoEvent(event.getMessage()));
            if(!sender.equals(NetworkPlugin.getUsername())) {
                RequestUtils.sendMessage(sender, NetworkConfig.CHANNEL_INFO_RESPONSE, NetworkPlugin.encodeServerInfo());
            }
        }

        if(channel.equals(NetworkConfig.CHANNEL_INFO_RESPONSE))
        {
            EventEmitter.getEmitter().triggerEvent(new ServerInfoEvent(event.getMessage()));
        }

        if(channel.equals(NetworkConfig.CHANNEL_PLAYERLIST_REQUEST))
        {
            EventEmitter.getEmitter().triggerEvent(new ServerPlayerListEvent(event.getSender(), event.getMessage()));
            if(!sender.equals(NetworkPlugin.getUsername())) {
                RequestUtils.sendMessage(sender, NetworkConfig.CHANNEL_PLAYERLIST_RESPONSE, NetworkPlugin.encodePlayerList());
            }
        }

        if(channel.equals(NetworkConfig.CHANNEL_PLAYERLIST_RESPONSE))
        {
            EventEmitter.getEmitter().triggerEvent(new ServerPlayerListEvent(event.getSender(), event.getMessage()));
        }

		if(channel.equals(NetworkConfig.CHANNEL_UNCACHE))
		{		
			EventEmitter.getEmitter().triggerEvent(new ServerUncacheEvent(message, sender));
		}
		
	}

}
