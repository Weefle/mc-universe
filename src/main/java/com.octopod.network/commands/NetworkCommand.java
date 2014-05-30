package com.octopod.network.commands;

import com.octopod.network.NPPermission;
import com.octopod.network.bukkit.BukkitUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * Extend this class when making commands, and it will automatically be added into the /help command list.
 * @author Octopod
 */
public abstract class NetworkCommand implements Comparable<NetworkCommand> {

    protected String[] aliases;
	protected String root, usage, description;
    protected NPPermission permission;

	public NetworkCommand(String root, String[] aliases, String usage, NPPermission permission, String description) {
		this.aliases = aliases;
        this.root = root;
		this.usage = usage;
		this.description = description;
        this.permission = permission;
	}


	public String getLabel() {
		return root;
	}

    public String[] getAliases() {
        return aliases;
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
        return false;
    }

	protected abstract boolean exec(CommandSender sender, String label, String[] args);

	public boolean onCommand(CommandSender sender, String label, String[] args) {

        if(!permission.senderHas(sender)) return false;

        //If number of arguments matches required for this command
		if(numArgs() == null || Arrays.asList(numArgs()).contains(args.length)) {

            //If command returns true
            if(exec(sender, label, args)) {
                return true;
            }

		}

        //If the command isn't weak, show the help entry instead of letting Bukkit handle the command.
        if(!weakCommand()) {
            BukkitUtils.sendMessage(sender,
                    "&8[&b" + getUsage() + "&8]: " +
                            "&6" + getDescription());
            return true;
        }

        return false;

	}

    @Override
    public int compareTo(NetworkCommand command) {
        return this.getLabel().compareTo(command.getLabel());
    }

}
