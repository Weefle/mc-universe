package com.hyperfresh.mcuniverse;

import com.octopod.minecraft.MinecraftPlayer;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface NetworkConnection
{
	public String getName();

	public String getServerIdentifier();

	public void disconnect();

	public void connect();

	public boolean isConnected();

	/**
	 * Attempts to send a message to a single server.
	 *
	 * @param server the server's identifier
	 * @param channel the channel
	 * @param message the message
	 */
	public void sendMessage(String server, String channel, String message);

	/**
	 * Attempts to send a message to all avaliable servers.
	 *
	 * @param channel the channel
	 * @param message the message
	 */
	public void broadcastMessage(String channel, String message);

	public void sendPlayer(MinecraftPlayer player, String server);
}
