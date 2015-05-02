package com.octopod.switchcore.packets;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketInServerPing extends SwitchPacket
{
	/**
	 * The id of the ping, to tell apart different pings in the case of multiple pinging.
	 */
	int id;

	public PacketInServerPing(int id)
	{
		this.id = id;
	}

	public PacketInServerPing()
	{
		this.id = 0;
	}

	public int getPingID()
	{
		return id;
	}
}
