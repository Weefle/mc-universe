package com.octopod.switchcore.serializer;

import com.octopod.switchcore.exceptions.DeserializationException;
import com.octopod.switchcore.exceptions.SerializationException;
import com.octopod.switchcore.packets.SwitchPacket;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface SwitchCorePacketSerializer
{
	public String getName();

	public String serialize(SwitchPacket packet) throws SerializationException;

	public SwitchPacket deserialize(String encoded) throws DeserializationException;
}
