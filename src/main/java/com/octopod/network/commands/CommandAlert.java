package com.octopod.network.commands;

import com.octopod.network.NetworkPlugin;
import com.octopod.network.NetworkConfig;
import com.octopod.network.NetworkPermission;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class CommandAlert extends NetworkCommand {

	public CommandAlert(String root) {
		super(root, "<command> <message...>", NetworkPermission.NETWORK_ALERT,

			"Broadcasts a message to the entire network. " +
			"Only servers that are running this plugin will recieve the alert."

		);
	}

	@Override
	protected boolean exec(CommandSender sender, String label, String[] args) {

		String message = StringUtils.join(args, " ");

		NetworkPlugin.broadcastNetworkMessage(String.format(NetworkConfig.getConfig().FORMAT_ALERT, message));

		return true;

	}

}
