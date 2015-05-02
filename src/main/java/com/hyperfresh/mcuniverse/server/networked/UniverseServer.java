package com.hyperfresh.mcuniverse.server.networked;

import com.hyperfresh.mcuniverse.UniverseVersion;
import com.hyperfresh.mcuniverse.minecraft.MinecraftServer;
import com.hyperfresh.mcuniverse.server.ServerProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface UniverseServer extends Serializable, MinecraftServer
{
	/**
	 * Gets the username of this server.
	 *
	 * @return
	 */
	public String getServerUsername();

	/**
	 * Gets the display name of this server.
	 * If there is no display name, the username will be used instead.
	 *
	 * @return
	 */
	public String getServerName();

	public UniverseVersion getPluginVersion();

	public Map<ServerProperty, Object> getPropertyMap();

	public <T> T getProperty(ServerProperty<T> key);

	/**
	 * Sets a property of a server.
	 * This method should fail if:
	 *   - The value does not match the expected type of the ServerProperty
	 *   - A duplicate instance of an existing ServerProperty is used
	 *
	 * @param key
	 * @param value
	 * @param <T>
	 */
	public <T> void setProperty(ServerProperty<T> key, T value) throws IllegalArgumentException;

	public boolean isOnline();
}
