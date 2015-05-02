package com.octopod.switchcore.server.networked.local;

import com.octopod.minecraft.MinecraftConsole;
import com.octopod.minecraft.MinecraftOfflinePlayer;
import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.minecraft.MinecraftServer;
import com.octopod.minecraft.exceptions.PlayerOfflineException;
import com.octopod.switchcore.SwitchCore;
import com.octopod.switchcore.SwitchCoreVersion;
import com.octopod.switchcore.server.ServerProperty;
import com.octopod.switchcore.server.ServerPropertyManager;
import com.octopod.switchcore.server.networked.NetworkedServer;

import java.util.*;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class LocalServer implements NetworkedServer
{
	MinecraftServer server;

	public LocalServer(MinecraftServer server)
	{
		this.server = server;
	}

	@Override
	public String getServerIdentifier()
	{
		return SwitchCore.getInstance().getConnection().getServerIdentifier();
	}

	@Override
	public SwitchCoreVersion getSwitchVersion()
	{
		return SwitchCoreVersion.LATEST;
	}

	@Override
	public Map<ServerProperty, Object> getPropertyMap()
	{
		Map<ServerProperty, Object> properties = new HashMap<>();
		ServerPropertyManager spm = SwitchCore.getInstance().getPropertyManager();
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
	public String toString()
	{
		return "[server: " + getServerIdentifier() + "]";
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
	public MinecraftOfflinePlayer getOfflinePlayer(String s)
	{
		return server.getOfflinePlayer(s);
	}

	@Override
	public MinecraftOfflinePlayer getOfflinePlayerByName(String s)
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
