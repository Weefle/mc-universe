package com.octopod.switchcore;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.minecraft.MinecraftServerInterface;
import com.octopod.switchcore.database.LocalFileDatabase;
import com.octopod.switchcore.database.ServerDatabase;
import com.octopod.switchcore.event.EventManager;
import com.octopod.switchcore.event.events.NetworkMessageOutEvent;
import com.octopod.switchcore.event.events.NetworkPacketInEvent;
import com.octopod.switchcore.event.events.NetworkPacketOutEvent;
import com.octopod.switchcore.extensions.LilypadEssentialsCompatability;
import com.octopod.switchcore.extensions.SwitchCoreExtension;
import com.octopod.switchcore.lilypad.LilypadConnection;
import com.octopod.switchcore.packets.PacketInPlayerSwitch;
import com.octopod.switchcore.packets.PacketOutPlayerSwitch;
import com.octopod.switchcore.packets.SwitchPacket;
import com.octopod.switchcore.serializer.GsonSerializer;
import com.octopod.switchcore.serializer.SwitchCorePacketSerializer;
import com.octopod.util.configuration.yaml.YamlConfiguration;
import com.octopod.util.minecraft.command.CommandManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class SwitchCore
{
	private SwitchCore() {}

	public static void init(SwitchCorePlugin plugin)
	{
		SwitchCore.plugin = plugin;
		SwitchCore.server = plugin.getServerInterface();
		SwitchCore.logger = new Logger(server);
		SwitchCore.config = new SwitchCoreConfig(plugin, logger);
		SwitchCore.serializer = new GsonSerializer();
		SwitchCore.connection = new LilypadConnection();
		SwitchCore.database = new LocalFileDatabase();

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

	private static SwitchCoreConfig config = null;

	private static SwitchCorePlugin plugin = null;

	private static List<SwitchCoreExtension> extensions = new ArrayList<>();

	/**
	 * The Serializer where packets will be encoded and decoded where possible
	 */
	private static SwitchCorePacketSerializer serializer = null;

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

	public static SwitchCorePlugin getPlugin() {return plugin;}

	public static void registerExtension(Class<? extends SwitchCoreExtension> type)
	{
		try
		{
			SwitchCoreExtension extension = type.newInstance();
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
		for(SwitchCoreExtension layer: extensions)
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
	 * @return the current SwitchCorePacketSerializer
	 */
	public static SwitchCorePacketSerializer getSerializer() {return serializer;}

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
		if(database == null) return null;
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

	public static String serializePacket(SwitchPacket packet)
	{
		return serializer.serialize(packet);
	}

	public static SwitchPacket deserializePacket(String encoded)
	{
		return serializer.deserialize(encoded);
	}

	public static void sendMessage(String server, String channel, String message)
	{
		NetworkMessageOutEvent event = new NetworkMessageOutEvent(server, channel, message);
		SwitchCore.getEventManager().callEvent(event);
		if(!event.isCancelled())
		{
			connection.sendMessage(server, channel, message);
		}
	}

	public static void sendPacket(String server, SwitchPacket packet)
	{
		NetworkPacketOutEvent event = new NetworkPacketOutEvent(server, packet);
		SwitchCore.getEventManager().callEvent(event);
		if(!event.isCancelled())
		{
			sendMessage(server, StaticChannel.SWITCH_PACKET.toString(), SwitchCore.serializePacket(packet));
		}
	}

	public static void broadcastMessage(String channel, String message)
	{
		NetworkMessageOutEvent event = new NetworkMessageOutEvent(channel, message);
		SwitchCore.getEventManager().callEvent(event);
		if(!event.isCancelled())
		{
			connection.broadcastMessage(channel, message);
		}
	}

	public static void broadcastPacket(SwitchPacket packet)
	{
		NetworkPacketOutEvent event = new NetworkPacketOutEvent(packet);
		SwitchCore.getEventManager().callEvent(event);
		if(!event.isCancelled())
		{
			broadcastMessage(StaticChannel.SWITCH_PACKET.toString(), SwitchCore.serializePacket(packet));
		}
	}

	public PlayerSwitchResult redirectPlayer(final MinecraftPlayer player, final String server)
	{
		final AtomicReference<PlayerSwitchResult> result = new AtomicReference<>();
		final SwitchPacket message = new PacketOutPlayerSwitch(player);

		TempListenerFilter<NetworkPacketInEvent> filter = new TempListenerFilter<NetworkPacketInEvent>()
		{
			public boolean onEvent(TempListener<NetworkPacketInEvent> listener, NetworkPacketInEvent event)
			{
				if(event.getPacket() instanceof PacketInPlayerSwitch)
				{
					PacketInPlayerSwitch packet = (PacketInPlayerSwitch)event.getPacket();
					if(player.getUUID().equals(packet.getUUID()))
					{
						result.set(packet.getResult());
					}
					return true;
				}
				return false;
			}
		};

		message.send(server);
		new TempListener<>(NetworkPacketInEvent.class, filter).waitFor(500);

		return result.get();
	}
}
