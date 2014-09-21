package com.octopod.networkplus.server;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class CachedServerInfo implements ServerInfo
{
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

	public boolean containsValue(ServerValue infoType)
	{
		return values.containsKey(infoType);
	}
}
