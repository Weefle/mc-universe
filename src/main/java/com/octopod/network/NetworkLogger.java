package com.octopod.network;

import com.octopod.network.bukkit.BukkitUtils;
import org.bukkit.entity.Player;

public class NetworkLogger {

    /**
     * If this is true, it will show all debug messages regardless of the "debug" config option.
     * THIS SHOULDN'T BE TRUE IN RELEASE BUILDS.
     */
    private static final boolean TESTING = true;

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
            for(String message: messages) {
                message = "&8[&6info&8] &7" + message;

                BukkitUtils.console(message);
                for(Player player: BukkitUtils.getPlayers()) {
                    //Check permissions going down levels.
                    for(int i = level; i <= 0; i--) {
                        if(player.hasPermission("network.debug." + i)) {
                            BukkitUtils.sendMessage(player, "&7" + message);
                            break;
                        }
                    }
                }
            }
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
