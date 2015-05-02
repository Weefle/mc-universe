package com.octopod.switchcore.server.networked;

import com.octopod.minecraft.MinecraftServer;
import com.octopod.switchcore.SwitchCoreVersion;
import com.octopod.switchcore.server.ServerProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface NetworkedServer extends Serializable, MinecraftServer
{
	public abstract String getServerIdentifier();

	public abstract SwitchCoreVersion getSwitchVersion();

	public abstract Map<ServerProperty, Object> getPropertyMap();

	public abstract <T> T getProperty(ServerProperty<T> key);

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
	public abstract <T> void setProperty(ServerProperty<T> key, T value) throws IllegalArgumentException;
}
