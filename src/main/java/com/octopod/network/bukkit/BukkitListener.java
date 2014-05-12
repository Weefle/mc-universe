package com.octopod.network.bukkit;

import com.octopod.network.*;
import com.octopod.network.cache.NetworkCommandCache;
import com.octopod.network.cache.NetworkHubCache;
import com.octopod.network.cache.NetworkServerCache;
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
    public void onCommand(PlayerCommandPreprocessEvent event)
    {
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

    private void updatePlayerJoined() {
        List<String> players = new ArrayList<>();
        players.addAll(Arrays.asList(BukkitUtils.getPlayerNames()));
        updatePlayers(players);
    }

    private void updatePlayerLeft(String player) {
        List<String> players = new ArrayList<>();
        players.addAll(Arrays.asList(BukkitUtils.getPlayerNames()));
        players.remove(player);
        updatePlayers(players);
    }

    private void updatePlayers(List<String> players)
    {
        ServerFlags flags = new ServerFlags();
        flags.setFlag("onlinePlayers", players);

        NetworkPlus.broadcastPartialServerInfo(NetworkPlus.getServerID(), flags);
    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {updatePlayerJoined();}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) 
	{
		updatePlayerLeft(event.getPlayer().getName());
		if (NetworkQueueManager.instance.isQueued(event.getPlayer()
				.getName())) {
			NetworkQueueManager.instance
					.remove(event.getPlayer().getName());
		}
		NetworkQueueManager.instance.updateQueue();
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerKicked(PlayerKickEvent event)
    {

        String hub = NetworkHubCache.getHub();

        if(hub != null && !hub.equals(NetworkPlus.getServerID())) {
            event.setCancelled(true);

            BukkitUtils.sendMessage(event.getPlayer(), NetworkPlus.prefix() + "&7You've been moved to the hub server:");
            BukkitUtils.sendMessage(event.getPlayer(), "&c\"" + event.getReason() + "\"");
            NetworkPlus.sendPlayer(event.getPlayer().getName(), hub);
        }
		
	}

}
