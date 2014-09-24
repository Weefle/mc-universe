package com.octopod.networkplus;

import com.octopod.networkplus.database.LocalFileDatabase;
import com.octopod.networkplus.database.ServerDatabase;
import com.octopod.networkplus.event.EventManager;
import com.octopod.networkplus.network.NetworkConnection;
import com.octopod.networkplus.network.lilypad.LilypadConnection;
import com.octopod.networkplus.serializer.GsonSerializer;
import com.octopod.networkplus.serializer.NetworkPlusSerializer;
import com.octopod.networkplus.server.ServerInterface;
import com.octopod.util.configuration.yaml.YamlConfiguration;

import java.io.IOException;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class NetworkPlus
{
	private NetworkPlus() {}

	public static void init(NetworkPlusPlugin plugin)
	{
		NetworkPlus.plugin = plugin;
		NetworkPlus.server = plugin.getServerInterface();
		NetworkPlus.logger = new Logger(server);
		NetworkPlus.config = new NetworkPlusConfig(plugin, logger);
		NetworkPlus.serializer = new GsonSerializer(); //Use GSON for now
		NetworkPlus.connection = new LilypadConnection();
		NetworkPlus.database = new LocalFileDatabase();
	}

	final static String PREFIX = "&8[&6net+&8]&f ";

	private static ServerInterface server = null;

	private static Logger logger = null;

	private static CommandManager commandManager = new CommandManager();

	private static EventManager eventManager = new EventManager();

	private static NetworkConnection connection = null;

	private static NetworkPlusConfig config = null;

	private static NetworkPlusPlugin plugin = null;

	/**
	 * The Serializer where messages will be encoded and decoded where possible
	 */
	private static NetworkPlusSerializer serializer = null;

	/**
	 * The Database where server information will be stored
	 */
	private static ServerDatabase database = null;

	/**
	 * Returns the prefix that some messages will use
	 *
	 * @return this plugin's message prefix
	 */
	public static String prefix() {return PREFIX;}

	/**
	 * Gets the current ServerInterface. (returns null if wasn't set before)
	 *
	 * @return the current ServerInterface
	 */
	public static ServerInterface getServer() {return server;}

	public static void reloadConfig() throws IOException {config.load();}

	public static YamlConfiguration getConfig() {return config.getConfig();}

	public static NetworkPlusPlugin getPlugin() {return plugin;}

	/**
	 * Gets the current Logger.
	 * Please use <code>setServer()</code> before using this method.
	 *
	 * @return the current Logger
	 */
	public static Logger getLogger() {return logger;}

	/**
	 * Gets the current CommandManager.
	 *
	 * @return the current CommandManager
	 */
	public static CommandManager getCommandManager() {return commandManager;}

	/**
	 * Gets the current NetworkConnection.
	 *
	 * @return the current NetworkConnection
	 */
	public static NetworkConnection getConnection() {return connection;}

	/**
	 * Gets the current EventManager.
	 *
	 * @return the current EventManager
	 */
	public static EventManager getEventManager() {return eventManager;}

	/**
	 * Gets the current ServerDatabase
	 *
	 * @return the current ServerDatabase
	 */
	public static ServerDatabase getDatabase() {return database;}

	public static NetworkPlusSerializer getSerializer() {return serializer;}

	public static String getServerIdentifier()
	{
		return connection.getServerIdentifier();
	}

	public static ServerInfo getServerInfo()
	{
		return database.getServerInfo(getServerIdentifier());
	}

	public static ServerInfo getServerInfo(String server)
	{
		return database.getServerInfo(server);
	}

}
