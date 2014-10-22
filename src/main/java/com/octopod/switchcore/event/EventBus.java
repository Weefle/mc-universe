package com.octopod.switchcore.event;

import com.octopod.util.event.v2.EventHandler;
import com.octopod.util.event.v2.EventPriority;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventBus
{
	private final Map<Class<?>, Object> instances = new HashMap<>();
	private final Map<Class<? extends Event>, Set<Method>> handlers = new HashMap<>();

	public void unregisterAll()
	{
		handlers.clear();
	}

	public void unregisterAll(Class<? extends Event> event)
	{
		if(handlers.containsKey(event)) handlers.get(event).clear();
	}

	@SuppressWarnings("unchecked")
	public void register(Object object)
	{
		for(Method method: object.getClass().getDeclaredMethods())
		{
			if(isHandler(method))
			{
				Class<? extends Event> event = (Class<? extends Event>)method.getParameterTypes()[0];
				if(!handlers.containsKey(event)) handlers.put(event, new TreeSet<>(new HandlerComparator()));
				handlers.get(event).add(method);
			}
		}
		instances.put(object.getClass(), object);
	}

	@SuppressWarnings("unchecked")
	public void unregister(Object object)
	{
		for(Method method: object.getClass().getDeclaredMethods())
		{
			if(isHandler(method))
			{
				Class<? extends Event> event = (Class<? extends Event>)method.getParameterTypes()[0];
				if(handlers.containsKey(event)) handlers.get(event).remove(method);
			}
		}
		instances.remove(object.getClass());
	}

	@SuppressWarnings("unchecked")
    public synchronized void post(final Event event)
	{
		if(handlers.containsKey(event.getClass()))
		{
			for(Method handler: handlers.get(event.getClass()))
			{
				try
				{
					handler.invoke(instances.get(handler.getDeclaringClass()), event);
				}
				catch (IllegalAccessException | InvocationTargetException e)
				{
					e.printStackTrace();
				}
			}
		}
    }

	@SuppressWarnings("unchecked")
	public boolean registered(Object object)
	{
		for(Method method: object.getClass().getDeclaredMethods())
		{
			if(isHandler(method))
			{
				Class<? extends Event> event = (Class<? extends Event>)method.getParameterTypes()[0];

				if(handlers.containsKey(event)) if(handlers.get(event).contains(method)) return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean isHandler(Method method)
	{
		return
			method.getParameterTypes().length == 1 &&
			Event.class.isAssignableFrom(method.getParameterTypes()[0]) &&
			method.isAnnotationPresent(com.octopod.util.event.v2.EventHandler.class);
	}

	private class HandlerComparator implements Comparator<Method>
	{
		@Override
		public int compare(Method o1, Method o2)
		{
			com.octopod.util.event.v2.EventHandler h1 = o1.getAnnotation(com.octopod.util.event.v2.EventHandler.class);
			com.octopod.util.event.v2.EventHandler h2 = o2.getAnnotation(EventHandler.class);
			Integer p1 = h1 != null ? h1.priority().getPriority() : EventPriority.NORMAL.getPriority();
			Integer p2 = h2 != null ? h2.priority().getPriority() : EventPriority.NORMAL.getPriority();
			return p1.compareTo(p2);
		}
	}
}
