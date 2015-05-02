package com.octopod.switchcore.server.networked.stored;

import com.octopod.minecraft.MinecraftConsole;
import com.octopod.minecraft.MinecraftOfflinePlayer;
import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.minecraft.exceptions.PlayerOfflineException;
import com.octopod.switchcore.SwitchCore;
import com.octopod.switchcore.SwitchCoreVersion;
import com.octopod.switchcore.server.ServerProperty;
import com.octopod.switchcore.server.networked.NetworkedServer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class StoredServer implements NetworkedServer
{
	String server_id;
	SwitchCoreVersion version;
	Map<ServerProperty, Object> properties;

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
	public StoredServer(NetworkedServer server)
	{
		if(server != null)
		{
			this.server_id = 			server.getServerIdentifier();
			this.version = 				server.getSwitchVersion();
			this.properties = 			server.getPropertyMap();
		}
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
			SwitchCore.getInstance().getLogger().w("Tried to cast from " + object.getClass() + " to " + key.getType().getSimpleName());
			throw e;
		}
	}

	@Override
	public String getServerIdentifier()
	{
		return server_id;
	}

	@Override
	public SwitchCoreVersion getSwitchVersion()
	{
		return version;
	}

	@Override
	public String toString()
	{
		return "[server: " + getServerIdentifier() + ", properties: " + properties.size() + ", external: " + (version == null) + "]";
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
		return null;
	}

	@Override
	public MinecraftOfflinePlayer getOfflinePlayer(String s)
	{
		return null;
	}

	@Override
	public MinecraftOfflinePlayer getOfflinePlayerByName(String s)
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
	public MinecraftPlayer[] getOnlinePlayers()
	{
		return new MinecraftPlayer[0];
	}

	@Override
	public MinecraftOfflinePlayer[] getOfflinePlayers()
	{
		return new MinecraftOfflinePlayer[0];
	}

	@Override
	public boolean getWhitelistEnabled()
	{
		return false;
	}

	@Override
	public String[] getWhitelistedPlayers()
	{
		return new String[0];
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
