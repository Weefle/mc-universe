package com.octopod.network.commands;

import com.octopod.network.NetworkPlus;
import com.octopod.network.bukkit.BukkitUtils;
import com.octopod.network.NetworkConfig;
import com.octopod.network.NetworkPermission;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandMessage extends NetworkCommand {

	public CommandMessage(String root, String... aliases) {
		super(root, aliases, "<command> <player> <message>", NetworkPermission.NETWORK_MESSAGE,

			"Sends all players to a server."

		);
	}

    @Override
    public boolean weakCommand() {
        return true;
    }

	@Override
	protected boolean exec(CommandSender cmdsender, String label, String[] args) {

		if(args.length <= 1 || !(cmdsender instanceof Player)) return false;

		String sender = cmdsender.getName();
		String target = args[0];
		String message = StringUtils.join(Arrays.asList(args).subList(1, args.length), " ");
		String server = NetworkPlus.getServerID();

        String formatTarget = String.format(NetworkConfig.FORMAT_MSG_TARGET, server, sender, message);
        String formatSender = String.format(NetworkConfig.FORMAT_MSG_SENDER, server, target, message);

		//Checks if the player is online on the network
		if(NetworkPlus.isPlayerOnline(target)) {
            NetworkPlus.sendNetworkMessage(target, formatTarget);
            NetworkPlus.sendNetworkMessage(sender, formatSender);
		} else {
			BukkitUtils.sendMessage(sender, "&cThis player is not online.");
		}

		return true;

	}

}