package com.octopod.network.commands;

import com.octopod.network.*;
import com.octopod.network.cache.ServerCache;
import com.octopod.network.events.server.ServerInfoEvent;
import com.octopod.network.events.synclisteners.SyncServerInfoListener;
import com.octopod.octolib.minecraft.AbstractPlayer;
import com.octopod.octolib.minecraft.ChatBuilder;
import com.octopod.octolib.minecraft.ChatUtils;
import com.octopod.octolib.minecraft.bukkit.BukkitPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CommandServerList extends DocumentedCommand{

	public CommandServerList(String root) {
		super(root, "<command>", NetworkPermission.NETWORK_SERVER_LIST,

			"Lists out avaliable servers."

		);
	}

	public boolean exec(CommandSender sender, String label, String[] args) {

		if(!(sender instanceof Player)) {return false;}

		Map<String, ServerInfo> serverMap = ServerCache.getServerMap();

        NetworkPlugin.sendMessage(sender, "&7Found &a" + serverMap.size() + " &7servers. &b(" + "?" + " players)");

		for(Map.Entry entry: serverMap.entrySet()) {
			new ChatBuilder().
                append("    ").
                append(ChatUtils.translateColorCodes("&8[&a" + entry.getKey() + "&8] ")).

                    tooltip(ChatUtils.translateColorCodes(

                            "&aClick to join this server!"

                    , '&')).
                    run("/server " + entry.getKey()).

                append(ChatUtils.translateColorCodes("&b(" + "?" + ")")).
                send(new BukkitPlayer(sender));
		}

		return true;

	}

}