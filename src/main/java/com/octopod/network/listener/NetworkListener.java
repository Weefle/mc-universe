package com.octopod.network.listener;

import com.octopod.network.*;
import com.octopod.network.cache.PlayerCache;
import com.octopod.network.cache.ServerCache;
import com.octopod.network.events.EventEmitter;
import com.octopod.network.events.EventHandler;
import com.octopod.network.events.network.NetworkConnectedEvent;
import com.octopod.network.events.player.NetworkPlayerJoinEvent;
import com.octopod.network.events.player.NetworkPlayerLeaveEvent;
import com.octopod.network.events.player.NetworkPlayerRedirectEvent;
import com.octopod.network.events.relays.MessageEvent;
import com.octopod.network.events.server.ServerCacheEvent;
import com.octopod.network.events.server.ServerUncacheEvent;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NetworkListener {

    @EventHandler(runAsync = true)
	public void networkConnected(NetworkConnectedEvent event) {
		Debug.info("&aSuccessfully connected to LilyPad!");
		LPRequestUtils.broadcastMessage(NetworkConfig.getConfig().REQUEST_CACHE, NetworkPlugin.getServerName());
	}
	
	@EventHandler(runAsync = true)
	public void serverCacheEvent(ServerCacheEvent event) {
		String server = event.getCachedServer();
		ServerCache.addServer(server);
		PlayerCache.importServer(server);
	}

    @EventHandler(runAsync = true)
	public void serverUncacheEvent(ServerUncacheEvent event) {
		ServerCache.removeServer(event.getUncachedServer());
	}

    @EventHandler(runAsync = true)
	public void playerJoinEvent(NetworkPlayerJoinEvent event) {
		Debug.info("&b" + event.getPlayer() + " &7joined network through &a" + event.getServer());
		PlayerCache.putPlayer(event.getPlayer(), event.getServer());
	}

    @EventHandler(runAsync = true)
	public void playerLeaveEvent(NetworkPlayerLeaveEvent event) {
		Debug.info("&b" + event.getPlayer() + " &7left network through &a" + event.getServer());
		PlayerCache.removePlayer(event.getPlayer());
	}

    @EventHandler(runAsync = true)
	public void playerRedirectEvent(NetworkPlayerRedirectEvent event) {
		Debug.info("&b" + event.getPlayer() + " &7switched servers to &a" + event.getServer());
		PlayerCache.putPlayer(event.getPlayer(), event.getServer());
	}

    @EventHandler(runAsync = true)
	public void messageRecieved(MessageEvent event) {
		
		String sender = event.getSender();
		String channel = event.getChannel();
		String message = event.getMessage();
		
		Debug.debug(
				"&7Message: &a" + sender + "&7 on &b" + channel
		);

		NetworkConfig config = NetworkPlugin.getNetworkConfig();
		
		if(channel.equals(config.REQUEST_SENDALL)) 
		{	
			try {
				for(String player: NetworkPlugin.getPlayerNames()) {
					NetworkPlugin.connect.request(new RedirectRequest(message, player));
				}
			} catch (RequestException e) {}
		}	
		
		if(channel.equals(config.REQUEST_MESSAGE)) 
		{
			List<String> args = new LinkedList<String>(Arrays.asList(message.split(" ")));
			String player = args.get(0);
			String playerMessage = StringUtils.join(args.subList(1, args.size() - 1), " ");
			
			NetworkPlugin.sendMessage(player, playerMessage);
		}
		
		if(channel.equals(config.REQUEST_PLAYER_JOIN)) {
			EventEmitter.getEmitter().triggerEvent(new NetworkPlayerJoinEvent(message, sender));
		}
		
		if(channel.equals(config.REQUEST_PLAYER_LEAVE)) {
			EventEmitter.getEmitter().triggerEvent(new NetworkPlayerLeaveEvent(message, sender));
		}
		
		if(channel.equals(config.REQUEST_PLAYER_REDIRECT)) {
			EventEmitter.getEmitter().triggerEvent(new NetworkPlayerRedirectEvent(message, sender));
		}
	
		/**
		 * Alert channels: Broadcasts a message on the server when recieved.
		 */
		if(channel.equals(config.REQUEST_BROADCAST)) 
		{			
			NetworkPlugin.broadcastMessage(message);
		}
	
		/**
		 * Find Request: Servers that recieve this message will ping back, if the specified player is on the server.
		 * Use synchronized listeners to wait for the server's response.
		 */					
		if(channel.equals(config.REQUEST_FIND)) 
		{			
			if(NetworkPlugin.getPlayerNames().contains(message)) {
				LPRequestUtils.sendEmptyMessage(sender, config.REQUEST_FIND_RELAY);
			}
		}

		/**
		 * Info Request: Servers that recieve this message will send back server information.
		 *  Use synchronized listeners to wait for the server's response.
		 */
		if(channel.equals(config.REQUEST_INFO)) 
		{		
			LPRequestUtils.sendMessage(sender, config.REQUEST_INFO_RELAY, ServerInfo.encodeThisServer());
		}
	
		/**
		 * Status channels: Tells other servers when a server is online or offline (also so servers can cache server names)
		 */
		if(channel.equals(config.REQUEST_CACHE) || channel.equals(config.REQUEST_CACHE_RELAY))
		{		
			if(channel.equals(config.REQUEST_CACHE) && !sender.equals(NetworkPlugin.connect.getSettings().getUsername()))
				LPRequestUtils.sendMessage(sender, config.REQUEST_CACHE_RELAY, NetworkPlugin.getServerName());	
			
			EventEmitter.getEmitter().triggerEvent(new ServerCacheEvent(message, sender));
		}
	
		if(channel.equals(config.REQUEST_UNCACHE))
		{		
			EventEmitter.getEmitter().triggerEvent(new ServerUncacheEvent(message, sender));
		}
		
	}

}
