package com.octopod.networkplus.command;

import com.octopod.networkplus.*;
import com.octopod.networkplus.event.events.NetworkMessageEvent;
import com.octopod.networkplus.network.NetworkMessage;
import com.octopod.networkplus.network.NetworkReturn;
import com.octopod.networkplus.requests.ServerInfoRequest;
import com.octopod.networkplus.server.ServerCommandSource;

import java.util.Map.Entry;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class CommandGetInfo extends Command
{
	public CommandGetInfo(String root, String... aliases)
	{
		super(root, aliases, "/serverinfo <server>", Permission.COMMAND_PING,
			"Gets the server info of a server."
		);
	}

	@Override
	protected boolean exec(final ServerCommandSource source, String label, final String[] args)
	{
		if(args.length == 0) return false;

		NetworkMessage message = new ServerInfoRequest();

		message.setReturn(new NetworkReturn()
		{
			@Override
			public void ret(boolean successful, NetworkMessageEvent event)
			{
				if(successful)
				{
					source.sendMessage("&aServerInfo recieved!");
					ServerInfo serverInfo = new CachedServerInfo(event.getParsed()[0]);
					source.sendMessage("&8------------");
					for(Entry<ServerValue, Object> e: serverInfo.getValues().entrySet())
					{
						source.sendMessage("&b" + e.getKey() + "&7: &6" + e.getValue());
					}
					source.sendMessage("&8------------");
				} else {
					source.sendMessage("&cRequest timed out.");
				}
			}
		});

		message.send(args[0]);
		source.sendMessage("&aRequesting ServerInfo from server &f" + args[0] + "&a...");
		return true;
	}
}
