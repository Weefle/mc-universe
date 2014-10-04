package com.octopod.networkplus.serializer;

import com.octopod.networkplus.exceptions.DeserializationException;
import com.octopod.networkplus.exceptions.SerializationException;

import java.util.List;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface NetworkPlusSerializer
{
	public String getName();

	public String serialize(Object object) throws SerializationException;

	public <T> T deserialize(String encoded, Class<T> type) throws DeserializationException;

	public <K, V> Map<K, V> deserializeMap(String encoded, Class<K> key, Class<V> value) throws DeserializationException;

	public <T> List<T> deserializeList(String encoded, Class<T> type) throws DeserializationException;
}
