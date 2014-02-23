package com.octopod.network.commands;

import com.octopod.network.NetworkPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * Extend this class when making commands, and it will automatically be added into the /help command list.
 * @author Octopod
 */
public abstract class DocumentedCommand implements CommandExecutor {

	protected String root = "";
	protected String usage = "<command>";
	protected String description = "This is a command.";
	
	public DocumentedCommand(String root, String usage, String description) {
		this.root = root;
		this.usage = usage;
		this.description = description;
	}
	
	public String getLabel() {
		return root;
	}

	public String getUsage() {
		return usage.replaceAll("<command>", "/" + root);
	}
	
	public String getDescription() {
		return description;
	}
	
	public Integer[] numArgs() {
		return null;
	}

	protected abstract boolean exec(CommandSender sender, Command cmd, String label, String[] args);
		
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if((numArgs() != null && !Arrays.asList(numArgs()).contains(args.length)) || !exec(sender, cmd, label, args)) {
			NetworkPlugin.sendMessage(sender, 
				"&8[&b" + getUsage() + "&8]: " +
				"&6" + getDescription());
		}

		return true;
		
	}

}
