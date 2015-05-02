package com.hyperfresh.mcuniverse.commands;

import com.hyperfresh.mcuniverse.*;
import com.hyperfresh.mcuniverse.database.ServerDatabase;
import com.hyperfresh.mcuniverse.event.events.NetworkPacketInEvent;
import com.hyperfresh.mcuniverse.minecraft.MinecraftCommandSource;
import com.hyperfresh.mcuniverse.packets.*;
import com.hyperfresh.mcuniverse.server.networked.UniverseServer;
import com.hyperfresh.mcuniverse.server.networked.stored.StoredServer;
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
		aliases = {"/servers", "/glist"},
		permission = "switchcore.server.list",
		description = "Shows a list of all avaliable servers."
	)
	@SuppressWarnings("unchecked")
	public void serverList(MinecraftCommandSource source)
	{
		ServerDatabase database = UniverseAPI.getInstance().getServerDatabase();
		new ChatElement("Server List", ChatColor.AQUA, ChatFormat.BOLD);//send(source);
		List<String> servernames = new ArrayList<>(database.getServerNames());
		Collections.sort(servernames);
		for(String servername: servernames)
		{
			UniverseServer server = database.getServer(servername);
			int total_players = server.getOnlinePlayers().size();
			int max_players = server.getMaxPlayers();
			boolean online = server.isOnline();
			ChatElement playercount;
			ChatElement element = new ChatElement("    ");
			if(server instanceof StoredServer)
			{
				if(online)
				{
					element.legacy("&8(&7||||&8)");
				}
				else
				{
					element.legacy("&8(&c||||&8)");
				}
				element.tooltip(
					Chat.colorize(server.getServerName()),
					ChatColor.GRAY + "(Discovered from an external source)",
					ChatColor.DARK_GRAY + "----------------",
					ChatColor.GOLD + "Players: " + ChatColor.WHITE + total_players,
					Chat.colorize("&6Max Players: &f" + (max_players == -1 ? "&7UNKNOWN" : max_players)),
					ChatColor.DARK_GRAY + "----------------",
					Chat.colorize("&b(Click to attempt to join!)")
				).run("/server " + server.getServerUsername());
			} else {
				if(online)
				{
					element.legacy("&8(&a||||&8)");
				}
				else
				{
					element.legacy("&8(&c||||&8)");
				}
				element.tooltip(
					Chat.colorize(server.getServerName()),
					ChatColor.DARK_GRAY + "----------------",
					ChatColor.GOLD + "Players: " + ChatColor.WHITE + total_players,
					Chat.colorize("&6Max Players: &f" + (max_players == -1 ? "&7UNKNOWN" : max_players)),
					ChatColor.DARK_GRAY + "----------------",
					Chat.colorize("&b(Click to join!)")
				).run("/server " + server.getServerUsername());
			}
			element.
				sappend(server.getServerName(), ChatColor.AQUA).
				sappend(":", ChatColor.DARK_GRAY).append(' ');
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
				playercount.color(ChatColor.GOLD);
			}

			element.append(playercount);
			// element.send(source);
		}
	}

	@Command
	(
		aliases = {"/ping"},
		permission = "switchcore.server.ping",
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
						append("+------------------------+");//.
					//send(source, true);
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
		final Packet message = new PacketOutServerPing(id);
		long time = System.currentTimeMillis();

		TempListenerFilter<NetworkPacketInEvent> filter = new TempListenerFilter<NetworkPacketInEvent>()
		{
			@Override
			public boolean onEvent(TempListener<NetworkPacketInEvent> listener, NetworkPacketInEvent event)
			{
				Packet packet = event.getPacket();
				return (packet instanceof PacketInServerPing && ((PacketInServerPing)packet).getPingID() == id);
			}
		};

		message.send(server);
		return new TempListener<>(NetworkPacketInEvent.class, filter).waitFor(500) ? System.currentTimeMillis() - time : -1;
	}

//	@Command
//	(
//		aliases = {"/servervalue"},
//		permission = "switchcore.server.value",
//		description = "Shows a list of all avaliable servers."
//	)
//	public void serverValueRequest(final MinecraftCommandSource source, String server, final ServerValue value)
//	{
//		final Packet message = new PacketOutServerValue(value);
//
//		TempListenerFilter<NetworkPacketInEvent> filter = new TempListenerFilter<NetworkPacketInEvent>()
//		{
//			public boolean onEvent(TempListener<NetworkPacketInEvent> listener, NetworkPacketInEvent event)
//			{
//				if(event.getPacket() instanceof PacketInServerValue)
//				{
//					PacketInServerValue packet = (PacketInServerValue)event.getPacket();
//					source.sendMessage("&b" + packet.getType() + "&7: &6" + packet.getValue());
//					return true;
//				}
//				return false;
//			}
//		};
//
//		TempListenerFinish finish = new TempListenerFinish()
//		{
//			public void finish(boolean success)
//			{
//				if(!success) source.sendMessage("&cRequest Timed Out");
//			}
//		};
//
//		message.send(server);
//		new TempListener<>(NetworkPacketInEvent.class, filter).waitForAsync(500, finish);
//		source.sendMessage("&aRequesting ServerValue " + value.name() + " from server &f" + server + "&a...");
//	}
//
//	@Command
//	(
//		aliases = {"/serverinfo"},
//		permission = "switchcore.server.info",
//		description = "Shows a list of all avaliable servers."
//	)
//	public void serverInfoRequest(final MinecraftCommandSource source, String server)
//	{
//		final Packet message = new PacketOutServerDiscover();
//
//		TempListenerFilter<NetworkPacketInEvent> filter = new TempListenerFilter<NetworkPacketInEvent>()
//		{
//			public boolean onEvent(TempListener<NetworkPacketInEvent> listener, NetworkPacketInEvent event)
//			{
//				if(event.getPacket() instanceof PacketInServerDiscover)
//				{
//					PacketInServerDiscover packet = (PacketInServerDiscover)event.getPacket();
//					Server server = packet.getServer();
//					source.sendMessage("&aRecieved Server from " + event.getServer() + ", " + server.totalValues() + " values.");
//					source.sendMessage("&8------------");
//					for(ServerValue value: ServerValue.values())
//					{
//						source.sendMessage("&b" + value.name() + "&7: &6" + server.getValue(value));
//					}
//					source.sendMessage("&8------------");
//					return true;
//				}
//				return false;
//			}
//		};
//
//		TempListenerFinish finish = new TempListenerFinish()
//		{
//			public void finish(boolean success)
//			{
//				if(!success) source.sendMessage("&cRequest Timed Out");
//			}
//		};
//
//		message.send(server);
//		new TempListener<>(NetworkPacketInEvent.class, filter).waitForAsync(500, finish);
//		source.sendMessage("&aRequesting Server from server &f" + server + "&a...");
//	}
}
