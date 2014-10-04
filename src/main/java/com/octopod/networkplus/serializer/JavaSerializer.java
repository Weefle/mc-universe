package com.octopod.networkplus.serializer;

import com.octopod.networkplus.exceptions.DeserializationException;
import com.octopod.networkplus.exceptions.SerializationException;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class JavaSerializer implements NetworkPlusSerializer
{
	@Override
	public String getName() {return "Java";}

	public String serialize(Object object) throws SerializationException
	{
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.close();
			return DatatypeConverter.printBase64Binary(byteArrayOutputStream.toByteArray());
		} catch (Exception e) {
			throw new SerializationException("Unable to serialize this object.");
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialize(String encoded, Class<T> type) throws DeserializationException
	{
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(encoded));
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object o = ois.readObject();
			ois.close();
			return (T)o;
		} catch (Exception e) {
			throw new DeserializationException("Unable to decode this object.");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> deserializeMap(String encoded, Class<K> key, Class<V> value) throws DeserializationException
	{
		return deserialize(encoded, (Class<Map<K, V>>)(Class<?>)Map.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> deserializeList(String encoded, Class<T> type) throws DeserializationException
	{
		return deserialize(encoded, (Class<List<T>>)(Class<?>)Map.class);
	}
}
