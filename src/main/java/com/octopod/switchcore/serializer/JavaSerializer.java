package com.octopod.switchcore.serializer;

import com.octopod.switchcore.exceptions.DeserializationException;
import com.octopod.switchcore.exceptions.SerializationException;
import com.octopod.switchcore.packets.SwitchPacket;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class JavaSerializer implements SwitchPacketSerializer
{
	@Override
	public String getName() {return "Java";}

	@Override
	public String serialize(SwitchPacket packet) throws SerializationException
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
	public SwitchPacket deserialize(String encoded) throws DeserializationException
	{
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(encoded));
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object o = ois.readObject();
			ois.close();
			return SwitchPacket.class.cast(o);
		} catch (Exception e) {
			throw new DeserializationException("Unable to deserilize this string as SwitchPacket");
		}
	}
}
