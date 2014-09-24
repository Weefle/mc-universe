package com.octopod.networkplus.server;

import com.octopod.networkplus.PlayerOfflineException;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface ServerInterface
{
	public void console(String message);

	public void player(String ID, String message) throws PlayerOfflineException;

	public void broadcast(String message, String permission);

	public void broadcast(String message);

	public ServerPlayer getPlayer(String ID);

	public ServerConsole getConsole();

	public int getMaxPlayers();

	public List<ServerPlayer> getOnlinePlayers();

	public boolean getWhitelistEnabled();

	public List<String> getWhitelistedPlayers();
}
