package com.octopod.switchcore.packets;

/**
 * @author Octopod - octopodsquad@gmail.com
 */

/**
 * Tells the server to broadcast a message to all players.
 */
public class PacketInServerBroadcast extends SwitchPacket
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
