package com.octopod.networkplus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class CachedServerInfo implements ServerInfo
{
	public CachedServerInfo() {}

	public CachedServerInfo(String encoded)
	{
		try
		{
			values = NetworkPlus.getSerializer().decode(encoded, mapOf(ServerValue.class, Object.class));
		}
		catch (NetworkDecodeException e)
		{
			values = new HashMap<>();
		}
	}

	public CachedServerInfo(Map<ServerValue, Object> values)
	{
		this.values = values == null ? new HashMap<ServerValue, Object>() : values;
	}

	@SuppressWarnings("unchecked")
	private static <K, V> Class<Map<K, V>> mapOf(Class<K> key, Class<V> value)
	{
		return (Class<Map<K, V>>)(Class<?>)Map.class;
	}

	private Map<ServerValue, Object> values = new HashMap<>();

	@SuppressWarnings("unchecked")
	public void setValue(ServerValue infoType, Object value)
	{
		Class<?> type = infoType.expectedType();
		if(type != value.getClass()) throw new ClassCastException(
			"ServerValue: Expecting " + type.getSimpleName() +
			", but recieved " + value.getClass().getSimpleName() +
			" for listOf " + infoType.name()
		);
		values.put(infoType, type.cast(value));
	}

	public Object getValue(ServerValue infoType)
	{
		return infoType.expectedType().cast(values.get(infoType));
	}

	public Map<ServerValue, Object> getValues()
	{
		return values;
	}

	public boolean containsValue(ServerValue infoType)
	{
		return values.containsKey(infoType);
	}

	public String encode()
	{
		return NetworkPlus.getSerializer().encode(values);
	}
}
