package com.octopod.networkplus;

import com.octopod.networkplus.server.ServerInterface;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class Logger
{
	ServerInterface server = null;

	public Logger(ServerInterface server)
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
		server.broadcast(message, level.getPermission().toString());
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
		INFO(Permission.LOGGER_INFO),
		WARNING(Permission.LOGGER_WARNING),
		VERBOSE(Permission.LOGGER_VERBOSE);

		Permission permission;

		private LoggerLevel(Permission perm)
		{
			permission = perm;
		}

		public Permission getPermission()
		{
			return permission;
		}
	}
}
