package com.octopod.networkplus;

import com.octopod.networkplus.exceptions.DeserializationException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class CachedServer extends HashMap<ServerValue, Object> implements Server
{
	private String name;

	public CachedServer(String servername)
	{
		this.name = servername;
	}

	public CachedServer(Map<ServerValue, Object> values)
	{
		if(values != null)
		{
			super.putAll(values);
		}
	}

	public static CachedServer decode(String servername, String encoded)
	{
		CachedServer server = new CachedServer(servername);
		try
		{
			server.putAll(NetworkPlus.getSerializer().deserializeMap(encoded, ServerValue.class, Object.class));
		}
		catch (DeserializationException e)
		{
			e.printStackTrace();
		}
		return server;
	}

	@Override
	public Object getValue(ServerValue type)
	{
		return type.expectedType().cast(super.get(type));
	}

	@Override
	public Object get(Object key)
	{
		if(!(key instanceof ServerValue)) return null;
		ServerValue type = (ServerValue)key;

		return type.expectedType().cast(super.get(key));
	}

	@Override
	public void setValue(ServerValue type, Object value)
	{
		Class<?> classType = type.expectedType();
		if(classType != value.getClass()) throw new ClassCastException(
			"ServerValue: Expecting " + classType.getSimpleName() +
				", but recieved " + value.getClass().getSimpleName() +
				" for listOf " + type.name()
		);
		super.put(type, classType.cast(value));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object put(ServerValue type, Object value)
	{
		setValue(type, value);
		return value;
	}

	@Override
	public String getServerIdentifier()
	{
		return name;
	}

	@SuppressWarnings("unchecked")
	public <T> T ifNull(Object object, T def)
	{
		if(object == null) return def;
		return (T)object;
	}

	@Override
	public boolean isOnline()
	{
		return getLastOnline() == -1;
	}

	@Override
	public String getServerName()
	{
		return ifNull(getValue(ServerValue.SERVER_NAME), getServerIdentifier());
	}

	@Override
	public String getServerVersion()
	{
		return ifNull(getValue(ServerValue.SERVER_VERSION), "UNKNOWN");
	}

	@Override
	public int getMaxPlayers()
	{
		return ifNull(getValue(ServerValue.MAX_PLAYERS), -1);
	}

	@Override
	public boolean getWhitelistEnabled()
	{
		return ifNull(getValue(ServerValue.WHITELIST_ENABLED), false);
	}

	@Override
	public String[] getWhitelistedPlayers()
	{
		return ifNull(getValue(ServerValue.WHITELIST_PLAYERS), new String[0]);
	}

	@Override
	public int getHubPriority()
	{
		return ifNull(getValue(ServerValue.HUB_PRIORITY), -1);
	}

	@Override
	public String[] getOnlinePlayers()
	{
		return ifNull(getValue(ServerValue.ONLINE_PLAYERS), new String[0]);
	}

	@Override
	public String[] getQueuedPlayers()
	{
		return ifNull(getValue(ServerValue.QUEUED_PLAYERS), new String[0]);
	}

	@Override
	public long getLastOnline()
	{
		return ifNull(getValue(ServerValue.LAST_ONLINE), -1);
	}
}
