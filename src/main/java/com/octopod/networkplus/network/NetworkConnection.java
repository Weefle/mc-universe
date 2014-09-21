package com.octopod.networkplus.network;

import com.octopod.networkplus.server.ServerPlayer;

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
	 * If that server sends back a message on a return channel, <code>callback.ret()</code> will run.
	 * If the request times out (config-defined), then <code>callback.ret()</code> will still run,
	 * but with a failure code.
	 * @param serverID the destination server's identifier
	 * @param request the request to send
	 * @param callback the return interface
	 */
	public void sendNetworkRequest(String serverID, NetworkMessage request, NetworkReturn callback);

	/**
	 * Attempts to send a NetworkMessage to a single server.
	 *
	 * @param serverID the destination server's identifier
	 * @param request the request to send
	 */
	public void sendNetworkRequest(String serverID, NetworkMessage request);

	/**
	 * Attempts to send a NetworkMessage to all avaliable servers.
	 *
	 * @param request the request to send
	 */
	public void broadcastNetworkRequest(NetworkMessage request);

	public void redirectPlayer(ServerPlayer player, String server);
}
