package com.octopod.network.commands;

import com.octopod.network.util.BukkitUtils;
import com.octopod.network.NetworkConfig;
import com.octopod.network.NetworkPermission;
import com.octopod.network.NetworkPlugin;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandMessage extends NetworkCommand {

	public CommandMessage(String root) {
		super(root, "<command> <player> <message>", NetworkPermission.NETWORK_MESSAGE,

			"Sends all players to a server."

		);
	}

	@Override
	protected boolean exec(CommandSender cmdsender, String label, String[] args) {

		if(args.length <= 1 || !(cmdsender instanceof Player)) return false;

        NetworkPlugin plugin = NetworkPlugin.self;

		String sender = cmdsender.getName();
		String target = args[0];
		String message = StringUtils.join(Arrays.asList(args).subList(1, args.length), " ");
		String server = plugin.getUsername();

		//Checks if the player is online on the network
		if(plugin.isPlayerOnline(target)) {
            plugin.sendNetworkMessage(target, String.format(NetworkConfig.FORMAT_MSG_TARGET, server, sender, message));
		} else {
			BukkitUtils.sendMessage(sender, "&cThis player is not online.");
			return true;
		}

        plugin.sendNetworkMessage(sender, String.format(NetworkConfig.FORMAT_MSG_SENDER, server, target, message));

		return true;

	}

}