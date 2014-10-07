package com.octopod.networkplus;

import java.util.HashMap;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public enum StaticChannel
{
	/**
	 *  Requests a server to add a player to their queue.
	 *  ARGS: Player, Server ID, Place (?)
	 */
	OUT_PLAYER_JOIN_QUEUE("player.queue.join"),

	/**
	 *  Requests a server to clear a player from their queue.
	 *  ARGS: Player, Server ID, Place (?)
	 */
	OUT_PLAYER_LEAVE_QUEUE("player.queue.leave"),

	/**
	 * Requests a server to add a player to their ServerFlags entry.
	 * ARGS: Player, Server
	 */
	OUT_PLAYER_JOIN_SERVER("player.server.join"),

	/**
	 * Requests a server to clear a player to their ServerFlags entry.
	 * ARGS: Player, Server
	 */
	OUT_PLAYER_LEAVE_SERVER("player.server.leave"),

	/**
	 *  To be used in combination with broadcastMessage()
	 *  Requests a server to send a player a message, if they are online.
	 *  ARGS: Player, Message
	 */
	OUT_PLAYER_MESSAGE("player.message"),

	/**
	 *  Requests a server to broadcast a message to everyone.
	 *  ARGS: Message
	 */
	OUT_SERVER_BROADCAST("server.alert"),

	/**
	 *  Requests a server to send all players on it to another server.
	 *  ARGS: Server ID
	 */
	OUT_SERVER_SENDALL("server.sendall"),

	OUT_SERVER_PING("server.ping.out"),

	IN_SERVER_PING("server.ping.in"),

	/**
	 *  Requests a server to send back their current ServerFlags object (via SERVER_FLAGS_CACHE channel)
	 *  ARGS: (none)
	 */
	OUT_SERVER_DISCOVER("server.discover.out"),

	/**
	 *  Requests a server to cache a ServerFlags object (via a JSON object of a HashMap)
	 *  If the server is already cached, the HashMap will merge into the existing one instead.
	 *  ARGS: Server ID, HashMap (full or partial) (in JSON)
	 */
	IN_SERVER_DISCOVER("server.discover.in"),

	OUT_SERVER_VALUE_REQUEST("server.value.request"),

	IN_SERVER_VALUE_REQUEST("server.value.return"),

	OUT_PLAYER_SEND_REQUEST("player.send.out"),

	IN_PLAYER_SEND_REQUEST("player.send.in"),

	OUT_SERVER_DISPATCH("server.dispatch");

	private static HashMap<String, StaticChannel> lookup = new HashMap<>();

	static{
		for(StaticChannel channel: StaticChannel.values())
			lookup.put(channel.toString(), channel);
	}

	public static StaticChannel getByString(String channel)
	{
		return lookup.get(channel.toLowerCase());
	}

	private String channel;
	private StaticChannel(String channel)
	{
		this.channel = channel;
	}

	public String toString() {return "network." + channel;}
}
