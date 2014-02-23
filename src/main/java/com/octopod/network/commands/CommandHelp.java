package com.octopod.network.commands;

import com.octopod.network.NetworkPlugin;
import com.octopod.network.cache.CommandCache;
import com.octopod.octolib.minecraft.ChatBuilder;
import com.octopod.octolib.minecraft.ChatElement;
import com.octopod.octolib.minecraft.ChatUtils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHelp extends DocumentedCommand{

	public CommandHelp() {
		super(
			"nhelp", "<command>", 
			"Lists out avaliable commands."
		);
	}

	@Override
	public boolean exec(CommandSender sender, Command cmd, String label, String[] args) {

		NetworkPlugin.sendMessage(sender, "&8-----------------------------------------------------");
		NetworkPlugin.sendMessage(sender, new ChatBuilder().appendBlock(new ChatElement("Network Commands").color(Color.AQUA), 320, 2).toLegacy());
		NetworkPlugin.sendMessage(sender, "&8-----------------------------------------------------");
		
		for(DocumentedCommand command: CommandCache.getCache().getCommands().values()) {
			NetworkPlugin.sendMessage(sender, "&8[&b" + command.getUsage() + "&8]: &6" + command.getDescription());
		}
	
		return true;
	}
}
