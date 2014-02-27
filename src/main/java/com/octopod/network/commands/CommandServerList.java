package com.octopod.network.commands;

import com.octopod.network.*;
import com.octopod.network.cache.NetworkPlayerCache;
import com.octopod.network.cache.NetworkServerCache;
import com.octopod.network.util.BukkitUtils;
import com.octopod.network.util.RequestUtils;
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
        Map<String, List<String>> reverseMap = NetworkPlayerCache.getReverseMap();

        int unlistedPlayerCount = NetworkPlugin.getNetworkedPlayers().size() - playerMap.size();

        BukkitUtils.sendMessage(sender, "&7Found &a" + serverMap.size() + " &7servers. &b" + playerMap.size() + " players &8(" + unlistedPlayerCount + " unlisted players)");
        BukkitUtils.sendMessage(sender, "&7Hover over the server names for more information.");

		for(Map.Entry<String, ServerInfo> entry: serverMap.entrySet()) {
            List<String> playerList = reverseMap.get(entry.getKey());
            int playerCount = playerList == null ? 0 : playerList.size();
            //If this server is the server the commandsender is on:
            if(entry.getKey().equals(NetworkPlugin.getUsername()))
            {
                new ChatBuilder().appendL("    &8[&f" + entry.getKey() + "&8] ").
                    tooltip(ChatUtils.translateColorCodes(

                        entry.getValue().getServerName() + "\n" +
                        entry.getValue().getMotd() + "\n" +
                        "&8-------------------------------------" + "\n" +
                        "&7You're on this server!"

                    , '&')).
                    append(ChatUtils.translateColorCodes("&b(" + playerCount + ")")).
                    append(ChatUtils.translateColorCodes("&f <- You are here!")).
                    send(new BukkitPlayer(sender));

            } else
            {
                new ChatBuilder().appendL("    &8[&a" + entry.getKey() + "&8] ").
                    tooltip(ChatUtils.translateColorCodes(

                        entry.getValue().getServerName() + "\n" +
                        entry.getValue().getMotd() + "\n" +
                        "&8-------------------------------------" + "\n" +
                        "&7Click to join the server &a" + entry.getKey() + "&7!"

                    , '&')).run("/server " + entry.getKey()).
                    appendL("&b(" + playerCount + ")").
                    send(new BukkitPlayer(sender));
            }
		}

		return true;

	}

}