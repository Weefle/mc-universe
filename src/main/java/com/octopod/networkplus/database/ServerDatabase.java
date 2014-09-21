package com.octopod.networkplus.database;

import com.octopod.networkplus.server.ServerInfo;

import java.util.Collection;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface ServerDatabase
{
	/**
	 * Gets the ServerInfo of a server.
	 * If the server isn't made yet, a new one will be created.
	 *
	 * @param serverID the server's identifier
	 * @return the server's information
	 */
	public ServerInfo getServerInfo(String serverID);

	/**
	 * Gets the ServerInfo of this server
	 *
	 * @return the server's information
	 */
	public ServerInfo getServerInfo();

	/**
	 * Removes the ServerInfo of a server.
	 *
	 * @param serverID the server's identifier
	 */
	public void removeServerInfo(String serverID);

	/**
	 * Gets all the currently saved server identifiers.
	 *
	 * @return all the saved server identifiers
	 */
	public Collection<String> getServerIDs();

	/**
	 * Saves this database.
	 */
	public void write();

	/**
	 * Reads the database.
	 */
	public void read();
}
