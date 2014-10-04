package com.octopod.networkplus;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.minecraft.MinecraftServerInterface;
import com.octopod.networkplus.compatable.CompatabilityLayer;
import com.octopod.networkplus.compatable.LilypadEssentialsCompatability;
import com.octopod.networkplus.database.LocalFileDatabase;
import com.octopod.networkplus.database.ServerDatabase;
import com.octopod.networkplus.event.EventManager;
import com.octopod.networkplus.event.events.NetworkMessageEvent;
import com.octopod.networkplus.lilypad.LilypadConnection;
import com.octopod.networkplus.messages.MessageOutPlayerSend;
import com.octopod.networkplus.messages.NetworkMessage;
import com.octopod.networkplus.serializer.GsonSerializer;
import com.octopod.networkplus.serializer.NetworkPlusSerializer;
import com.octopod.util.configuration.yaml.YamlConfiguration;
import com.octopod.util.minecraft.command.CommandManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
		NetworkPlus.serializer = new GsonSerializer();
		NetworkPlus.connection = new LilypadConnection();
		NetworkPlus.database = new LocalFileDatabase();

		registerCompatability(LilypadEssentialsCompatability.class);
	}

	public static void dinit()
	{
		unregisterCompatabilities();
	}

	final static String PREFIX = "&8[&6net+&8]&f ";

	private static MinecraftServerInterface server = null;

	private static Logger logger = null;

	private static CommandManager commandManager = new CommandManager();

	private static EventManager eventManager = new EventManager();

	private static NetworkConnection connection = null;

	private static NetworkPlusConfig config = null;

	private static NetworkPlusPlugin plugin = null;

	private static List<CompatabilityLayer> compatabilityLayers = new ArrayList<>();

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
	 * Gets the current MinecraftServerInterface. (returns null if wasn't set before)
	 *
	 * @return the current MinecraftServerInterface
	 */
	public static MinecraftServerInterface getInterface() {return server;}

	public static void reloadConfig() throws IOException {config.load();}

	public static YamlConfiguration getConfig() {return config.getConfig();}

	public static NetworkPlusPlugin getPlugin() {return plugin;}

	public static void registerCompatability(Class<? extends CompatabilityLayer> type)
	{
		try
		{
			CompatabilityLayer layer = type.newInstance();
			compatabilityLayers.add(layer);
			layer.onEnable();
			server.broadcast(PREFIX + "&7Currently running in compatability with &a" + layer.getName() + "&7!");
		}
		catch(InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	public static void unregisterCompatabilities()
	{
		for(CompatabilityLayer layer: compatabilityLayers)
		{
			layer.onDisable();
		}
		compatabilityLayers = new ArrayList<>();
	}

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
	public static ServerDatabase getServerDatabase() {return database;}

	/**
	 * Gets the current Serializer for messages.
	 *
	 * @return the current NetworkPlusSerializer
	 */
	public static NetworkPlusSerializer getSerializer() {return serializer;}

	public static String getServerIdentifier()
	{
		return connection.getServerIdentifier();
	}

	/**
	 * Gets this server.
	 *
	 * @return this server
	 */
	public static Server getServer()
	{
		return database.getServer(getServerIdentifier());
	}

	/**
	 * Gets a cached server.
	 *
	 * @param server the name of the server
	 * @return the server, or null if the name doesn't exist
	 */
	public static Server getServer(String server)
	{
		return database.getServer(server);
	}

	public static boolean serverExists(String server)
	{
		return database.getServerNames().contains(server);
	}

	public static String serialize(Object object)
	{
		return serializer.serialize(object);
	}

	public static <T> T deserialize(String encoded, Class<T> type)
	{
		return serializer.deserialize(encoded, type);
	}

	public PlayerSendResult redirectPlayer(final MinecraftPlayer player, final String server)
	{
		final AtomicReference<PlayerSendResult> result = new AtomicReference<>();
		final NetworkMessage message = new MessageOutPlayerSend(player);

		TempListenerFilter<NetworkMessageEvent> filter = new TempListenerFilter<NetworkMessageEvent>()
		{
			public boolean onEvent(TempListener<NetworkMessageEvent> listener, NetworkMessageEvent event)
			{
				if(event.getChannel().equals(message.getReturnChannel()))
				{
					if(player.getUUID().equals(event.getParsed()[0]))
					{
						result.set(PlayerSendResult.valueOf(event.getParsed()[1]));
					}
					return true;
				}
				return false;
			}
		};

		message.send(server);
		new TempListener<>(NetworkMessageEvent.class, filter).waitFor(500);

		return result.get();
	}
}
