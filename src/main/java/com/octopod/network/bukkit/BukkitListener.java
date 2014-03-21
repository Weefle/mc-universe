package com.octopod.network.bukkit;

import com.octopod.network.NetworkConfig;
import com.octopod.network.NetworkPlus;
import com.octopod.network.cache.NetworkCommandCache;
import com.octopod.network.cache.NetworkHubCache;
import com.octopod.network.cache.NetworkPlayerCache;
import com.octopod.network.commands.NetworkCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class BukkitListener implements Listener {

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

		String location = NetworkPlayerCache.findPlayer(event.getPlayer().getName());
		String channel;

		if(location == null) {
			channel = NetworkConfig.getChannel("PLAYER_JOIN");
		} else {
			channel = NetworkConfig.getChannel("PLAYER_REDIRECT");
		}

        NetworkPlus.broadcastMessage(channel, event.getPlayer().getName());
		
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {

		String location = NetworkPlayerCache.findPlayer(event.getPlayer().getName());
		String channel = NetworkConfig.getChannel("PLAYER_LEAVE");
		
		if(location != null && location.equals(NetworkPlus.getUsername()))
		{
			NetworkPlus.broadcastMessage(channel, event.getPlayer().getName());
		}
		
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerKicked(PlayerKickEvent event) {

        String hub = NetworkHubCache.getHub();

        if(hub != null && !hub.equals(NetworkPlus.getUsername())) {
            event.setCancelled(true);

            BukkitUtils.sendMessage(event.getPlayer(), NetworkPlus.prefix() + "&7You've been moved to the hub server:");
            BukkitUtils.sendMessage(event.getPlayer(), "&c\"" + event.getReason() + "\"");
            NetworkPlus.sendPlayer(event.getPlayer().getName(), hub);
        }
		
	}

}
