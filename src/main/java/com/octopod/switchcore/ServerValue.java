package com.octopod.switchcore;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public enum ServerValue
{
	SERVER_NAME(String.class),
	SERVER_VERSION(String.class),
	MAX_PLAYERS(Integer.class),
	WHITELIST_ENABLED(Boolean.class),
	WHITELIST_PLAYERS(String[].class),
	HUB_PRIORITY(Integer.class),
	ONLINE_PLAYERS(String[].class),
	QUEUED_PLAYERS(String[].class),
	LAST_ONLINE(Long.class);

	private Class<?> type;

	private ServerValue(Class<?> type)
	{
		this.type = type;
	}

	public Class<?> expectedType() {return type;}

	@Override
	public String toString() {return name().toLowerCase();}
}
