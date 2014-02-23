package com.octopod.network;

import org.bukkit.entity.Player;

public class Debug {
	
	/**
	 * Sends a message to the console and players with the "network.debug" permission.
	 * Players will see messages sent using this method regardless of what the debug option is.
	 * @param messages An array of messages to send.
	 */
	public static void info(String... messages) 
	{
		for(String message: messages) {

			NetworkPlugin.console(message);
			for(Player player: NetworkPlugin.getPlayers()) {
				if(player.hasPermission("network.debug")) {
					NetworkPlugin.sendMessage(player, NetworkPlugin.PREFIX + message);
				}
			}				
		}		
	}	
	
	/**
	 * Sends a message to the console and players with the "network.debug" permission.
	 * Players will see messages sent using this method if the debug option is at least 1.
	 * @param messages An array of messages to send.
	 */	
	public static void debug(String... messages) 
	{
		if(NetworkConfig.getConfig().getDebugMode() >= 1) 
		{
			for(String message: messages) {
				message = "&7[D] &7" + message;
				
				NetworkPlugin.console(message);
				for(Player player: NetworkPlugin.getPlayers()) {
					if(player.hasPermission("network.debug")) {
						NetworkPlugin.sendMessage(player, NetworkPlugin.PREFIX + message);
					}
				}				
			}		
		}
	}
	
	/**
	 * Sends a message to the console and players with the "network.debug" permission.
	 * Players will see messages sent using this method if the debug option is at least 2.
	 * @param messages An array of messages to send.
	 */		
	public static void verbose(String... messages) 
	{
		if(NetworkConfig.getConfig().getDebugMode() >= 2) 
		{
			for(String message: messages) {
				message = "&c[V] &7" + message;
				
				NetworkPlugin.console(message);
				for(Player player: NetworkPlugin.getPlayers()) {
					if(player.hasPermission("network.debug.verbose")) {
						NetworkPlugin.sendMessage(player, NetworkPlugin.PREFIX + message);
					}
				}				
			}		
		}
	}

}
