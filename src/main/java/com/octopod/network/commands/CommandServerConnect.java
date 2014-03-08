package com.octopod.network.commands;

import com.octopod.network.util.BukkitUtils;
import com.octopod.network.NetworkPermission;
import com.octopod.network.NetworkPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandServerConnect extends NetworkCommand {

    public CommandServerConnect(String root) {
        super(root, "<command> <server>", NetworkPermission.NETWORK_SERVER_CONNECT,

			"Attempts to send you to another server. The console cannot run this command."

		);
	}

	Integer[] numArgs = new Integer[]{1};

	@Override
	public Integer[] numArgs() {
		return numArgs;
	}

	@Override
	protected boolean exec(CommandSender sender, String label, String[] args) {

		if(!(sender instanceof Player)) return false;

		Player player = (Player)sender;
		String server = args[0];

		//Checks if the server they're trying to connect to is this same server
		if(server.equals(NetworkPlugin.self.getUsername())) {
			BukkitUtils.sendMessage(sender, "&cYou are already connected to this server.");
			return true;
		}

		//Checks if the message went through before sending them there.
		if(!NetworkPlugin.self.isServerOnline(server)) {
            BukkitUtils.sendMessage(sender, "&cThis server is offline or does not exist.");
			return true;
		}

		NetworkPlugin.self.sendPlayer(player.getName(), server);

		return true;

	}

}
