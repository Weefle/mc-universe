package com.octopod.network.commands;

import com.octopod.network.NetworkPlus;
import com.octopod.network.util.BukkitUtils;
import com.octopod.network.NetworkPermission;
import com.octopod.network.NetworkPlusPlugin;
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
		if(server.equals(NetworkPlus.getUsername())) {
			BukkitUtils.sendMessage(sender, "&cYou are already connected to this server.");
			return true;
		}

		//Checks if the message went through before sending them there.
		if(!NetworkPlus.getInstance().isServerOnline(server)) {
            BukkitUtils.sendMessage(sender, "&cThis server is offline or does not exist.");
			return true;
		}

		NetworkPlus.getInstance().sendPlayer(player.getName(), server);

		return true;

	}

}
