package com.octopod.network.commands;

import com.octopod.network.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.octopod.network.bukkit.BukkitUtils;
import com.octopod.network.cache.NetworkServerCache;

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
		if(server.equals(NetworkPlus.getServerID())) {
			BukkitUtils.sendMessage(sender, "&cYou are already connected to this server.");
			return true;
		}

		//Checks if the message went through before sending them there.
		if(!NetworkPlus.isServerOnline(server)) {
            BukkitUtils.sendMessage(sender, "&cThis server is offline or does not exist.");
			return true;
		}
		
		// Checks if the server is full before sending them there.
		if (NetworkPlus.isServerFull(server)) {
			// Make sure they're not already in a queue
			for (ServerFlags flags : NetworkServerCache.getServerMap().values()) {
				if (flags.getQueuedPlayers().contains(player)) {
					BukkitUtils.sendMessage(
							sender,
							"&cYou're already queued to join &a"
									+ flags.getServerName());
					return true;
				}
			}

			// Broadcast message to be added to queue
			BukkitUtils.sendMessage(sender, "&c" + server
					+ " is full. Adding you to queue.");
			String channel = NetworkConfig.Channels.PLAYER_JOIN_QUEUE.toString();
			if (!player.hasPermission(NetworkPermission.NETWORK_QUEUE_BYPASS
					.toString())) {
				NetworkPlus.broadcastMessage(channel,
                        new ServerMessage(player.getName(), server, "0")
                );
			} else {
				int vipInQueue = NetworkQueueManager.instance
						.getVIPQueueMembers();
				NetworkPlus.broadcastMessage(channel,
                        new ServerMessage(player.getName(), server, String.valueOf(vipInQueue + 1))
                );
			}
			return true;
		}

		NetworkPlus.sendPlayer(player.getName(), server);

		return true;

	}

}
