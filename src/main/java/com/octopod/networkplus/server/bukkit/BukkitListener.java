package com.octopod.networkplus.server.bukkit;

import com.octopod.networkplus.Command;
import com.octopod.networkplus.NetworkPlus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitListener implements Listener
{
	/**
	 * Handles all the command-related events
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent event)
	{
		String[] parsed = event.getMessage().split(" ");

		String root = parsed[0];
		String[] args = Arrays.copyOfRange(parsed, 1, parsed.length);

		Command command = NetworkPlus.getCommandManager().getCommand(root);

		if(command != null) {
			if(command.startCommand(new BukkitPlayer(event.getPlayer()), root, args)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		//NetworkPlus.broadcastMessage(NPChannel.PLAYER_JOIN_SERVER, new NetworkMessage(NetworkPlus.getServerID(), event.getPlayer().getUniqueId()));
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event)
	{
//		NetworkPlus.broadcastMessage(NPChannel.PLAYER_LEAVE_SERVER, new NetworkMessage(NetworkPlus.getServerID(), player));
//		if (QueueManager.instance.isQueued(event.getPlayer()
//											   .getName())) {
//			QueueManager.instance
//				.remove(event.getPlayer().getName());
//		}
//		QueueManager.instance.updateQueue();
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerKicked(PlayerKickEvent event)
	{
//		String hub = HubManager.getHub();
//
//		if(hub != null && !hub.equals(NetworkPlus.getServerID())) {
//			event.setCancelled(true);
//
//			BukkitUtils.sendMessage(event.getPlayer(), "&7You've been moved to the hub server:");
//			BukkitUtils.sendMessage(event.getPlayer(), "&c\"" + event.getReason() + "\"");
//			NetworkPlus.sendPlayer(event.getPlayer().getName(), hub);
//		}
	}

}
