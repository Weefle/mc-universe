package com.octopod.networkplus.server;

import com.octopod.networkplus.NetworkPlus;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ServerLogger
{
	ServerInterface server = null;

	/**
	 * Gets the current ServerInterface. if ServerInterface is null, it will
	 * get it from NetworkPlus instead.
	 *
	 * @return the current instance of ServerInterface
	 */
	private ServerInterface getServer()
	{
		if(server == null) server = NetworkPlus.getInstance().getServer();
		return server;
	}

	/**
	 * Logs a message to the server and the console.
	 *
	 * @param level the level to log the message at
	 * @param message the message to log
	 */
	public void log(LoggerLevel level, String message)
	{
		getServer().broadcast(message, level.getPermission().toString());
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
		INFO(ServerPermission.LOGGER_INFO),
		WARNING(ServerPermission.LOGGER_WARNING),
		VERBOSE(ServerPermission.LOGGER_VERBOSE);

		ServerPermission permission;

		private LoggerLevel(ServerPermission perm)
		{
			permission = perm;
		}

		public ServerPermission getPermission()
		{
			return permission;
		}
	}
}
