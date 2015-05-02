package com.octopod.switchcore.packets;

import com.octopod.switchcore.ServerProperty;
import com.octopod.switchcore.SwitchCore;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PacketOutServerUpdate<T> extends SwitchPacket
{
	String class_name;
	Object object;

	@SuppressWarnings("unchecked")
	public PacketOutServerUpdate(Class<? extends ServerProperty<?>> type)
	{
		this.class_name = type.getName();
		this.object = SwitchCore.getProperty(type).get();
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

	public Object getValue() {return object;}
}
