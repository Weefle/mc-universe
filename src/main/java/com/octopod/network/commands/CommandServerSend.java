package com.octopod.network.commands;

import com.octopod.network.NetworkPlus;
import com.octopod.network.bukkit.BukkitUtils;
import com.octopod.network.NetworkPermission;
import org.bukkit.command.CommandSender;

public class CommandServerSend extends NetworkCommand {

	public CommandServerSend(String root) {
		super(root, "<command> <player> <server>", NetworkPermission.NETWORK_SERVER_SEND,

			"Sends a player to a server."

		);
	}

	Integer[] numArgs = new Integer[]{2};

	@Override
	public Integer[] numArgs() {
		return numArgs;
	}

	@Override
	protected boolean exec(CommandSender sender, String label, String[] args) {

		String player = args[0];
		String server = args[1];

		//Checks if the player is online.
		if(!NetworkPlus.isPlayerOnline(player)) {
			BukkitUtils.sendMessage(sender, "&cThis player is not online.");
			return true;
		}

		//Checks if the server is online before sending them there.
		if(!NetworkPlus.isServerOnline(server)) {
			BukkitUtils.sendMessage(sender, "&cThis server is offline or does not exist.");
			return true;
		}

		//Attempts to send them to the server
		BukkitUtils.sendMessage(sender, "&7Sending &6" + player + " &7to server &b" + server + "&7...");
        NetworkPlus.sendPlayer(player, server);

		return true;

	}

}
