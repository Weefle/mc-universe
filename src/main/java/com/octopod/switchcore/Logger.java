package com.octopod.switchcore;

import com.octopod.minecraft.MinecraftServerInterface;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class Logger
{
	MinecraftServerInterface server = null;

	public Logger(MinecraftServerInterface server)
	{
		this.server = server;
	}

	/**
	 * Logs a message to the server and the console.
	 *
	 * @param level the level to log the message at
	 * @param message the message to log
	 */
	public void log(LoggerLevel level, String message)
	{
		server.broadcast(message, level.node());
		server.console(message);
	}

	/**
	 * Logs a message on the INFO level
	 *
	 * @param message the message to log
	 */
	public void i(String message)
	{
		log(LoggerLevel.INFO, message);
	}

	/**
	 * Logs a message on the WARNING level
	 *
	 * @param message the message to log
	 */
	public void w(String message)
	{
		log(LoggerLevel.WARNING, message);
	}

	/**
	 * Logs a message on the VERBOSE level
	 *
	 * @param message the message to log
	 */
	public void v(String message)
	{
		log(LoggerLevel.VERBOSE, message);
	}

	public static enum LoggerLevel
	{
		INFO("switchcore.log.info"),
		WARNING("switchcore.log.warning"),
		VERBOSE("switchcore.log.verbose");

		String node;

		private LoggerLevel(String node)
		{
			this.node = node;
		}

		public String node() {return node;}
	}
}
