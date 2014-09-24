package com.octopod.networkplus;

import com.octopod.networkplus.server.ServerCommandSource;

import java.util.Arrays;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class Command implements Comparable<Command> {

	protected String[] aliases;
	protected String root, usage, description;
	protected Permission permission;

	public Command(String root, String[] aliases, String usage, Permission permission, String description) {
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

	protected abstract boolean exec(ServerCommandSource sender, String label, String[] args);

	public boolean startCommand(ServerCommandSource sender, String label, String[] args) {

		if(!sender.hasPermission(permission))
		{
			sender.sendMessage("&cSorry, you don't have permission to use this command.");
			return true;
		}

		//If number of arguments matches required for this command
		if(numArgs() == null || Arrays.asList(numArgs()).contains(args.length)) {

			//If command returns true
			if(exec(sender, label, args)) {
				return true;
			}

		}

		//If the command isn't weak, show the help entry instead of letting Bukkit handle the command.
		if(!weakCommand()) {
			sender.sendMessage("&8[&b" + getUsage() + "&8]: " + "&6" + getDescription());
			return true;
		}

		return false;

	}

	@Override
	public int compareTo(Command command) {
		return this.getLabel().compareTo(command.getLabel());
	}


}
