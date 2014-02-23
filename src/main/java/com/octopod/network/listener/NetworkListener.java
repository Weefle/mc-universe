package com.octopod.network.listener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.RedirectRequest;

import org.apache.commons.lang.StringUtils;

import com.octopod.network.Debug;
import com.octopod.network.NetworkPlugin;
import com.octopod.network.NetworkConfig;
import com.octopod.network.LPRequestUtils;
import com.octopod.network.ServerInfo;
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

public class NetworkListener {
	
	@EventHandler
	public void networkConnected(NetworkConnectedEvent event) {

		Debug.debug("Successfully connected to LilyPad!");
		LPRequestUtils.broadcastMessage(NetworkConfig.getConfig().REQUEST_CACHE, NetworkPlugin.getServerName());

	}
	
	@EventHandler
	public void serverCacheEvent(ServerCacheEvent event) {
		String server = event.getCachedServer();
		ServerCache.getCache().addServer(server);
		PlayerCache.getCache().importServer(server);
	}
	
	@EventHandler
	public void serverUncacheEvent(ServerCacheEvent event) {
		String server = event.getCachedServer();
		ServerCache.getCache().addServer(server);
	}
	
	@EventHandler
	public void playerJoinEvent(NetworkPlayerJoinEvent event) {
		Debug.debug("&b" + event.getPlayer() + " &7joined network through &a" + event.getServer());
		PlayerCache.getCache().putPlayer(event.getPlayer(), event.getServer());
	}
	
	@EventHandler
	public void playerLeaveEvent(NetworkPlayerLeaveEvent event) {
		Debug.debug("&b" + event.getPlayer() + " &7left network through &a" + event.getServer());
		PlayerCache.getCache().removePlayer(event.getPlayer());
	}
	
	@EventHandler
	public void playerRedirectEvent(NetworkPlayerRedirectEvent event) {
		Debug.debug("&b" + event.getPlayer() + " &7switched servers to &a" + event.getServer());
		PlayerCache.getCache().putPlayer(event.getPlayer(), event.getServer());
	}
	
	@EventHandler
	public void messageRecieved(MessageEvent event) {
		
		String sender = event.getSender();
		String channel = event.getChannel();
		String message = event.getMessage();
		
		Debug.verbose(
				"&7Recieved message from &a" + sender + "&7:\n",
				"    &7channel: &b" + channel
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
