package com.octopod.network.commands;

import com.octopod.network.util.BukkitUtils;
import com.octopod.network.util.RequestUtils;
import com.octopod.network.NetworkPermission;
import com.octopod.network.NetworkPlugin;
import org.bukkit.command.CommandSender;

public class CommandServerSendAll extends NetworkCommand {

	public CommandServerSendAll(String root) {
		super(root, "<command> <server>", NetworkPermission.NETWORK_SERVER_SENDALL,

			"Sends all players to a server."

		);
	}

	Integer[] numArgs = new Integer[]{1, 2};

	@Override
	public Integer[] numArgs() {
		return numArgs;
	}

	@Override
	protected boolean exec(CommandSender sender, String label, String[] args) {

		String from = null, server;
		if(args.length == 2) {
			from = args[0];
			server = args[1];
		} else {
			server = args[0];
		}

		//Checks if the server is online before sending them there.
		if(!NetworkPlugin.isServerOnline(server)) {
			BukkitUtils.sendMessage(sender, "&cThis server is offline or does not exist.");
			return true;
		}

		if(from == null) {
			BukkitUtils.sendMessage(sender, "&7Sending all players to server &6" + server + "&7...");
			RequestUtils.requestSendAll(server);
		} else {
			BukkitUtils.sendMessage(sender, "&7Sending all players from server &6" + from + "&7 to server &6 " + server + "&7...");
			RequestUtils.requestSendAll(from, server);
		}

		return true;

	}

}
