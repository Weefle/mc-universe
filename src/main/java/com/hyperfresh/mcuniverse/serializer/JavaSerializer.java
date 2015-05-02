package com.hyperfresh.mcuniverse.serializer;

import com.hyperfresh.mcuniverse.exceptions.DeserializationException;
import com.hyperfresh.mcuniverse.exceptions.SerializationException;
import com.hyperfresh.mcuniverse.packets.Packet;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class JavaSerializer implements PacketSerializer
{
	@Override
	public String getName() {return "Java";}

	@Override
	public String serialize(Packet packet) throws SerializationException
	{
		try
		{
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(packet);
			objectOutputStream.close();
			return DatatypeConverter.printBase64Binary(byteArrayOutputStream.toByteArray());
		}
		catch (Exception e)
		{
			SerializationException exception = new SerializationException(e.getClass().getName() + ": " + e.getMessage());
			exception.setStackTrace(e.getStackTrace());
			throw exception;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Packet deserialize(String encoded) throws DeserializationException
	{
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(encoded));
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object o = ois.readObject();
			ois.close();
			return Packet.class.cast(o);
		} catch (Exception e) {
			throw new DeserializationException("Unable to deserilize this string as SwitchPacket");
		}
	}
}
