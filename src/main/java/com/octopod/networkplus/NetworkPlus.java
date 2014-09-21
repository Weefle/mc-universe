package com.octopod.networkplus;

import com.octopod.networkplus.database.LocalFileDatabase;
import com.octopod.networkplus.database.ServerDatabase;
import com.octopod.networkplus.event.EventManager;
import com.octopod.networkplus.network.NetworkConnection;
import com.octopod.networkplus.serializer.GsonSerializer;
import com.octopod.networkplus.serializer.NetworkPlusSerializer;
import com.octopod.networkplus.server.ServerCommandManager;
import com.octopod.networkplus.server.ServerInterface;
import com.octopod.networkplus.server.ServerLogger;
import com.octopod.util.configuration.yaml.YamlConfiguration;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class NetworkPlus
{
	private NetworkPlus() {}

	private static NetworkPlus instance = null;

	public static NetworkPlus getInstance() {return instance;}

	public NetworkPlus(NetworkPlusPlugin plugin)
	{
		instance = this;
		this.plugin = plugin;
	}

	final String PREFIX = "&8[&6net+2&8]&f ";

	private ServerInterface server = null;
	private ServerLogger logger = new ServerLogger();
	private ServerCommandManager commandManager = new ServerCommandManager();

	private NetworkConnection connection = null;

	private EventManager eventManager = new EventManager();

	private NetworkPlusConfig config = null;
	private NetworkPlusPlugin plugin = null;

	/**
	 * The Serializer where messages will be encoded and decoded where possible
	 */
	private NetworkPlusSerializer serializer = new GsonSerializer(); //Use GSON for now

	/**
	 * The Database where server information will be stored
	 */
	private ServerDatabase database = new LocalFileDatabase();

	/**
	 * Returns the prefix that some messages will use
	 *
	 * @return this plugin's message prefix
	 */
	public String prefix() {return PREFIX;}

	/**
	 * Sets the current ServerInterface.
	 * Please use this method and setConfig() before using any getter methods.
	 *
	 * @param server the new ServerInterface
	 */
	public void setServer(ServerInterface server) {this.server = server;}

	/**
	 * Gets the current ServerInterface. (returns null if wasn't set before)
	 *
	 * @return the current ServerInterface
	 */
	public ServerInterface getServer() {return server;}

	public void setConfig(NetworkPlusConfig config) {this.config = config;}

	public YamlConfiguration getConfig() {return config.getConfig();}

	public NetworkPlusPlugin getPlugin() {return plugin;}

	/**
	 * Gets the current ServerLogger.
	 * Please use <code>setServer()</code> before using this method.
	 *
	 * @return the current ServerLogger
	 */
	public ServerLogger getLogger() {return logger;}

	/**
	 * Gets the current ServerCommandManager.
	 *
	 * @return the current ServerCommandManager
	 */
	public ServerCommandManager getCommandManager() {return commandManager;}

	/**
	 * Sets the current NetworkConnection.
	 *
	 * @param connection the new NetworkConnection
	 */
	public void setConnection(NetworkConnection connection) {this.connection = connection;}

	/**
	 * Gets the current NetworkConnection.
	 *
	 * @return the current NetworkConnection
	 */
	public NetworkConnection getConnection() {return connection;}

	/**
	 * Gets the current EventManager.
	 *
	 * @return the current EventManager
	 */
	public EventManager getEventManager() {return eventManager;}

	/**
	 * Gets the current ServerDatabase
	 *
	 * @return the current ServerDatabase
	 */
	public ServerDatabase getDatabase() {return database;}

	public NetworkPlusSerializer getSerializer() {return serializer;}

}
