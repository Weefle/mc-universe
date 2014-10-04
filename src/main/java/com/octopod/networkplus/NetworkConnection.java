package com.octopod.networkplus;

import com.octopod.minecraft.MinecraftPlayer;
import com.octopod.networkplus.messages.NetworkMessage;

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
	 * Attempts to send a NetworkMessage to a single server.
	 *
	 * @param server the server's identifier
	 * @param message the message
	 */
	public void sendNetworkMessage(String server, NetworkMessage message);

	/**
	 * Attempts to send a message to a single server.
	 *
	 * @param server the server's identifier
	 * @param channel the channel
	 * @param message the message
	 */
	public void sendNetworkMessage(String server, String channel, String message);

	/**
	 * Attempts to send a message to all avaliable servers.
	 *
	 * @param channel the channel
	 * @param message the message
	 */
	public void broadcastNetworkMessage(String channel, String message);

	/**
	 * Attempts to send a NetworkMessage to all avaliable servers.
	 *
	 * @param message the message
	 */
	public void broadcastNetworkMessage(NetworkMessage message);

	public void redirectPlayer(MinecraftPlayer player, String server);
}
