package com.octopod.networkplus.commands;

import com.octopod.minecraft.MinecraftCommandSource;
import com.octopod.networkplus.*;
import com.octopod.networkplus.database.ServerDatabase;
import com.octopod.networkplus.event.events.NetworkMessageInEvent;
import com.octopod.networkplus.exceptions.DeserializationException;
import com.octopod.networkplus.packets.*;
import com.octopod.networkplus.packets.PacketOutServerDiscover;
import com.octopod.networkplus.packets.NetworkPacket;
import com.octopod.util.common.Math;
import com.octopod.util.minecraft.chat.*;
import com.octopod.util.minecraft.command.Command;
import com.octopod.util.minecraft.command.Default;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		List<String> servernames = new ArrayList<>(database.getServerNames());
		Collections.sort(servernames);
		for(String servername: servernames)
		{
			Server server = database.getServer(servername);
			int total_players = server.getOnlinePlayers().length;
			ChatElement playercount;
			ChatElement element = new ChatElement("    ");
			if(server.isExternal())
			{
				element.
					append(server.getServerName(), ChatColor.WHITE).
					sappend(":", ChatColor.DARK_GRAY).append(' ');
			} else {
				element.
					append(server.getServerName(), ChatColor.AQUA).
					sappend(":", ChatColor.DARK_GRAY).append(' ');
			}

			if(server.getMaxPlayers() == -1)
			{
				//Unknown max players
				playercount = new ChatElement("(" + total_players + ")");

			} else
			{
				playercount = new ChatElement("(" + total_players + "/" + server.getMaxPlayers() + ")");
			}

			if(total_players == 0)
			{
				playercount.color(ChatColor.DARK_GRAY);
			}
			else
			{
				if(server.isExternal())
				{
					playercount.color(ChatColor.WHITE);
				} else {
					playercount.color(ChatColor.GOLD);
				}
			}

			element.append(playercount);
			element.send(source);
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
		final NetworkPacket message = new PacketOutServerPing(id);
		long time = System.currentTimeMillis();

		TempListenerFilter<NetworkMessageInEvent> filter = new TempListenerFilter<NetworkMessageInEvent>()
		{
			@Override
			public boolean onEvent(TempListener<NetworkMessageInEvent> listener, NetworkMessageInEvent event)
			{
				if(event.getChannel().equals(message.getChannelIn()) && Integer.parseInt(event.getParsed()[0]) == id)
				{
					event.setCancelled(true);
					return true;
				}
				return false;
			}
		};

		message.send(server);
		return new TempListener<>(NetworkMessageInEvent.class, filter).waitFor(500) ? System.currentTimeMillis() - time : -1;
	}

	@Command
	(
		aliases = {"/servervalue"},
		permission = "networkplus.server.value",
		description = "Shows a list of all avaliable servers."
	)
	public void serverValueRequest(final MinecraftCommandSource source, String server, final ServerValue value)
	{
		final NetworkPacket message = new PacketOutServerValue(value);

		TempListenerFilter<NetworkMessageInEvent> filter = new TempListenerFilter<NetworkMessageInEvent>()
		{
			public boolean onEvent(TempListener<NetworkMessageInEvent> listener, NetworkMessageInEvent event)
			{
				if(event.getChannel().equals(message.getChannelIn()))
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
		new TempListener<>(NetworkMessageInEvent.class, filter).waitForAsync(500, finish);
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
		final NetworkPacket message = new PacketOutServerDiscover();

		TempListenerFilter<NetworkMessageInEvent> filter = new TempListenerFilter<NetworkMessageInEvent>()
		{
			public boolean onEvent(TempListener<NetworkMessageInEvent> listener, NetworkMessageInEvent event)
			{
				if(event.getChannel().equals(message.getChannelIn()))
				{
					Server server = CachedServer.decode(event.getServer(), event.getMessage());
					source.sendMessage("&aRecieved Server from " + event.getServer() + ", " + server.totalValues() + " values.");
					source.sendMessage("&8------------");
					for(ServerValue value: ServerValue.values())
					{
						source.sendMessage("&b" + value.name() + "&7: &6" + server.getValue(value));
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
		new TempListener<>(NetworkMessageInEvent.class, filter).waitForAsync(500, finish);
		source.sendMessage("&aRequesting Server from server &f" + server + "&a...");
	}
}
