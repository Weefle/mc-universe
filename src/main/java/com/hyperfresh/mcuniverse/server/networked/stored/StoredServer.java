package com.hyperfresh.mcuniverse.server.networked.stored;

import com.hyperfresh.mcuniverse.UniverseVersion;
import com.hyperfresh.mcuniverse.exceptions.PlayerOfflineException;
import com.hyperfresh.mcuniverse.minecraft.MinecraftConsole;
import com.hyperfresh.mcuniverse.minecraft.MinecraftPlayer;
import com.hyperfresh.mcuniverse.minecraft.MinecraftUser;
import com.hyperfresh.mcuniverse.server.ServerProperty;
import com.hyperfresh.mcuniverse.server.networked.UniverseServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class StoredServer implements UniverseServer
{
	String server_id;
	UniverseVersion version;
	Map<ServerProperty, Object> properties;

	/**
	 * The display name of this server. If null, the username will be used instead.
	 */
	String serverName = null;

	boolean online = true;

	List<String> onlinePlayers;

	/**
	 * Constructor for external servers.
	 *
	 * @param serverID the server's identifier
	 */
	public StoredServer(String serverID)
	{
		this.server_id = serverID;
		this.version = null;
		this.properties = new HashMap<>();
	}

	/**
	 * Constructor for internal servers.
	 *
	 * @param server the recieved server
	 */
	public StoredServer(UniverseServer server)
	{
		if(server != null)
		{
			this.server_id = 	server.getServerUsername();
			this.version = 		server.getPluginVersion();
			this.properties = 	server.getPropertyMap();
		}
	}

	public void setServerName(String name)
	{
		this.serverName = name;
	}

	public void setOnlinePlayers(List<String> onlinePlayers)
	{
		this.onlinePlayers = onlinePlayers;
	}

	@Override
	public Map<ServerProperty, Object> getPropertyMap()
	{
		return new HashMap<>(properties);
	}

	@Override
	public <T> void setProperty(ServerProperty<T> key, T object) throws IllegalArgumentException
	{
		if(!key.getType().isInstance(object))
		{
			throw new IllegalArgumentException("Provided value does not match type " + key.getType().getName() + " (property: " + key.getName() + ")");
		}
		properties.put(key, object);
	}

	@Override
	public boolean isOnline()
	{
		return online;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getProperty(ServerProperty<T> key) throws ClassCastException
	{
		Object object = properties.get(key);
		try
		{
			return (T)object;
		}
		catch (ClassCastException e)
		{
			com.hyperfresh.mcuniverse.UniverseAPI.getInstance().getLogger().w("Tried to cast from " + object.getClass() + " to " + key.getType().getSimpleName());
			throw e;
		}
	}

	@Override
	public String getServerUsername()
	{
		return server_id;
	}

	@Override
	public String getServerName()
	{
		return serverName == null ? server_id : serverName;
	}

	@Override
	public UniverseVersion getPluginVersion()
	{
		return version;
	}

	@Override
	public String toString()
	{
		return "[server: " + getServerUsername() + ", properties: " + properties.size() + ", external: " + (version == null) + "]";
	}

	@Override
	public void console(String s)
	{

	}

	@Override
	public void command(String s)
	{

	}

	@Override
	public void command(MinecraftPlayer minecraftPlayer, String s)
	{

	}

	@Override
	public void message(String s, String s1) throws PlayerOfflineException
	{

	}

	@Override
	public void broadcast(String s, String s1)
	{

	}

	@Override
	public void broadcast(String s)
	{

	}

	@Override
	public MinecraftPlayer getPlayer(String s)
	{
		throw new UnsupportedOperationException("Unable to get MinecraftPlayers from this server");
	}

	@Override
	public MinecraftPlayer getPlayerByName(String s)
	{
		throw new UnsupportedOperationException("Unable to get MinecraftPlayers from this server");
	}

	@Override
	public MinecraftUser getOfflinePlayer(String s)
	{
		return null;
	}

	@Override
	public MinecraftUser getOfflinePlayerByName(String s)
	{
		return null;
	}

	@Override
	public MinecraftConsole getConsole()
	{
		return null;
	}

	@Override
	public int getMaxPlayers()
	{
		return 0;
	}

	@Override
	public List<MinecraftPlayer> getOnlinePlayers()
	{
		List<MinecraftPlayer> players = new ArrayList<>();
		onlinePlayers.stream().forEach((name) -> players.add(new StoredPlayer(this, name)));
		return players;
	}

	@Override
	public List<MinecraftUser> getOfflinePlayers()
	{
		return new ArrayList<>();
	}

	@Override
	public boolean getWhitelistEnabled()
	{
		return false;
	}

	@Override
	public List<String> getWhitelistedPlayers()
	{
		return new ArrayList<>();
	}

	@Override
	public boolean isWhitelisted(String s)
	{
		return false;
	}

	@Override
	public boolean isBanned(String s)
	{
		return false;
	}

	@Override
	public boolean isFull()
	{
		return false;
	}
}
