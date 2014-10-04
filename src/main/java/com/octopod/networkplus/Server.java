package com.octopod.networkplus;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface Server
{
	public void setValue(ServerValue type, Object object);

	public Object getValue(ServerValue type);

	public boolean isOnline();

	public String getServerIdentifier();

	public String getServerName();

	public String getServerVersion();

	public int getMaxPlayers();

	public boolean getWhitelistEnabled();

	public String[] getWhitelistedPlayers();

	public int getHubPriority();

	public String[] getOnlinePlayers();

	public String[] getQueuedPlayers();

	public long getLastOnline();
}
