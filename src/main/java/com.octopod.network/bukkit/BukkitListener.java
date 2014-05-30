package com.octopod.network.bukkit;

import com.octopod.network.*;
import com.octopod.network.CommandManager;
import com.octopod.network.HubManager;
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
        Map<String, NetworkCommand> commands = CommandManager.getCommands();

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

    private void updatePlayerJoined(String player) {
        NetworkPlus.broadcastMessage(NPChannel.PLAYER_JOIN_SERVER, new NPMessage(NetworkPlus.getServerID(), player));
    }

    private void updatePlayerLeft(String player) {
        NetworkPlus.broadcastMessage(NPChannel.PLAYER_LEAVE_SERVER, new NPMessage(NetworkPlus.getServerID(), player));
    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {updatePlayerJoined(event.getPlayer().getName());}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) 
	{
		updatePlayerLeft(event.getPlayer().getName());
		if (QueueManager.instance.isQueued(event.getPlayer()
				.getName())) {
			QueueManager.instance
					.remove(event.getPlayer().getName());
		}
		QueueManager.instance.updateQueue();
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerKicked(PlayerKickEvent event)
    {

        String hub = HubManager.getHub();

        if(hub != null && !hub.equals(NetworkPlus.getServerID())) {
            event.setCancelled(true);

            BukkitUtils.sendMessage(event.getPlayer(), "&7You've been moved to the hub server:");
            BukkitUtils.sendMessage(event.getPlayer(), "&c\"" + event.getReason() + "\"");
            NetworkPlus.sendPlayer(event.getPlayer().getName(), hub);
        }
		
	}

}
