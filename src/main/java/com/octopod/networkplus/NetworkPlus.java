package com.octopod.networkplus;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.minecraft.MinecraftServerInterface;
import com.octopod.networkplus.database.LocalFileDatabase;
import com.octopod.networkplus.database.ServerDatabase;
import com.octopod.networkplus.event.EventManager;
import com.octopod.networkplus.event.events.NetworkMessageInEvent;
import com.octopod.networkplus.event.events.NetworkMessageOutEvent;
import com.octopod.networkplus.event.events.NetworkPacketOutEvent;
import com.octopod.networkplus.extensions.LilypadEssentialsCompatability;
import com.octopod.networkplus.extensions.NetworkExtension;
import com.octopod.networkplus.lilypad.LilypadConnection;
import com.octopod.networkplus.packets.NetworkPacket;
import com.octopod.networkplus.packets.PacketOutPlayerSend;
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

		registerExtension(LilypadEssentialsCompatability.class);
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

	private static List<NetworkExtension> extensions = new ArrayList<>();

	/**
	 * The Serializer where packets will be encoded and decoded where possible
	 */
	private static NetworkPlusSerializer serializer = null;

	/**
	 * The Database where server information will be stored
	 */
	private static ServerDatabase database = null;

	/**
	 * Returns the prefix that some packets will use
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

	public static void registerExtension(Class<? extends NetworkExtension> type)
	{
		try
		{
			NetworkExtension extension = type.newInstance();
			extensions.add(extension);
			extension.onEnable();
			server.broadcast(PREFIX + "&7Loaded Extension &a" + extension.getName() + "&7!");
		}
		catch(InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	public static void unregisterCompatabilities()
	{
		for(NetworkExtension layer: extensions)
		{
			layer.onDisable();
		}
		extensions = new ArrayList<>();
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
	 * Gets the current Serializer for packets.
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

	public static void sendMessage(String server, String channel, String message)
	{
		NetworkMessageOutEvent event = new NetworkMessageOutEvent(server, channel, message);
		NetworkPlus.getEventManager().callEvent(event);
		if(!event.isCancelled())
		{
			connection.sendMessage(server, channel, message);
		}
	}

	public static void sendPacket(String server, NetworkPacket message)
	{
		NetworkPacketOutEvent event = new NetworkPacketOutEvent(server, message);
		NetworkPlus.getEventManager().callEvent(event);
		if(!event.isCancelled())
		{
			sendMessage(server, message.getChannelOut(), NetworkPlus.serialize(message.getMessage()));
		}
	}

	public static void broadcastMessage(String channel, String message)
	{
		NetworkMessageOutEvent event = new NetworkMessageOutEvent(channel, message);
		NetworkPlus.getEventManager().callEvent(event);
		if(!event.isCancelled())
		{
			connection.broadcastMessage(channel, message);
		}

	}

	public static void broadcastPacket(NetworkPacket message)
	{
		NetworkPacketOutEvent event = new NetworkPacketOutEvent(message);
		NetworkPlus.getEventManager().callEvent(event);
		if(!event.isCancelled())
		{
			broadcastMessage(message.getChannelOut(), NetworkPlus.serialize(message.getMessage()));
		}
	}

	public PlayerSendResult redirectPlayer(final MinecraftPlayer player, final String server)
	{
		final AtomicReference<PlayerSendResult> result = new AtomicReference<>();
		final NetworkPacket message = new PacketOutPlayerSend(player);

		TempListenerFilter<NetworkMessageInEvent> filter = new TempListenerFilter<NetworkMessageInEvent>()
		{
			public boolean onEvent(TempListener<NetworkMessageInEvent> listener, NetworkMessageInEvent event)
			{
				if(event.getChannel().equals(message.getChannelIn()))
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
		new TempListener<>(NetworkMessageInEvent.class, filter).waitFor(500);

		return result.get();
	}
}
