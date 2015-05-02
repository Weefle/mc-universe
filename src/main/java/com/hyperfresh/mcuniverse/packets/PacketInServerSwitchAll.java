package com.hyperfresh.mcuniverse.packets;

import com.hyperfresh.mcuniverse.UniverseAPI;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketInServerSwitchAll extends Packet
{
	String destination;

	public PacketInServerSwitchAll(String destination)
	{
		this.destination = destination;
	}

	public PacketInServerSwitchAll()
	{
		this.destination = UniverseAPI.getInstance().getServerIdentifier();
	}

	public String getDestination()
	{
		return destination;
	}
}
