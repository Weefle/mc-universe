package com.octopod.network.commands;

import com.octopod.network.NetworkPermission;
import com.octopod.network.cache.NetworkCommandCache;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMaster extends NetworkCommand {

	public final static String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "NXN" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;

	public CommandMaster(String root){
		super(root, "<command> <arguments...>", NetworkPermission.NETWORK_MASTER,

			"Network general command."

		);
	}

	public boolean exec(CommandSender sender, String label, String[] args) {

		if(!(sender instanceof Player)){return false;}

		Player player = (Player)sender;

		if(args.length == 0) {

			player.sendMessage(PREFIX + "Welcome to the Nixium Network!");
			player.sendMessage(PREFIX + "Type /net help to view commands.");

		} else {

			switch(label){
				case "help":
					NetworkCommandCache.getCommand(CommandHelp.class).exec(sender, label, args);
					break;
				case "hub":
				    //player.connect(ProxyServer.getInstance().getServerInfo("core"));
				    break;
				case "join":
					NetworkCommandCache.getCommand(CommandServerConnect.class).exec(sender, label, args);
				    break;
				case "list":
                    NetworkCommandCache.getCommand(CommandServerList.class).exec(sender, label, args);
					break;
				default:
					break;
			}
		}

		return true;

	}

}
