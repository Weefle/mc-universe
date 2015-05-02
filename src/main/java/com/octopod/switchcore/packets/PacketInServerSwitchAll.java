package com.octopod.switchcore.packets;

import com.octopod.switchcore.SwitchCore;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketInServerSwitchAll extends SwitchPacket
{
	String destination;

	public PacketInServerSwitchAll(String destination)
	{
		this.destination = destination;
	}

	public PacketInServerSwitchAll()
	{
		this.destination = SwitchCore.getServerIdentifier();
	}

	public String getDestination()
	{
		return destination;
	}
}
