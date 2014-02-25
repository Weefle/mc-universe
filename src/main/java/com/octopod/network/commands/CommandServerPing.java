package com.octopod.network.commands;

import com.octopod.network.LPRequestUtils;
import com.octopod.network.NetworkPermission;
import com.octopod.network.NetworkPlugin;
import com.octopod.network.events.server.ServerInfoEvent;
import com.octopod.network.events.synclisteners.SyncServerInfoListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CommandServerPing extends DocumentedCommand {

	public CommandServerPing(String root) {
		super(root, "<command> <server>", NetworkPermission.NETWORK_SERVER_PING,

			"Pings a LilyPad server that is running this plugin."

		);
	}

	@Override
	public boolean exec(CommandSender sender, String label, String[] args) {

		final String server = args[0];
		
		NetworkPlugin.sendMessage(sender, "&7Attempting to ping the server &b'" + server + "'");
		
		if(!LPRequestUtils.serverExists(server)) {
			NetworkPlugin.sendMessage(sender, "&cThis server does not exist.");
			return true;
		}
		
		LPRequestUtils.requestServerInfo(Arrays.asList(server));
		List<ServerInfoEvent> events = SyncServerInfoListener.waitForExecutions(1, Arrays.asList(server));

		try {
			long ping = events.get(0).getPing();
			NetworkPlugin.sendMessage(sender, "&aPing returned successful in " + ping + "ms!");
		} catch (IndexOutOfBoundsException e) {
			NetworkPlugin.sendMessage(sender, "&cPing timed out! The server isn't running this plugin?");
		}
		
		return true;
		
	}

}
