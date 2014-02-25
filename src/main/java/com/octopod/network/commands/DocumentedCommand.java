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
public abstract class DocumentedCommand {

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
		return usage.replaceAll("<command>", root);
	}
	
	public String getDescription() {
		return description;
	}
	
	public Integer[] numArgs() {
		return null;
	}

    public boolean weakCommand() {
        return true;
    }

	protected abstract boolean exec(CommandSender sender, String label, String[] args);

	public boolean onCommand(CommandSender sender, String label, String[] args) {

        //If number of arguments matches required for this command
		if((numArgs() != null && !Arrays.asList(numArgs()).contains(args.length))) {

            //If command returns true
            if(exec(sender, label, args)) {
                return true;
            } else
            //If the command isn't weak, show the help entry instead of letting Bukkit handle the command.
            if(!weakCommand()) {
                NetworkPlugin.sendMessage(sender,
                        "&8[&b" + getUsage() + "&8]: " +
                                "&6" + getDescription());
                return true;
            }

		}

        return false;

	}

}
