package com.octopod.network.commands;

import com.octopod.network.*;
import com.octopod.network.cache.NetworkServerCache;
import com.octopod.network.bukkit.BukkitUtils;
import com.octopod.octolib.minecraft.ChatBuilder;
import com.octopod.octolib.minecraft.ChatUtils;
import com.octopod.octolib.minecraft.bukkit.BukkitPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class CommandServerList extends NetworkCommand {

	public CommandServerList(String root) {
		super(root, "<command>", NetworkPermission.NETWORK_SERVER_LIST,

			"Lists out avaliable servers."

		);
	}

	public boolean exec(CommandSender sender, String label, String[] args) {

		if(!(sender instanceof Player)) {return false;}

		Map<String, ServerFlags> serverMap = NetworkServerCache.getServerMap();
        int totalPlayers = NetworkServerCache.getAllOnlinePlayers().size();

        //Gets the size of all players on the network via LilyPad and gets the difference from the total known players.
        int unlistedPlayerCount = NetworkPlus.getNetworkedPlayers().size() - totalPlayers;

        BukkitUtils.sendMessage(sender, "&7Found &a" + serverMap.size() + " &7servers. &b" + totalPlayers + " players &8(~" + unlistedPlayerCount + " unlisted)", null);
        BukkitUtils.sendMessage(sender, "&7Hover over the server names for more information.", null);

		for(ServerFlags serverInfo: serverMap.values()) {

            String server = serverInfo.getUsername();

            //The list of players on this server.
            List<String> playerList = NetworkServerCache.getOnlinePlayers(server);

            //The playerlist shouldn't be null, but just in case:
            int playerCount = playerList == null ? 0 : playerList.size();

            ChatBuilder cb = new ChatBuilder();

            //If this server is the server the commandsender is on:
            if(server.equals(NetworkPlus.getUsername()))
            {
                cb.appendL("    &8[&f" + serverInfo.getServerName() + "&8] ").
                    tooltip(ChatUtils.translateColorCodes(

                        serverInfo.getServerName() + "\n" +
                        serverInfo.getDescription() + "\n" +
                        "&8-------------------------------------" + "\n" +
                        "&7You're on this server!"

                    , '&')).
                    appendL("&b(" + playerCount + ")").appendL("&f <- You are here!");
            }
            //Default case:
            else
            {
                cb.appendL("    &8[&a" + serverInfo.getServerName() + "&8] ").
                    tooltip(ChatUtils.translateColorCodes(

                        serverInfo.getServerName() + "\n" +
                        serverInfo.getDescription() + "\n" +
                        "&8-------------------------------------" + "\n" +
                        "&7Click to join the server &a" + serverInfo.getServerName() + "&7!"

                    , '&')).run("/server " + serverInfo.getUsername()).
                    appendL("&b(" + playerCount + ")");
            }

            cb.send(new BukkitPlayer(sender));

		}

		return true;

	}

}