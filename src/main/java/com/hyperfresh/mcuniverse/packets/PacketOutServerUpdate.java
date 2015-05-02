package com.hyperfresh.mcuniverse.packets;

import com.hyperfresh.mcuniverse.UniverseAPI;
import com.hyperfresh.mcuniverse.server.ServerProperty;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketOutServerUpdate<T> extends Packet
{
	String class_name;
	T object;

	@SuppressWarnings("unchecked")
	public PacketOutServerUpdate(Class<? extends ServerProperty<T>> type)
	{
		this.class_name = type.getName();
		this.object = UniverseAPI.getInstance().getProperty(type).nextValue();
	}

	@SuppressWarnings("unchecked")
	public Class<? extends ServerProperty<?>> getPropertyClass()
	{
		try
		{
			return (Class<? extends ServerProperty<?>>)Class.forName(class_name);
		}
		catch (ClassNotFoundException e)
		{
			return null;
		}
	}

	public String getPropertyClassName()
	{
		return class_name;
	}

	public boolean propertyExists()
	{
		try
		{
			Class.forName(class_name);
			return true;
		}
		catch (ClassNotFoundException e)
		{
			return false;
		}
	}

	public T getValue() {return object;}
}
