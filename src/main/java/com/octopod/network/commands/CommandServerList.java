package com.octopod.network.commands;

import com.octopod.network.Debug;
import com.octopod.network.LPRequestUtils;
import com.octopod.network.NetworkPlugin;
import com.octopod.network.cache.ServerCache;
import com.octopod.network.events.server.ServerInfoEvent;
import com.octopod.network.events.synclisteners.SyncServerInfoListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CommandServerList extends DocumentedCommand{

	public CommandServerList(String root) {
		super(root, "<command>",

			"Lists out avaliable servers."

		);
	}
	
	public boolean exec(CommandSender sender, String label, String[] args) {
		
		if(!(sender instanceof Player)) {return false;}
		
		long startTime = System.currentTimeMillis();

		final List<String> cachedServers =  ServerCache.getCache().getServers();
		Debug.verbose("&7Expecting responses from: &b" + cachedServers);
		
		LPRequestUtils.requestServerInfo(cachedServers);
		List<ServerInfoEvent> events = SyncServerInfoListener.waitForExecutions(cachedServers.size());

		int playerCount = 0;
		for(ServerInfoEvent event: events) {
			playerCount += event.getServerInfo().getPlayers().size();
		}
		
		long time = System.currentTimeMillis() - startTime;

		NetworkPlugin.sendMessage(sender, "&7Found &a" + events.size() + " &7servers in &f" + time + "ms&7: &b(" + playerCount + " players)");
		
		Collections.sort(events);
		
		for(Iterator<ServerInfoEvent> it = events.iterator(); it.hasNext();) {
			ServerInfoEvent event = it.next();
			NetworkPlugin.sendMessage(sender, 
					"    " +
					"&7(" + event.getPing() + "ms) " +
					"&8[&a" + event.getSender() + "&8] " + 
					"&b(" + event.getServerInfo().getPlayers().size() + ")"
			);
			cachedServers.remove(event.getSender());
		}
		
		for(String extraServer: cachedServers) {
			NetworkPlugin.sendMessage(sender, 
					"    " +
					"&8(?ms) " +
					"&8[" + extraServer + "] " + 
					"&8(?)"
			);			
		}

		return true;
		
	}

}