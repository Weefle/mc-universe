package com.octopod.networkplus.network;

import java.util.HashMap;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public enum NetworkChannel
{
	/**
	 *  Requests a server to add a player to their queue.
	 *  ARGS: Player, Server ID, Place (?)
	 */
	PLAYER_JOIN_QUEUE ("player.queue.join"),

	/**
	 *  Requests a server to clear a player from their queue.
	 *  ARGS: Player, Server ID, Place (?)
	 */
	PLAYER_LEAVE_QUEUE ("player.queue.leave"),

	/**
	 * Requests a server to add a player to their ServerFlags entry.
	 * ARGS: Player, Server
	 */
	PLAYER_JOIN_SERVER ("player.server.join"),

	/**
	 * Requests a server to clear a player to their ServerFlags entry.
	 * ARGS: Player, Server
	 */
	PLAYER_LEAVE_SERVER ("player.server.leave"),

	/**
	 *  To be used in combination with broadcastMessage()
	 *  Requests a server to send a player a message, if they are online.
	 *  ARGS: Player, Message
	 */
	PLAYER_MESSAGE ("player.message"),

	/**
	 *  Requests a server to broadcast a message to everyone.
	 *  ARGS: Message
	 */
	SERVER_ALERT ("server.alert"),

	/**
	 *  Requests a server to send all players on it to another server.
	 *  ARGS: Server ID
	 */
	SERVER_SENDALL ("server.sendall"),

	SERVER_PING_REQUEST("server.ping.request"),

	SERVER_PING_RETURN("server.ping.return"),

	SERVER_OFFLINE("server.offline"),

	SERVER_ONLINE("server.online"),

	/**
	 *  Requests a server to send back their current ServerFlags object (via SERVER_FLAGS_CACHE channel)
	 *  ARGS: (none)
	 */
	SERVER_INFO_REQUEST("server.info.request"),

	/**
	 *  Requests a server to cache a ServerFlags object (via a JSON object of a HashMap)
	 *  If the server is already cached, the HashMap will merge into the existing one instead.
	 *  ARGS: Server ID, HashMap (full or partial) (in JSON)
	 */
	SERVER_INFO_RETURN("server.info.return");

	private static HashMap<String, NetworkChannel> lookup = new HashMap<>();

	static{
		for(NetworkChannel channel: NetworkChannel.values())
			lookup.put(channel.toString(), channel);
	}

	public static NetworkChannel getByString(String channel)
	{
		return lookup.get(channel.toLowerCase());
	}

	private String channel;
	private NetworkChannel(String channel)
	{
		this.channel = channel;
	}

	public String toString() {return "network." + channel;}
}
