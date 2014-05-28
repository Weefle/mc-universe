package com.octopod.network;

import com.octopod.network.bukkit.BukkitUtils;
import com.octopod.octal.minecraft.ChatUtils;
import com.octopod.octal.minecraft.ChatUtils.ChatColor;
import org.bukkit.entity.Player;

public class NetworkLogger {

    /**
     * If this is true, it will show all debug messages regardless of the "debug" config option.
     * THIS SHOULDN'T BE TRUE IN RELEASE BUILDS.
     */
    private static final boolean TESTING = false;

    /**
     * Sends a message at a level to the console and players with the "network.debug.<level>" permission.
     * The level should represent the importance of the message (0 being the most important)
     * For example, if the level was 2, the player will need the "network.debug.2" permission (or higher)
     * @param level The level of the message.
     * @param messages An array of messages to send.
     */
    public void log(int level, String... messages)
    {
        if(NetworkConfig.getDebugMode() >= level || TESTING)
        {
            send(messages);
			for(Player player: BukkitUtils.getPlayers()) {
				//Check permissions going down levels.
				if(level == 0 && player.hasPermission("network.log")) {
					send(player, messages);
					break;
				}
				for(int i = level; i <= 1; i--) {
					if(player.hasPermission("network.log." + i)) {
						send(player, messages);
						break;
					}
				}
			}
        }
    }

	private void send(String... messages) {
		for(String message: messages) {
			BukkitUtils.console(ChatColor.GRAY + ChatUtils.colorize(message));
		}
	}

	private void send(Player player, String... messages) {
		for(String message: messages) {
			player.sendMessage(ChatColor.GRAY + ChatUtils.colorize(message));
		}
	}

    public void info(String... messages) {
        log(0, messages);
    }

    public void debug(String... messages) {
        log(1, messages);
    }

    public void verbose(String... messages) {
        log(2, messages);
    }

}
