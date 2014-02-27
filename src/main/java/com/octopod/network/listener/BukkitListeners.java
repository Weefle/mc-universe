package com.octopod.network.listener;

import com.octopod.network.util.BukkitUtils;
import com.octopod.network.util.RequestUtils;
import com.octopod.network.NetworkPlugin;
import com.octopod.network.cache.NetworkCommandCache;
import com.octopod.network.cache.NetworkHubCache;
import com.octopod.network.cache.NetworkPlayerCache;
import com.octopod.network.commands.NetworkCommand;
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

        Map<String, NetworkCommand> commands = NetworkCommandCache.getCommands();

        String[] parsed = event.getMessage().split(" ");

        String root = parsed[0];
        String[] args = Arrays.copyOfRange(parsed, 1, parsed.length);

        NetworkCommand command = commands.get(root);

        if(command != null) {
            if(command.onCommand(event.getPlayer(), root, args)) {
                event.setCancelled(true);
            }
        }

    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		Map<String, String> playerMap = NetworkPlayerCache.getPlayerMap();
		
		String location = playerMap.get(event.getPlayer().getName());		
		String channel;

		if(location == null) {
			channel = NetworkPlugin.getNetworkConfig().CHANNEL_PLAYER_JOIN;
		} else {
			channel = NetworkPlugin.getNetworkConfig().CHANNEL_PLAYER_REDIRECT;
		} 
		
		RequestUtils.broadcastMessage(channel, event.getPlayer().getName());
		
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		
		Map<String, String> playerMap = NetworkPlayerCache.getPlayerMap();
		
		String location = playerMap.get(event.getPlayer().getName());
		String channel = NetworkPlugin.getNetworkConfig().CHANNEL_PLAYER_LEAVE;
		
		if(location != null && location.equals(NetworkPlugin.getUsername()))
		{
			RequestUtils.broadcastMessage(channel, event.getPlayer().getName());
		}
		
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerKicked(PlayerKickEvent event) {

        String hub = NetworkHubCache.getHub();

        if(hub != null && !hub.equals(NetworkPlugin.getUsername())) {
            event.setCancelled(true);

            BukkitUtils.sendMessage(event.getPlayer(), NetworkPlugin.PREFIX + "&7You've been moved to the hub server:");
            BukkitUtils.sendMessage(event.getPlayer(), "&c\"" + event.getReason() + "\"");
            RequestUtils.request(new RedirectRequest(hub, event.getPlayer().getName()));
        }
		
	}

}
