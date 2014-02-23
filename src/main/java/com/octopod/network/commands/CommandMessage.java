package com.octopod.network.commands;

import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.octopod.network.NetworkConfig;
import com.octopod.network.NetworkPlugin;
import com.octopod.network.LPRequestUtils;

public class CommandMessage extends DocumentedCommand {
	
	public CommandMessage() {
		super("nmsg", "<command> <player> <message>", 
				
			"Sends all players to a server."
					
		);
	}

	@Override
	protected boolean exec(CommandSender cmdsender, Command cmd, String label, String[] args) {
		
		if(args.length <= 1 || !(cmdsender instanceof Player)) return false;

		String sender = cmdsender.getName();
		String target = args[0];
		String message = StringUtils.join(new LinkedList<String>(Arrays.asList(args)).subList(1, args.length), " ");
		String server = NetworkPlugin.connect.getSettings().getUsername();
		
		//Checks if the player is online on the network
		if(LPRequestUtils.isPlayerOnline(target)) {
			LPRequestUtils.sendPlayerMessage(target, String.format(NetworkConfig.getConfig().FORMAT_MSG_TARGET, server, sender, message));
		} else {
			NetworkPlugin.sendMessage(sender, "&cThis player is not online.");
			return true;
		}

		NetworkPlugin.sendMessage(sender, String.format(NetworkConfig.getConfig().FORMAT_MSG_SENDER, server, target, message));

		return true;
		
	}
	
}
