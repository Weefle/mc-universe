package com.octopod.switchcore.packets;

import com.octopod.switchcore.SwitchCore;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketOutServerSwitchAll extends SwitchPacket
{
	String destination;

	public PacketOutServerSwitchAll(String destination)
	{
		this.destination = destination;
	}

	public PacketOutServerSwitchAll()
	{
		this.destination = SwitchCore.getServerIdentifier();
	}

	public String getDestination()
	{
		return destination;
	}

	@Override
	public String getChannelOut()
	{
		return null;
	}

	@Override
	public String[] getMessage()
	{
		return new String[0];
	}
}
