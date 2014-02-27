package com.octopod.network.commands;

import com.octopod.network.NetworkPermission;
import com.octopod.network.NetworkPlugin;
import com.octopod.network.util.BukkitUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * Extend this class when making commands, and it will automatically be added into the /help command list.
 * @author Octopod
 */
public abstract class NetworkCommand {

	protected String root, usage, description;
    protected NetworkPermission permission;

	public NetworkCommand(String root, String usage, NetworkPermission permission, String description) {
		this.root = root;
		this.usage = usage;
		this.description = description;
        this.permission = permission;
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

        if(!permission.playerHas(sender)) return false;

        //If number of arguments matches required for this command
		if(numArgs() == null || Arrays.asList(numArgs()).contains(args.length)) {

            //If command returns true
            if(exec(sender, label, args)) {
                return true;
            } else
            //If the command isn't weak, show the help entry instead of letting Bukkit handle the command.
            if(!weakCommand()) {
                BukkitUtils.sendMessage(sender,
                        "&8[&b" + getUsage() + "&8]: " +
                                "&6" + getDescription());
                return true;
            }

		}

        return false;

	}

}
