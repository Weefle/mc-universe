package com.octopod.networkplus.server;

import java.util.Arrays;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class ServerCommand implements Comparable<ServerCommand> {

	protected String[] aliases;
	protected String root, usage, description;
	protected ServerPermission permission;

	public ServerCommand(String root, String[] aliases, String usage, ServerPermission permission, String description) {
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

	protected abstract boolean exec(ServerCommandSender sender, String label, String[] args);

	public boolean startCommand(ServerCommandSender sender, String label, String[] args) {

		if(sender.hasPermission(permission)) return false;

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
	public int compareTo(ServerCommand command) {
		return this.getLabel().compareTo(command.getLabel());
	}


}
