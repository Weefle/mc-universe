package com.octopod.switchcore;

import com.octopod.minecraft.BukkitConsole;
import com.octopod.minecraft.BukkitPlayer;
import com.octopod.switchcore.packets.PacketOutPlayerJoin;
import com.octopod.switchcore.packets.PacketOutPlayerLeave;
import com.octopod.util.minecraft.command.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;

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

		CommandManager commandManager = SwitchCore.getCommandManager();

		if(commandManager.commandExists(root))
		{
			SwitchCore.getCommandManager().dispatchCommand(new BukkitPlayer(event.getPlayer()), root, args);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCommand(ServerCommandEvent event)
	{
		String[] parsed = event.getCommand().split(" ");

		String root = "/" + parsed[0];
		String[] args = Arrays.copyOfRange(parsed, 1, parsed.length);

		CommandManager commandManager = SwitchCore.getCommandManager();

		if(commandManager.commandExists(root))
		{
			SwitchCore.getCommandManager().dispatchCommand(new BukkitConsole(Bukkit.getConsoleSender()), root, args);
			event.setCommand("");
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		new PacketOutPlayerJoin(new BukkitPlayer(event.getPlayer())).broadcast();
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event)
	{
		new PacketOutPlayerLeave(new BukkitPlayer(event.getPlayer())).broadcast();
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
//		if(hub != null && !hub.equals(SwitchCore.getServer())) {
//			event.setCancelled(true);
//
//			BukkitUtils.sendMessage(event.getPlayer(), "&7You've been moved to the hub server:");
//			BukkitUtils.sendMessage(event.getPlayer(), "&c\"" + event.getReason() + "\"");
//			SwitchCore.sendPlayer(event.getPlayer().getServerName(), hub);
//		}
	}

}
