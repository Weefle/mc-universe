package com.hyperfresh.mcuniverse.serializer;

import com.hyperfresh.mcuniverse.exceptions.DeserializationException;
import com.hyperfresh.mcuniverse.exceptions.SerializationException;
import com.hyperfresh.mcuniverse.packets.Packet;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface PacketSerializer
{
	public String getName();

	public String serialize(Packet packet) throws SerializationException;

	public Packet deserialize(String encoded) throws DeserializationException;
}
