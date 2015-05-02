package com.hyperfresh.mcuniverse;

import com.hyperfresh.mcuniverse.database.LocalFileDatabase;
import com.hyperfresh.mcuniverse.database.ServerDatabase;
import com.hyperfresh.mcuniverse.event.AsyncEventHandler;
import com.hyperfresh.mcuniverse.event.EventBus;
import com.hyperfresh.mcuniverse.event.events.NetworkMessageOutEvent;
import com.hyperfresh.mcuniverse.event.events.NetworkPacketInEvent;
import com.hyperfresh.mcuniverse.event.events.NetworkPacketOutEvent;
import com.hyperfresh.mcuniverse.extensions.LilypadEssentialsCompatability;
import com.hyperfresh.mcuniverse.extensions.SwitchCoreExtension;
import com.hyperfresh.mcuniverse.lilypad.LilypadConnection;
import com.hyperfresh.mcuniverse.minecraft.MinecraftPlayer;
import com.hyperfresh.mcuniverse.minecraft.MinecraftServer;
import com.hyperfresh.mcuniverse.packets.*;
import com.hyperfresh.mcuniverse.serializer.GsonSerializer;
import com.hyperfresh.mcuniverse.serializer.PacketSerializer;
import com.hyperfresh.mcuniverse.server.ServerProperty;
import com.hyperfresh.mcuniverse.server.ServerPropertyManager;
import com.hyperfresh.mcuniverse.server.networked.UniverseServer;
import com.octopod.util.configuration.yaml.YamlConfiguration;
import com.octopod.util.minecraft.command.CommandManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class UniverseAPI
{
	private static com.hyperfresh.mcuniverse.UniverseAPI instance = new com.hyperfresh.mcuniverse.UniverseAPI();

	private static boolean initialized = false;

	public static com.hyperfresh.mcuniverse.UniverseAPI getInstance()
	{
		if(initialized) return instance;
		throw new IllegalStateException("SyncPod isn't initialized yet; initialize it first with SyncPod.getInstance().init()!");
	}
	
	private UniverseAPI() {}

	public static void init(UniversePlugin plugin)
	{
		instance.plugin = plugin;
		instance.server = plugin.getServerInterface();
		instance.logger = new Logger(instance.server);

		instance.logger.i("&8[ &esyncpod " + UniverseVersion.LATEST + " &8] &7is loading!");

		instance.config = new UniverseConfig(plugin, instance.logger);
		instance.serializer = new GsonSerializer();
		instance.connection = new LilypadConnection();
		instance.database = new LocalFileDatabase();
		instance.serverPropertyManager = new ServerPropertyManager();

		//instance.registerProperties(StaticProperties.class);
		instance.registerExtension(LilypadEssentialsCompatability.class);

		UniverseAPI.initialized = true;
	}

	public static void dinit()
	{
		instance.unregisterExtensions();

		UniverseAPI.initialized = false;
	}

	final public String PREFIX = "&8[&esyncpod&8]&f ";

	private MinecraftServer server = null;

	private Logger logger = null;

	private CommandManager commandManager = new CommandManager();

	private EventBus eventBus = new EventBus();

	private NetworkConnection connection = null;

	private UniverseConfig config = null;

	private UniversePlugin plugin = null;

	private List<SwitchCoreExtension> extensions = new ArrayList<>();

	/**
	 * The Serializer where packets will be encoded and decoded where possible
	 */
	private PacketSerializer serializer = null;

	/**
	 * The Database where server information will be stored
	 */
	private ServerDatabase database = null;

	private ServerPropertyManager serverPropertyManager = null;

	/**
	 * Gets the current MinecraftServer. (returns null if wasn't set before)
	 *
	 * @return the current MinecraftServer
	 */
	public MinecraftServer getInterface() {return server;}

	public void reloadConfig() throws IOException {config.load();}

	public YamlConfiguration getConfig() {return config.getConfig();}

	public UniversePlugin getPlugin() {return plugin;}

	public void registerExtension(Class<? extends SwitchCoreExtension> type)
	{
		try
		{
			SwitchCoreExtension extension = type.newInstance();
			extensions.add(extension);
			extension.onEnable();
			logger.i("&7Loaded Extension &a" + extension.getName() + "&7!");
		}
		catch(InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	public void unregisterExtensions()
	{
		for(SwitchCoreExtension layer: extensions)
		{
			layer.onDisable();
		}
		extensions = new ArrayList<>();
	}

	public void registerProperties(Class<?> c)
	{
		serverPropertyManager.registerProperties(c);
	}

	public <T> ServerProperty<T> getProperty(Class<? extends ServerProperty<T>> type)
	{
		return serverPropertyManager.getProperty(type);
	}

	public ServerPropertyManager getPropertyManager()
	{
		return serverPropertyManager;
	}

	/**
	 * Gets the current Logger.
	 * Please use <code>addServer()</code> before using this method.
	 *
	 * @return the current Logger
	 */
	public Logger getLogger() {return logger;}

	/**
	 * Gets the current CommandManager.
	 *
	 * @return the current CommandManager
	 */
	public CommandManager getCommandManager() {return commandManager;}

	/**
	 * Gets the current NetworkConnection.
	 *
	 * @return the current NetworkConnection
	 */
	public NetworkConnection getConnection() {return connection;}

	/**
	 * Gets the current EventBus.
	 *
	 * @return the current EventBus.
	 */
	public EventBus getEventBus() {return eventBus;}

	/**
	 * Gets the current ServerDatabase
	 *
	 * @return the current ServerDatabase
	 */
	public ServerDatabase getServerDatabase() {return database;}

	/**
	 * Gets the current Serializer for packets.
	 *
	 * @return the current SwitchCorePacketSerializer
	 */
	public PacketSerializer getSerializer() {return serializer;}

	public String getServerIdentifier()
	{
		return connection.getServerIdentifier();
	}

	/**
	 * Gets this server.
	 *
	 * @return this server
	 */
	public UniverseServer getServer()
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
	public UniverseServer getServer(String server)
	{
		return database.getServer(server);
	}

	public boolean serverExists(String server)
	{
		return database.getServerNames().contains(server);
	}

	public String serializePacket(Packet packet)
	{
		return serializer.serialize(packet);
	}

	public Packet deserializePacket(String encoded)
	{
		return serializer.deserialize(encoded);
	}

	public void sendMessage(String server, String channel, String message)
	{
		NetworkMessageOutEvent event = new NetworkMessageOutEvent(server, channel, message);
		getEventBus().post(event);
		if(!event.isCancelled())
		{
			connection.sendMessage(server, channel, message);
		}
	}

	public void sendPacket(String server, Packet packet)
	{
		NetworkPacketOutEvent event = new NetworkPacketOutEvent(server, packet);
		getEventBus().post(event);
		if(!event.isCancelled())
		{
			sendMessage(server, "switchcore.packet", serializePacket(packet));
		}
	}

	public void broadcastMessage(String channel, String message)
	{
		NetworkMessageOutEvent event = new NetworkMessageOutEvent(channel, message);
		getEventBus().post(event);
		if(!event.isCancelled())
		{
			connection.broadcastMessage(channel, message);
		}
	}

	public void broadcastPacket(Packet packet)
	{
		NetworkPacketOutEvent event = new NetworkPacketOutEvent(packet);
		getEventBus().post(event);
		if(!event.isCancelled())
		{
			broadcastMessage("switchcore.packet", serializePacket(packet));
		}
	}

	public PlayerSwitchResult redirectPlayer(final MinecraftPlayer player, final String server)
	{
		final AtomicReference<PlayerSwitchResult> result = new AtomicReference<>();
		final Packet message = new PacketInPlayerSwitch(player);

		AsyncEventHandler<NetworkPacketInEvent> listener = new AsyncEventHandler<NetworkPacketInEvent>()
		{
			public boolean handle(NetworkPacketInEvent event)
			{
				if(event.getPacket() instanceof PacketOutPlayerSwitch)
				{
					PacketOutPlayerSwitch packet = (PacketOutPlayerSwitch)event.getPacket();
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
		listener.waitFor(500);

		return result.get();
	}

	public Long pingServer(String serverID)
	{
		return pingServer(new PacketInServerPing(), serverID);
	}

	public Long pingServer(int id, String serverID)
	{
		return pingServer(new PacketInServerPing(id), serverID);
	}

	/**
	 * Pings a server, and returns the amount of time (ms) it took for
	 * the server to return the ping. If the ping timed out, it will
	 * return null.
	 *
	 * @param packet the ping packet
	 * @param serverID the server's identifier
	 * @return the time it took to complete the ping, or null if it timed out.
	 */
	private Long pingServer(PacketInServerPing packet, String serverID)
	{
		long time = System.currentTimeMillis();
		final int id = packet.getPingID();

		packet.send(serverID);
		return new AsyncEventHandler<NetworkPacketInEvent>()
		{
			@Override
			public boolean handle(NetworkPacketInEvent event)
			{
				Packet packet = event.getPacket();
				return (packet instanceof PacketOutServerPing && ((PacketOutServerPing)packet).getPingID() == id);
			}
		}.waitFor(500) ? System.currentTimeMillis() - time : null;
	}
}
