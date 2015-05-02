package com.hyperfresh.mcuniverse.minecraft;

import com.hyperfresh.mcuniverse.exceptions.PlayerOfflineException;

import java.lang.String;
import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface MinecraftServer
{
	public void console(String message);

	public void command(String command);

	public void command(MinecraftPlayer player, String command);

	public void message(String UUID, String message) throws PlayerOfflineException;

	public void broadcast(String message, String permission);

	public void broadcast(String message);

	public MinecraftPlayer getPlayer(String UUID);

	public MinecraftPlayer getPlayerByName(String name);

	public MinecraftUser getOfflinePlayer(String UUID);

	public MinecraftUser getOfflinePlayerByName(String name);

	public MinecraftConsole getConsole();

	public int getMaxPlayers();

	public List<MinecraftPlayer> getOnlinePlayers();

	public List<MinecraftUser> getOfflinePlayers();

	public boolean getWhitelistEnabled();

	public List<String> getWhitelistedPlayers();

	public boolean isWhitelisted(String UUID);

	public boolean isBanned(String UUID);

	public boolean isFull();
}
