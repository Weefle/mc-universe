package com.octopod.network;

import com.octopod.network.util.BukkitUtils;
import org.bukkit.entity.Player;

public class NetworkLogger {
	
	/**
	 * Sends a message to the console and players with the "network.debug" permission.
	 * Players will see messages sent using this method regardless of what the debug option is.
	 * @param messages An array of messages to send.
	 */
	public void info(String... messages)
	{
        if(NetworkConfig.getDebugMode() >= 0)
        {
            for(String message: messages) {

                BukkitUtils.console(message);
                for(Player player: BukkitUtils.getPlayers()) {
                    if(player.hasPermission("network.debug")) {
                        BukkitUtils.sendMessage(player, "&7" + message);
                    }
                }
            }
        }
	}	
	
	/**
	 * Sends a message to the console and players with the "network.debug" permission.
	 * Players will see messages sent using this method if the debug option is at least 1.
	 * @param messages An array of messages to send.
	 */	
	public void debug(String... messages)
	{
		if(NetworkConfig.getDebugMode() >= 1)
		{
			for(String message: messages) {
				message = "&7[i] &7" + message;
				
				BukkitUtils.console(message);
				for(Player player: BukkitUtils.getPlayers()) {
					if(player.hasPermission("network.debug")) {
						BukkitUtils.sendMessage(player, message);
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
	public void verbose(String... messages)
	{
		if(NetworkConfig.getDebugMode() >= 2)
		{
			for(String message: messages) {
				message = "&c[i] &7" + message;
				
				BukkitUtils.console(message);
				for(Player player: BukkitUtils.getPlayers()) {
					if(player.hasPermission("network.debug.verbose")) {
                        BukkitUtils.sendMessage(player, message);
					}
				}				
			}		
		}
	}

}
