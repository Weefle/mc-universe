package com.hyperfresh.mcuniverse.server.networked.local;

import com.hyperfresh.mcuniverse.UniverseAPI;
import com.hyperfresh.mcuniverse.exceptions.PlayerOfflineException;
import com.hyperfresh.mcuniverse.minecraft.MinecraftConsole;
import com.hyperfresh.mcuniverse.minecraft.MinecraftPlayer;
import com.hyperfresh.mcuniverse.minecraft.MinecraftServer;
import com.hyperfresh.mcuniverse.minecraft.MinecraftUser;
import com.hyperfresh.mcuniverse.server.ServerProperty;
import com.hyperfresh.mcuniverse.server.ServerPropertyManager;
import com.hyperfresh.mcuniverse.server.networked.UniverseServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class LocalServer implements UniverseServer
{
	MinecraftServer server;

	public LocalServer(MinecraftServer server)
	{
		this.server = server;
	}

	@Override
	public String getServerUsername()
	{
		return UniverseAPI.getInstance().getConnection().getServerIdentifier();
	}

	@Override
	public String getServerName()
	{
		return UniverseAPI.getInstance().getConfig().getString("server-name");
	}

	@Override
	public com.hyperfresh.mcuniverse.UniverseVersion getPluginVersion()
	{
		return com.hyperfresh.mcuniverse.UniverseVersion.LATEST;
	}

	@Override
	public Map<ServerProperty, Object> getPropertyMap()
	{
		Map<ServerProperty, Object> properties = new HashMap<>();
		ServerPropertyManager spm = UniverseAPI.getInstance().getPropertyManager();
		for(ServerProperty property: spm.getProperties())
		{
			properties.put(property, property.nextValue());
		}
		return properties;
	}

	@Override
	public void setProperty(ServerProperty key, Object value) {}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getProperty(ServerProperty<T> key)
	{
		return key.nextValue();
	}

	@Override
	public boolean isOnline()
	{
		return true;
	}

	@Override
	public String toString()
	{
		return "[server: " + getServerUsername() + "]";
	}

	@Override
	public void console(String s)
	{
		server.console(s);
	}

	@Override
	public void command(String s)
	{
		server.command(s);
	}

	@Override
	public void command(MinecraftPlayer player, String s)
	{
		server.command(player, s);
	}

	@Override
	public void message(String UUID, String message) throws PlayerOfflineException
	{
		server.message(UUID, message);
	}

	@Override
	public void broadcast(String s, String s1)
	{
		server.broadcast(s, s1);
	}

	@Override
	public void broadcast(String s)
	{
		server.broadcast(s);
	}

	@Override
	public MinecraftPlayer getPlayer(String s)
	{
		return server.getPlayer(s);
	}

	@Override
	public MinecraftPlayer getPlayerByName(String s)
	{
		return server.getPlayerByName(s);
	}

	@Override
	public MinecraftUser getOfflinePlayer(String s)
	{
		return server.getOfflinePlayer(s);
	}

	@Override
	public MinecraftUser getOfflinePlayerByName(String s)
	{
		return server.getOfflinePlayerByName(s);
	}

	@Override
	public MinecraftConsole getConsole()
	{
		return server.getConsole();
	}

	@Override
	public int getMaxPlayers()
	{
		return server.getMaxPlayers();
	}

	@Override
	public List<MinecraftPlayer> getOnlinePlayers()
	{
		return server.getOnlinePlayers();
	}

	@Override
	public List<MinecraftUser> getOfflinePlayers()
	{
		return server.getOfflinePlayers();
	}

	//TODO: implement the rest of these methods

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
