package com.octopod.networkplus.commands;

import com.octopod.minecraft.MinecraftCommandSource;
import com.octopod.networkplus.*;
import com.octopod.networkplus.database.ServerDatabase;
import com.octopod.networkplus.event.events.NetworkMessageEvent;
import com.octopod.networkplus.exceptions.DeserializationException;
import com.octopod.networkplus.messages.MessageInServerRequest;
import com.octopod.networkplus.messages.MessageOutServerPing;
import com.octopod.networkplus.messages.MessageOutServerValue;
import com.octopod.networkplus.messages.NetworkMessage;
import com.octopod.util.common.Math;
import com.octopod.util.minecraft.chat.*;
import com.octopod.util.minecraft.command.Command;
import com.octopod.util.minecraft.command.Default;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ServerCommands
{
	@Command
	(
		aliases = {"/glist"},
		permission = "networkplus.server.list",
		description = "Shows a list of all avaliable servers."
	)
	@SuppressWarnings("unchecked")
	public void serverList(MinecraftCommandSource source)
	{
		ServerDatabase database = NetworkPlus.getServerDatabase();
		new ChatElement("Server List", ChatColor.AQUA, ChatFormat.BOLD).send(source);
		for(Server server: database.getServers())
		{
			new ChatElement("    ").
				append(server.getServerName(), ChatColor.AQUA).
				sappend(":", ChatColor.DARK_GRAY).
				sappend("(" + server.getOnlinePlayers().length + "/" + server.getMaxPlayers() + ")", ChatColor.GOLD).
			send(source);
		}
	}

	@Command
	(
		aliases = {"/ping"},
		permission = "networkplus.server.ping",
		description = "Pings a server."
	)
	public void ping(final MinecraftCommandSource source, final String server, @Default("1") final int pings)
	{
		source.sendMessage("&aPinging &f\"" + server + "\"&a " + pings + " times");

		new Thread()
		{
			public void run()
			{
				double valid_pings = 0;
				double total_time = 0;
				for(int i = 0; i < pings; i++)
				{
					long ping = ping(i, server);
					if(ping > -1)
					{
						valid_pings++;
						total_time += ping;
					}
				}
				if(valid_pings > 0)
				{
					ChatElement e_title = new ChatElement("- Ping Results -", ChatColor.GREEN);
					ChatElement e_ping = new ChatElement(Chat.colorize("&aAverage Ping&7: &f" + Math.round(total_time / valid_pings, 2) + "ms"));
					ChatElement e_time = new ChatElement(Chat.colorize("&aTotal Time&7: &f" + (int)total_time));

					new ChatElement(ChatColor.DARK_GRAY).
						append("+------------------------+").
						append(new ChatElement(ChatColor.DARK_GRAY).filler(2).append('|').append(e_title).block(148, ChatAlignment.CENTER).append('|')).
						append(new ChatElement(ChatColor.DARK_GRAY).filler(2).append('|').filler(2).append("------------------------").filler(2).append('|')).
						append(new ChatElement(ChatColor.DARK_GRAY).filler(2).append('|').sp().append(e_ping).block(144, ChatAlignment.LEFT).append('|')).
						append(new ChatElement(ChatColor.DARK_GRAY).filler(2).append('|').sp().append(e_time).block(144, ChatAlignment.LEFT).append('|')).
						append("+------------------------+").
					send(source, true);
				}
				else
				{
					source.sendMessage("&aPing&7: &cTIMED OUT");
				}
			}
		}.start();
	}

	private long ping(final int id, String server)
	{
		final NetworkMessage message = new MessageOutServerPing(id);
		long time = System.currentTimeMillis();

		TempListenerFilter<NetworkMessageEvent> filter = new TempListenerFilter<NetworkMessageEvent>()
		{
			@Override
			public boolean onEvent(TempListener<NetworkMessageEvent> listener, NetworkMessageEvent event)
			{
				if(event.getChannel().equals(message.getReturnChannel()) && Integer.parseInt(event.getParsed()[0]) == id)
				{
					event.setCancelled(true);
					return true;
				}
				return false;
			}
		};

		message.send(server);
		return new TempListener<>(NetworkMessageEvent.class, filter).waitFor(500) ? System.currentTimeMillis() - time : -1;
	}

	@Command
	(
		aliases = {"/servervalue"},
		permission = "networkplus.server.value",
		description = "Shows a list of all avaliable servers."
	)
	public void serverValueRequest(final MinecraftCommandSource source, String server, final ServerValue value)
	{
		final NetworkMessage message = new MessageOutServerValue(value);

		TempListenerFilter<NetworkMessageEvent> filter = new TempListenerFilter<NetworkMessageEvent>()
		{
			public boolean onEvent(TempListener<NetworkMessageEvent> listener, NetworkMessageEvent event)
			{
				if(event.getChannel().equals(message.getReturnChannel()))
				{
					try
					{
						Object obj = NetworkPlus.getSerializer().deserialize(event.getParsed()[1], value.expectedType());
						source.sendMessage("&b" + event.getParsed()[0] + "&7: &6" + obj);
					}
					catch (DeserializationException e)
					{
						source.sendMessage("&cServer " + event.getServer() + " has sent an invalid value for " + event.getParsed()[0] + ", &6" + event.getParsed()[1]);
					}
					return true;
				}
				return false;
			}
		};

		TempListenerFinish finish = new TempListenerFinish()
		{
			public void finish(boolean success)
			{
				if(!success) source.sendMessage("&cRequest Timed Out");
			}
		};

		message.send(server);
		new TempListener<>(NetworkMessageEvent.class, filter).waitForAsync(500, finish);
		source.sendMessage("&aRequesting ServerValue " + value.name() + " from server &f" + server + "&a...");
	}

	@Command
	(
		aliases = {"/serverinfo"},
		permission = "networkplus.server.info",
		description = "Shows a list of all avaliable servers."
	)
	public void serverInfoRequest(final MinecraftCommandSource source, String server)
	{
		final NetworkMessage message = new MessageInServerRequest();

		TempListenerFilter<NetworkMessageEvent> filter = new TempListenerFilter<NetworkMessageEvent>()
		{
			public boolean onEvent(TempListener<NetworkMessageEvent> listener, NetworkMessageEvent event)
			{
				if(event.getChannel().equals(message.getReturnChannel()))
				{
					source.sendMessage("&aServerInfo recieved!");
					Server serverInfo = new CachedServer(event.getMessage());
					source.sendMessage("&8------------");
					for(ServerValue value: ServerValue.values())
					{
						source.sendMessage("&b" + value.name() + "&7: &6" + serverInfo.getValue(value));
					}
					source.sendMessage("&8------------");
					return true;
				}
				return false;
			}
		};

		TempListenerFinish finish = new TempListenerFinish()
		{
			public void finish(boolean success)
			{
				if(!success) source.sendMessage("&cRequest Timed Out");
			}
		};

		message.send(server);
		new TempListener<>(NetworkMessageEvent.class, filter).waitForAsync(500, finish);
		source.sendMessage("&aRequesting Server from server &f" + server + "&a...");
	}
}
