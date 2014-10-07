package com.octopod.switchcore;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class CachedServer implements Server
{
	private String name = null;
	private boolean external = true;
	private Map<ServerValue, Object> values = null;

	public CachedServer(String servername)
	{
		this.name = servername;
		this.external = false;
		this.values = new HashMap<>();
	}

	public CachedServer(String servername, boolean external)
	{
		this.name = servername;
		this.external = external;
		this.values = new HashMap<>();
	}

	public CachedServer(String servername, Map<ServerValue, Object> values)
	{
		this.name = servername;
		this.external = false;
		this.values = values;
	}

	public CachedServer(Server server)
	{
		if(server != null)
		{
			this.name = server.getServerIdentifier();
			this.external = server.isExternal();
			this.values = server.toValueMap();
		}
	}

	@Override
	public Object getValue(ServerValue type)
	{
		return type.expectedType().cast(values.get(type));
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
		values.put(type, classType.cast(value));
	}

	@Override
	public Map<ServerValue, Object> toValueMap()
	{
		return new HashMap<>(values);
	}

	@Override
	public int totalValues()
	{
		return values.size();
	}

	@Override
	public boolean isExternal()
	{
		return external;
	}

	@Override
	public String getServerIdentifier()
	{
		return name;
	}

	@SuppressWarnings("unchecked")
	private <T> T ifNull(Object object, T def)
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

	@Override
	public String toString()
	{
		return "[server: " + getServerIdentifier() + ", values: " + totalValues() + ", external: " + isExternal() + "]";
	}
}
