package com.octopod.network.commands;

import com.octopod.network.NetworkPermission;
import com.octopod.network.cache.NetworkPlayerCache;
import com.octopod.network.util.BukkitUtils;
import org.bukkit.command.CommandSender;

public class CommandFind extends NetworkCommand {

	public CommandFind(String root) {
		super(root, "<command> <player>", NetworkPermission.NETWORK_FIND,

			"Finds a player and attempts to return their location."

		);
	}

	private final Integer[] numArgs = new Integer[]{1};

	@Override
	public Integer[] numArgs() {
		return numArgs;
	}

	@Override
	public boolean exec(CommandSender sender, String label, String[] args) {

		if(args.length != 1) return false;

		String player = args[0];

		String server = NetworkPlayerCache.findPlayer(player);

		if(server == null) {
			BukkitUtils.sendMessage(sender, "&cThis player was not found.");
		} else {
			BukkitUtils.sendMessage(sender, "&b" + player + " &7was last seen on &a" + server + "&7!");
		}

		return true;

	}
}
