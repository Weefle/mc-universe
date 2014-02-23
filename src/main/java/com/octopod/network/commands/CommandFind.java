package com.octopod.network.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.octopod.network.NetworkPlugin;
import com.octopod.network.cache.PlayerCache;

public class CommandFind extends DocumentedCommand {

	public CommandFind() {
		super(
			"nfind", "<command> <player>", 
			"Finds a player and attempts to return their location."
		);
	}
	
	Integer[] numArgs = new Integer[]{1};
	
	@Override
	public Integer[] numArgs() {
		return numArgs;
	}	
	
	@Override
	public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length != 1) return false;

		String player = args[0];

		String server = PlayerCache.getCache().findPlayer(player);
		
		if(server == null) {
			NetworkPlugin.sendMessage(sender, "&cThis player was not found.");
		} else {
			NetworkPlugin.sendMessage(sender, "&b" + player + " &7was last seen on &a" + server + "&7!");
		}
		
		return true;
		
	}
}
