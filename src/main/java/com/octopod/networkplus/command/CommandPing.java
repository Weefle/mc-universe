package com.octopod.networkplus.command;

import com.octopod.networkplus.Command;
import com.octopod.networkplus.Permission;
import com.octopod.networkplus.event.events.NetworkMessageEvent;
import com.octopod.networkplus.network.NetworkMessage;
import com.octopod.networkplus.network.NetworkReturn;
import com.octopod.networkplus.requests.ServerPingRequest;
import com.octopod.networkplus.server.ServerCommandSource;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class CommandPing extends Command
{
	public CommandPing(String root, String... aliases)
	{
		super(root, aliases, "/ping <server>", Permission.COMMAND_PING,
			"Pings a server."
		);
	}

	@Override
	protected boolean exec(final ServerCommandSource source, String label, final String[] args)
	{
		if(args.length == 0) return false;

		NetworkMessage message = new ServerPingRequest();
		final long time = System.currentTimeMillis();

		message.setReturn(new NetworkReturn()
		{
			@Override
			public void ret(boolean successful, NetworkMessageEvent event)
			{
				if(successful)
				{
					source.sendMessage("&aHello from &f" + event.getServerID() + "&a! (" + (System.currentTimeMillis() - time) + "ms)");
				} else {
					source.sendMessage("&cPing timed out.");
				}
			}
		});

		message.send(args[0]);
		source.sendMessage("&aPinging server &f" + args[0] + "&a...");
		return true;
	}
}
