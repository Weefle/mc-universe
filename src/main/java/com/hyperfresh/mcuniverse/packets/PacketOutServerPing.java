package com.hyperfresh.mcuniverse.packets;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketOutServerPing extends Packet
{
	int id;

	public PacketOutServerPing(int id)
	{
		this.id = id;
	}

	public PacketOutServerPing()
	{
		this.id = 0;
	}

	public int getPingID()
	{
		return id;
	}
}
