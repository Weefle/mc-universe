package com.octopod.network.commands;

import com.octopod.network.NetworkPermission;
import com.octopod.network.cache.NetworkCommandCache;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommandMaster extends NetworkCommand {

	public final static String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "NXN" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;

	public CommandMaster(String root){
		super(root, "<command> <arguments...>", NetworkPermission.NETWORK_MASTER,

			"Network general command."

		);
	}

	public boolean exec(CommandSender sender, String vlabel, String[] vargs) {

		if(!(sender instanceof Player)){return false;}

		Player player = (Player)sender;

        List<String> argList = Arrays.asList(vargs);


		if(vargs.length == 0) {

			player.sendMessage(PREFIX + "Welcome to the Nixium Network!");
			player.sendMessage(PREFIX + "Type /net help to view commands.");

		} else {

            String label = argList.get(0);
            String[] args;

            if(argList.size() == 1) {
                args = new String[0];
            } else {
                args = (String[])new LinkedList(argList).subList(1, argList.size()).toArray(new String[argList.size() - 1]);
            }

			switch(label){
				case "help":
					return NetworkCommandCache.getCommand(CommandHelp.class).onCommand(sender, label, args);
				case "hub":
				    return NetworkCommandCache.getCommand(CommandHub.class).onCommand(sender, label, args);
				case "join":
                    return NetworkCommandCache.getCommand(CommandServerConnect.class).onCommand(sender, label, args);
				case "list":
                    return NetworkCommandCache.getCommand(CommandServerList.class).onCommand(sender, label, args);
				default:
					break;
			}
		}

		return true;

	}

}
