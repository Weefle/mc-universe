package com.octopod.network.commands;

import com.octopod.network.NetworkConfig;
import com.octopod.network.NetworkPlus;
import com.octopod.network.bukkit.BukkitUtils;
import com.octopod.network.NetworkPermission;
import com.octopod.network.events.Listener;
import com.octopod.network.events.SynchronizedListener;
import com.octopod.network.events.server.ServerFoundEvent;
import org.bukkit.command.CommandSender;

public class CommandServerPing extends NetworkCommand {

	public CommandServerPing(String root) {
		super(root, "<command> <server>", NetworkPermission.NETWORK_SERVER_PING,

			"Pings a LilyPad server that is running this plugin."

		);
	}

	@Override
	public boolean exec(CommandSender sender, String label, String[] args) {

		final String server = args[0];

		BukkitUtils.sendMessage(sender, "&7Attempting to ping the server &b'" + server + "'");

		if(!NetworkPlus.isServerOnline(server)) {
			BukkitUtils.sendMessage(sender, "&cThis server does not exist.");
			return true;
		}

        final SynchronizedListener listener = new SynchronizedListener<>(ServerFoundEvent.class, new Listener<ServerFoundEvent>()
        {
            @Override
            public void onEvent(ServerFoundEvent event)
            {
                if(event.getServer().equals(server)) {
                    event.setUnlocked(true);
                }
            }
        });

        long timeout = NetworkConfig.getRequestTimeout();

        long startTime = System.currentTimeMillis();

        NetworkPlus.getEventManager().registerListener(listener);

        NetworkPlus.requestServerInfo(server);
        listener.waitFor(timeout, 1);

        if((System.currentTimeMillis() - startTime) >= timeout) {
			BukkitUtils.sendMessage(sender, "&cPing timed out! The server isn't running this plugin?");
		} else {
            BukkitUtils.sendMessage(sender, "&aPing returned successfully in " + (System.currentTimeMillis() - startTime) + "ms!");
        }

        NetworkPlus.getEventManager().unregisterListener(listener);

		return true;

	}

}
