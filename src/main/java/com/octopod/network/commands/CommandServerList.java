package com.octopod.network.commands;

import com.octopod.network.NetworkPermission;
import com.octopod.network.NetworkPlus;
import com.octopod.network.ServerFlags;
import com.octopod.network.ServerManager;
import com.octopod.octal.minecraft.ChatBuilder;
import com.octopod.octal.minecraft.ChatElement;
import com.octopod.octal.minecraft.ChatUtils;
import com.octopod.octal.minecraft.ChatUtils.*;
import com.octopod.octal.minecraft.bukkit.BukkitPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CommandServerList extends NetworkCommand {

	public CommandServerList(String root, String... aliases) {
		super(root, aliases, "<command>", NetworkPermission.NETWORK_SERVER_LIST,

			"Lists out avaliable servers."

		);
	}

	public boolean exec(CommandSender sender, String label, String[] args) {

		if(!(sender instanceof Player)) {return false;}

		BukkitPlayer player = new BukkitPlayer(sender);

		List<Map.Entry<String, ServerFlags>> offlineServers = new ArrayList<>();

		Map<String, ServerFlags> flagMap = ServerManager.getServerMap();

		//Gets the size of all players on the network via LilyPad and gets the difference from the total known players.
		//int unlistedPlayerCount = NetworkPlus.getNetworkedPlayers().size() - totalPlayers;

		List<ChatBuilder> lines = new ArrayList<>();

//		lines.add(new ChatBuilder("+---------------------------------------------------+").color(Color.DARK_GRAY));
//
//		lines.add(new ChatBuilder().
//				appendLegacy("&8\u2019| ").
//				append(ChatUtils.blockElement(new ChatElement(Color.GRAY + "Server"), 150, 0)).
//				appendLegacy("&8| ").
//				append(ChatUtils.blockElement(new ChatElement(Color.GRAY + "Players"), 150, 0)).
//				appendLegacy("&8|")
//		);
//
//		lines.add(new ChatBuilder("+---------------------------------------------------+").color(Color.DARK_GRAY));

		lines.add(new ChatBuilder(Color.DARK_GRAY + "-------------------------------------"));
		lines.add(new ChatBuilder(" " + Color.AQUA + "Servers"));
		lines.add(new ChatBuilder(Color.DARK_GRAY + "-------------------------------------"));

		for(Entry<String, ServerFlags> entry: flagMap.entrySet()) {

			String serverID = entry.getKey();
			ServerFlags flags = entry.getValue();

			List<String> players = flags.getOnlinePlayers();

			long lastOnline = flags.getFlagLong("serverLastOnline");

			if(lastOnline > -1) {
				offlineServers.add(entry);
				continue;
			}

			//The playerlist shouldn't be null, but just in case:
			int playerCount = players == null ? 0 : players.size();

			ChatBuilder cb = new ChatBuilder();

			//If this server is the server the commandsender is on:
			if (serverID.equals(NetworkPlus.getServerID()))
			{
				cb.
//						appendLegacy("&8\u2019| ").
						sp().
						append(Color.GRAY + "||||").
						tooltip(
								flags.getServerName(),
								flags.getDescription(),
								Color.DARK_GRAY + "-------------------------------------",
								Color.GRAY + "You're on this server!"
						).
                        sp().
						append(Color.DARK_GRAY + "|").
                        sp().
                        append(Color.WHITE + flags.getServerName()).
                        sp().
						append(Color.AQUA + "(" + playerCount + ")");
//						append(ChatUtils.blockElement(new ChatElement(ChatUtils.colorize(
//								"&f" + flags.getServerName())), 132, 0)).
//						appendLegacy("&8| ").
//						append(ChatUtils.blockElement(new ChatElement(ChatUtils.colorize("&b" + playerCount)), 150, 0)).
//						appendLegacy("&8|");
			}
			//Default case:
			else
			{
				cb.
//						appendLegacy("&8\u2019| ").
						sp().
						append(Color.GREEN + "||||").
                        tooltip(
								flags.getServerName(),
								flags.getDescription(),
								Color.DARK_GRAY + "-------------------------------------",
								Color.YELLOW + "Click to join this server!"
						).run("/server" + serverID).
                        sp().
						append(Color.DARK_GRAY + "|").
                        sp().
						append(Color.WHITE + flags.getServerName()).
                        sp().
						append(Color.AQUA + "(" + playerCount + ")");
//						append(ChatUtils.blockElement(new ChatElement(ChatUtils.colorize(
//								"&f" + flags.getServerName())), 132, 0)).
//						appendLegacy("&8| ").
//						append(ChatUtils.blockElement(new ChatElement(ChatUtils.colorize("&b" + 0)), 150, 0)).
//						appendLegacy("&8|");
			}

			lines.add(cb);

		}

		if(offlineServers.size() > 0)
		{
			for(Entry<String, ServerFlags> entry: offlineServers) {

				ServerFlags flags = entry.getValue();

				long lastOnline = flags.getFlagLong("serverLastOnline");

				String timestamp = new SimpleDateFormat("MMMM dd, YYYY KK:mm aa").format(new Date(lastOnline));

				ChatBuilder cb = new ChatBuilder();
				cb.
//						appendLegacy("&8\u2019| ").
						sp().
						append(Color.RED + "||||").
						tooltip(ChatUtils.colorize(

								flags.getServerName() + "\n" +
								flags.getDescription() + "\n" +
								"&8-------------------------------------" + "\n" +
								"&eLast Online: &f" + timestamp

						)).
						sp().
						append(Color.DARK_GRAY + "|").
						sp().
						append(Color.WHITE + flags.getServerName()).
						sp().
                        append(Color.AQUA + "(0)");
//						append(ChatUtils.blockElement(new ChatElement(ChatUtils.colorize(
//								"&f" + flags.getServerName())), 132, 0)).
//						appendLegacy("&8| ").
//						append(ChatUtils.blockElement(new ChatElement(ChatUtils.colorize("&b" + 0)), 150, 0)).
//						appendLegacy("&8|");

				lines.add(cb);

			}
		}

//		lines.add(new ChatBuilder("+---------------------------------------------------+").color(Color.DARK_GRAY));
		lines.add(new ChatBuilder(Color.DARK_GRAY + "-------------------------------------"));

		for(ChatBuilder builder: lines) {builder.send(player);}

		return true;

	}

}