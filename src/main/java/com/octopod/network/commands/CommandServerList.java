package com.octopod.network.commands;

import com.octopod.network.*;
import com.octopod.network.cache.NetworkPlayerCache;
import com.octopod.network.cache.NetworkServerCache;
import com.octopod.network.util.BukkitUtils;
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

		Map<String, ServerInfo> serverMap = NetworkServerCache.getServerMap();
        Map<String, String> playerMap = NetworkPlayerCache.getPlayerMap();

        //A reverse map where the keys are the servernames and the values are the list of players.
        Map<String, List<String>> reverseMap = NetworkPlayerCache.getReverseMap();

        //Gets the size of all players on the network via LilyPad and gets the difference from the total known players.
        int unlistedPlayerCount = NetworkPlus.getInstance().getNetworkedPlayers().size() - playerMap.size();

        BukkitUtils.sendMessage(sender, "&7Found &a" + serverMap.size() + " &7servers. &b" + playerMap.size() + " players &8(~" + unlistedPlayerCount + " unlisted)", null);
        BukkitUtils.sendMessage(sender, "&7Hover over the server names for more information.", null);

		for(Map.Entry<String, ServerInfo> entry: serverMap.entrySet()) {

            ServerInfo serverInfo = entry.getValue();

            //The list of players on this server.
            List<String> playerList = reverseMap.get(entry.getKey());

            //The playerlist shouldn't be null, but just in case:
            int playerCount = playerList == null ? 0 : playerList.size();

            //If this server is the server the commandsender is on:
            if(entry.getKey().equals(NetworkPlus.getUsername()))
            {
                new ChatBuilder().appendL("    &8[&f" + serverInfo.getUsername() + "&8] ").
                    tooltip(ChatUtils.translateColorCodes(

                        serverInfo.getServerName() + "\n" +
                        serverInfo.getDescription() + "\n" +
                        "&8-------------------------------------" + "\n" +
                        "&7You're on this server!"

                    , '&')).
                    appendL("&b(" + playerCount + ")").appendL("&f <- You are here!").
                    send(new BukkitPlayer(sender));
            }
            //Default case:
            else
            {
                new ChatBuilder().appendL("    &8[&a" + serverInfo.getUsername() + "&8] ").
                    tooltip(ChatUtils.translateColorCodes(

                        serverInfo.getServerName() + "\n" +
                        serverInfo.getDescription() + "\n" +
                        "&8-------------------------------------" + "\n" +
                        "&7Click to join the server &a" + serverInfo.getUsername() + "&7!"

                    , '&')).run("/server " + serverInfo.getUsername()).
                    appendL("&b(" + playerCount + ")").
                    send(new BukkitPlayer(sender));
            }
		}

		return true;

	}

}