package com.octopod.switchcore.server;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ServerPropertyManager
{
	Map<Class<? extends ServerProperty>, ServerProperty> properties = new WeakHashMap<>();

	@SuppressWarnings("unchecked")
	public void registerProperties(Class<?> c)
	{
		for(Class type: c.getDeclaredClasses())
		{
			if(ServerProperty.class.isAssignableFrom(type))
			{
				try
				{
					ServerProperty property = (ServerProperty)type.newInstance();
					properties.put((Class<? extends ServerProperty>)type, property);
				}
				catch (InstantiationException | IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> ServerProperty<T> getProperty(Class<? extends ServerProperty<T>> type) throws IllegalArgumentException
	{
		if(type.isInterface()) {throw new IllegalArgumentException("Cannot use interface " + type.getName() + " as key");}
		if(!properties.containsKey(type))
		{
			try
			{
				ServerProperty<T> property = type.newInstance();
				properties.put(type, property);
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		return properties.get(type);
	}

	public Collection<ServerProperty> getProperties()
	{
		return properties.values();
	}
}
