package com.octopod.networkplus.server.bukkit;

import com.octopod.networkplus.Permission;
import com.octopod.networkplus.server.ServerConsole;
import com.octopod.util.minecraft.ChatUtils;
import org.bukkit.command.ConsoleCommandSender;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitConsole implements ServerConsole
{
	ConsoleCommandSender console;

	public BukkitConsole(ConsoleCommandSender sender)
	{
		this.console = sender;
	}

	@Override
	public String getName()
	{
		return console.getName();
	}

	@Override
	public void sendMessage(String message)
	{
		console.sendMessage(ChatUtils.colorize(message));
	}

	@Override
	public void sendJsonMessage(String message)
	{
		sendMessage(message);
	}

	@Override
	public boolean hasPermission(Permission permission)
	{
		return console.hasPermission(permission.toString());
	}
}
