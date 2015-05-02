package com.hyperfresh.mcuniverse.packets;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Tells the server to broadcast a message to all players.
 */
public class PacketInServerBroadcast extends Packet
{
	String message;

	public PacketInServerBroadcast(String message)
	{
		this.message = message;
	}

	public String getBroadcastMessage()
	{
		return message;
	}
}
