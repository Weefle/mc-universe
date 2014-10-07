package com.octopod.switchcore.database;

import com.octopod.switchcore.Server;

import java.util.Collection;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface ServerDatabase
{
	/**
	 * Gets the Server of a server.
	 * If the server isn't made yet, a new one will be created.
	 *
	 * @param server the server's identifier
	 * @return the server's information
	 */
	public Server getServer(String server);

	/**
	 * Adds a server to the database.
	 * The server's identifier will be used to return this server in the future.
	 *
	 * @param server the server
	 */
	public void setServer(Server server);

	public boolean serverExists(String server);

	/**
	 * Removes the Server of a server.
	 *
	 * @param server the server's identifier
	 */
	public void removeServer(String server);

	/**
	 * Gets all the currently saved server identifiers.
	 *
	 * @return all the saved server identifiers
	 */
	public Collection<String> getServerNames();

	public Collection<Server> getServers();

	/**
	 * Saves this database.
	 */
	public void write();

	/**
	 * Reads the database.
	 */
	public void read();
}
