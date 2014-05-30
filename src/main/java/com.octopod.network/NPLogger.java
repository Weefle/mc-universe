package com.octopod.network;

import com.octopod.network.bukkit.BukkitUtils;
import com.octopod.octal.minecraft.ChatUtils;
import com.octopod.octal.minecraft.ChatUtils.ChatColor;

public class NPLogger {

    /**
     * If this is true, it will show all debug messages regardless of the "debug" config option.
     * THIS SHOULDN'T BE TRUE IN RELEASE BUILDS.
     */
    private static final boolean TESTING = false;

	private static NPLogger instance;

	public NPLogger() {
		instance = this;
	}

	public static NPLogger getLogger() {
		return instance;
	}

    /**
     * Sends a message at a level to the console and players with the "network.debug.<level>" permission.
     * The level should represent the importance of the message (0 being the most important)
     * For example, if the level was 2, the player will need the "network.debug.2" permission (or higher)
     * @param level The level of the message.
     * @param messages An array of messages to send.
     */
    public void log(NPLogLevel level, String... messages)
    {
		for(String message: messages)
		{
			String colored = NetworkPlus.getPrefix() + ChatColor.GRAY + ChatUtils.colorize(message);
			BukkitUtils.console(colored);
		}
    }

    public void i(String... messages) {
		if(NPConfig.getDebugMode() >= NPLogLevel.INFO.getLevel())
        	log(NPLogLevel.INFO, messages);
    }

	public void warn(String... messages) {
		if(NPConfig.getDebugMode() >= NPLogLevel.WARNING.getLevel())
			log(NPLogLevel.WARNING, messages);
	}

    public void d(String... messages) {
		if(NPConfig.getDebugMode() >= NPLogLevel.DEBUG.getLevel())
			log(NPLogLevel.DEBUG, messages);
    }

    public void v(String... messages) {
		if(NPConfig.getDebugMode() >= NPLogLevel.VERBOSE.getLevel())
			log(NPLogLevel.VERBOSE, messages);
    }

}
