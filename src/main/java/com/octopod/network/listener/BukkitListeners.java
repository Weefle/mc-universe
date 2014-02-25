package com.octopod.network.listener;

import com.octopod.network.LPRequestUtils;
import com.octopod.network.NetworkPlugin;
import com.octopod.network.cache.CommandCache;
import com.octopod.network.cache.PlayerCache;
import com.octopod.network.commands.DocumentedCommand;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class BukkitListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {

        Map<String, DocumentedCommand> commands = CommandCache.getCache().getCommands();

        String[] parsed = event.getMessage().split(" ");

        String root = parsed[0];
        String[] args = Arrays.copyOfRange(parsed, 1, parsed.length);

        DocumentedCommand command = commands.get(root);

        if(command != null) {
            if(command.onCommand(event.getPlayer(), root, args)) {
                event.setCancelled(true);
            }
        }

    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		Map<String, String> playerMap = PlayerCache.getCache().getPlayerMap();
		
		String location = playerMap.get(event.getPlayer().getName());		
		String channel;

		if(location == null) {
			channel = NetworkPlugin.getNetworkConfig().REQUEST_PLAYER_JOIN;
		} else {
			channel = NetworkPlugin.getNetworkConfig().REQUEST_PLAYER_REDIRECT;
		} 
		
		LPRequestUtils.broadcastMessage(channel, event.getPlayer().getName());	
		
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		
		Map<String, String> playerMap = PlayerCache.getCache().getPlayerMap();
		
		String location = playerMap.get(event.getPlayer().getName());
		String channel = NetworkPlugin.getNetworkConfig().REQUEST_PLAYER_LEAVE;
		
		if(location != null && location.equals(NetworkPlugin.getServerName())) 
		{
			LPRequestUtils.broadcastMessage(channel, event.getPlayer().getName());	
		}
		
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerKicked(PlayerKickEvent event) {
		
		event.setCancelled(true);
		
		try {
			NetworkPlugin.sendMessage(event.getPlayer(), NetworkPlugin.PREFIX + "&7You've been moved to the hub server: &c\"" + event.getReason() + "\"");
			NetworkPlugin.connect.request(new RedirectRequest("core", event.getPlayer().getName()));
		} catch (RequestException e) {
			e.printStackTrace();
		}
		
	}

}
