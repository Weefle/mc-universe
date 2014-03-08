package com.octopod.network.commands;

import com.octopod.network.NetworkConfig;
import com.octopod.network.events.EventListener;
import com.octopod.network.events.SynchronizedListener;
import com.octopod.network.util.BukkitUtils;
import com.octopod.network.NetworkPermission;
import com.octopod.network.NetworkPlugin;
import com.octopod.network.events.server.ServerInfoEvent;
import com.octopod.network.events.synclisteners.SyncServerInfoListener;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class CommandServerPing extends NetworkCommand {

	public CommandServerPing(String root) {
		super(root, "<command> <server>", NetworkPermission.NETWORK_SERVER_PING,

			"Pings a LilyPad server that is running this plugin."

		);
	}

	@Override
	public boolean exec(CommandSender sender, String label, String[] args) {

		final String server = args[0];
        final CommandSender fsender = sender;

		BukkitUtils.sendMessage(sender, "&7Attempting to ping the server &b'" + server + "'");

		if(!NetworkPlugin.self.isServerOnline(server)) {
			BukkitUtils.sendMessage(sender, "&cThis server does not exist.");
			return true;
		}

		NetworkPlugin.self.requestServerInfo(Arrays.asList(server));

        final SyncServerInfoListener listener = new SyncServerInfoListener(new EventListener<ServerInfoEvent>() {

            @Override
            public boolean onEvent(ServerInfoEvent event)
            {
                if(event.getSender().equals(server)) {
                    BukkitUtils.sendMessage(fsender, "&aPing returned successful!");
                    return true;
                }
                return false;
            }

        });

        long timeout = NetworkConfig.getRequestTimeout();

        long startTime = System.currentTimeMillis();
        listener.register().waitFor(timeout, 1);

        if((System.currentTimeMillis() - startTime) >= timeout) {
			BukkitUtils.sendMessage(sender, "&cPing timed out! The server isn't running this plugin?");
		}

        listener.unregister();

		return true;

	}

}
