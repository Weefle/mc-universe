package com.octopod.networkplus;

import com.octopod.minecraft.BukkitPlayer;
import com.octopod.networkplus.messages.MessageOutPlayerJoin;
import com.octopod.networkplus.messages.MessageOutPlayerLeave;
import com.octopod.util.minecraft.command.CommandManager;
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
	 * Handles all the commands-related events
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent event)
	{
		String[] parsed = event.getMessage().split(" ");

		String root = parsed[0];
		String[] args = Arrays.copyOfRange(parsed, 1, parsed.length);

		CommandManager commandManager = NetworkPlus.getCommandManager();

		if(commandManager.commandExists(root))
		{
			NetworkPlus.getCommandManager().dispatchCommand(new BukkitPlayer(event.getPlayer()), root, args);
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		new MessageOutPlayerJoin(new BukkitPlayer(event.getPlayer())).broadcast();
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event)
	{
		new MessageOutPlayerLeave(new BukkitPlayer(event.getPlayer())).broadcast();
//		if (QueueManager.instance.isQueued(event.getPlayer()
//											   .getServerName())) {
//			QueueManager.instance
//				.remove(event.getPlayer().getServerName());
//		}
//		QueueManager.instance.updateQueue();
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerKicked(PlayerKickEvent event)
	{
//		String hub = HubManager.getHub();
//
//		if(hub != null && !hub.equals(NetworkPlus.getServer())) {
//			event.setCancelled(true);
//
//			BukkitUtils.sendMessage(event.getPlayer(), "&7You've been moved to the hub server:");
//			BukkitUtils.sendMessage(event.getPlayer(), "&c\"" + event.getReason() + "\"");
//			NetworkPlus.sendPlayer(event.getPlayer().getServerName(), hub);
//		}
	}

}
